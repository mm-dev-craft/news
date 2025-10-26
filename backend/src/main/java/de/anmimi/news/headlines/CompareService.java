package de.anmimi.news.headlines;

import de.anmimi.news.crawler.core.Crawler;
import de.anmimi.news.crawler.core.LinkAndDescription;
import de.anmimi.news.crawler.core.TitleAndLink;
import de.anmimi.news.headlines.compare.CompareAIClient;
import de.anmimi.news.headlines.data.*;
import de.anmimi.news.headlines.compare.data.SimilarityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompareService {

    private static final String SYSTEM_TEMPLATE_KEYWORDS_FILTER = """
            You are an expert in the field of {keywords}.
            You will get a list of titles and links you should check if they are contextual similar to a provided title.
            If they are you should return the title and the link.
            If you are not sure you should also return the title and the link.
            """;
    private static final String USER_TEMPLATE_TITLE_FILTER = """
            Please check if the following titles are contextual similar to the provided title.
            Title to check against: {title}
            Titles and links to check against:
            {headlines}
            """;

    private static final String SYSTEM_TEMPLATE_CONTEXTUAL_SIMILIAR_CHECK =
            "You should check if the provided headlines are contextually similar to the base headline.\n"
                    + "Please also include a short summary of the article. Calculate also similiarty score.\n";

    private static final String USER_TEMPLATE_CONTEXTUAL_SIMILIAR_CHECK =
            "The base headline is:\n{baseHeadline}\n"
                    + "Please check if the following articles are contextually similar to the base headline and calculate a similiarty score.\n" +
                    "If the article is not similar please return a score of 0.\n" +
                    "Also return a summary" +
                    "Articles:\n{articles}\n";

    private final ChatClient gpt4o;
    private final Map<String, Crawler> crawlers;
    private final HeadlineService headlines;
    private final Set<String> keywords;
    private final CompareAIClient compareAIClient;
    private final SimilarHeadlineRepository similarHeadlineRepository;
    private final HeadlineRepository headlineRepository;

    public List<HeadlineSummaryResult> searchForSimiliar(String headlineId) {
        List<SimilarHeadline> similarHeadlinesFromDb = similarHeadlineRepository.findAllByHeadline_Id(headlineId);
        if (!similarHeadlinesFromDb.isEmpty()) {
            return similarHeadlinesFromDb
                    .stream()
                    .map(se ->
                            new HeadlineSummaryResult(
                                    se.getHeadline().getId(),
                                    se.getHeadline().getTitle(),
                                    se.getHeadline().getLink(),
                                    se.getHeadline().getSource(),
                                    se.getSummary(),
                                    se.getSimilarityScore())
                    )
                    .toList();
        }

        return searchExternalForSimilarHeadlines(headlineId);
    }

    private List<HeadlineSummaryResult> searchExternalForSimilarHeadlines(String headlineId) {
        SimiliarHeadlines similar = headlines.findHeadlinesContainingSameKeywords(headlineId);
        validateCrawler(similar.baseHeadline().getSource());

        Set<Headline> filtered = filterByActiveCrawlers(similar.similiarHeadlines());
        Set<TitleAndLink> titleAndLinks = filterHeadlinesByTitleSimlarity(similar.title(), filtered);

        Set<Headline> relevantHeadlines = matchToTitleAndLinks(filtered, titleAndLinks);
        List<LinkAndDescription> articlesContent = fetchArticleContent(relevantHeadlines);
        List<SimilarityResponse> responses = evaluateSimilarityWithAI(similar.baseHeadline(), articlesContent);

        List<HeadlineSummaryResult> headlineSummaryResults = buildResults(relevantHeadlines, articlesContent, responses);

        headlineSummaryResults.forEach(h -> similarHeadlineRepository.save(new SimilarHeadline(
                h.getSummary(),
                h.getSimilarityScore(),
                headlineRepository.findById(h.getId()).orElseThrow()
        )));
        return headlineSummaryResults;
    }


    private Set<Headline> matchToTitleAndLinks(Set<Headline> source, Set<TitleAndLink> filteredTitles) {
        return source.stream()
                .filter(h -> filteredTitles.stream().anyMatch(t -> t.title().equals(h.getTitle())))
                .collect(Collectors.toSet());
    }

    private List<SimilarityResponse> evaluateSimilarityWithAI(Headline baseHeadline, List<LinkAndDescription> articlesContent) {
        return articlesContent.stream()
                .map(article -> createPrompt(baseHeadline, article))
                .map(prompt -> compareAIClient.sendChatAsync(prompt, new ParameterizedTypeReference<SimilarityResponse>() {
                }))
                .map(CompletableFuture::join)
                .toList();
    }

    private Prompt createPrompt(Headline baseHeadline, LinkAndDescription article) {
        return PromptTemplate
                .builder()
                .template(SYSTEM_TEMPLATE_CONTEXTUAL_SIMILIAR_CHECK + USER_TEMPLATE_CONTEXTUAL_SIMILIAR_CHECK)
                .variables(Map.of("baseHeadline", baseHeadline.getLink(),
                        "articles", article.description() + " - " + article.link()))
                .build()
                .create();
    }

    private List<LinkAndDescription> fetchArticleContent(Set<Headline> headlines) {
        return headlines.stream()
                .map(h -> crawlers.get(h.getSource().toLowerCase()).crawleText(h.getLink()))
                .toList()
                .stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private List<HeadlineSummaryResult> buildResults(Set<Headline> headlines,
                                                     List<LinkAndDescription> content,
                                                     List<SimilarityResponse> responses) {
        List<HeadlineSummaryResult> results = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            LinkAndDescription article = content.get(i);
            SimilarityResponse similarity = responses.get(i);

            headlines.stream()
                    .filter(h -> h.getLink().equals(article.link()))
                    .findFirst()
                    .ifPresent(matched -> results.add(
                            new HeadlineSummaryResult(
                                    matched.getId(),
                                    matched.getTitle(),
                                    matched.getLink(),
                                    matched.getSource(),
                                    similarity.getSummary(),
                                    similarity.getSimilarityScore()
                            )
                    ));
        }
        return results;
    }

    private void validateCrawler(String source) {
        if (!crawlers.containsKey(source.toLowerCase())) {
            throw new IllegalStateException("Base Headline crawler not active");
        }
    }

    private Set<Headline> filterByActiveCrawlers(Set<Headline> allHeadlines) {
        return allHeadlines.stream()
                .filter(h -> crawlers.containsKey(h.getSource().toLowerCase()))
                .collect(Collectors.toSet());
    }

    private Set<TitleAndLink> filterHeadlinesByTitleSimlarity(String title, Set<Headline> filtered) {
        return gpt4o.prompt()
                .system(spec -> spec.text(SYSTEM_TEMPLATE_KEYWORDS_FILTER)
                        .param("keywords", String.join(", ", keywords)))
                .user(userSpec -> userSpec
                        .text(USER_TEMPLATE_TITLE_FILTER)
                        .param("title", title)
                        .param("headlines", filtered.stream()
                                .map(h -> h.getTitle() + " - " + h.getLink())
                                .collect(Collectors.joining("\n"))))
                .call()
                .entity(new ParameterizedTypeReference<>() {
                });
    }

}