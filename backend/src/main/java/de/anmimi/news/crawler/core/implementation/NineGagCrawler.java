package de.anmimi.news.crawler.core.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Slf4j
public class NineGagCrawler extends AbstractCrawler {

    private static final String NINEGAG = "https://9gag.com";
    private static final Pattern JSON_REGEX = Pattern.compile("window\\._config\\s*=\\s*JSON\\.parse\\(\"(.*?)\"\\);", Pattern.DOTALL);

    private final CrawlerClient client;
    private final ObjectMapper mapper;


    @Override
    protected Elements executeCrawling() {
        Elements scriptElements = client.load(NINEGAG, "script");
        log.info("Found {} script elements", scriptElements.size());
        return scriptElements;
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        log.warn("Ninegag does not support single element extraction");
        return new TitleAndLink("", "");
    }

    @Override
    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element element) {
        Optional<String> jsonEscapedOpt = extractEscapedJsonString(element);
        if (jsonEscapedOpt.isEmpty()) {
            return List.of();
        }

        try {
            String json = unescapeJsonString(jsonEscapedOpt.get());
            log.trace("Extracted JSON: {}", json);

            JsonNode postsNode = extractPostsNode(json);
            if (postsNode == null) {
                return List.of();
            }

            return extractHeadlinesFromPosts(postsNode);
        } catch (Exception e) {
            log.error("Error parsing JSON", e);
            return List.of();
        }
    }

    private Optional<String> extractEscapedJsonString(Element element) {
        Matcher matcher = JSON_REGEX.matcher(element.html());
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        log.warn("No matching JSON found in element");
        return Optional.empty();
    }

    private String unescapeJsonString(String jsonEscaped) throws IOException {
        // Let ObjectMapper handle the unescaping by reading it as a String literal
        return mapper.readValue("\"" + jsonEscaped + "\"", String.class);
    }

    private JsonNode extractPostsNode(String json) throws IOException {
        JsonNode postsNode = mapper.readTree(json).path("data").path("posts");
        if (!postsNode.isArray()) {
            log.warn("No posts found in JSON");
            return null;
        }
        return postsNode;
    }

    private List<TitleAndLink> extractHeadlinesFromPosts(JsonNode postsNode) {
        return StreamSupport.stream(postsNode.spliterator(), false)
                .map(this::extractTitleAndLink)
                .collect(Collectors.toList());
    }

    private TitleAndLink extractTitleAndLink(JsonNode postNode) {
        String title = postNode.path("title").asText("");
        String url = postNode.path("url").asText("");
        log.info("Extracted headline: Title='{}', URL='{}'", title, url);
        return new TitleAndLink(title, url);
    }
}
