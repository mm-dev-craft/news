package de.anmimi.news.crawler.core;

import de.anmimi.news.crawler.core.client.CrawlerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCrawler implements Crawler {

    protected final CrawlerClient client;

    @Override
    @Async
    public CompletableFuture<HeadlineSourceAndContent> crawleForHeadlines() {
        String simpleName = getSourceNameFromCrawler();
        log.debug("Starting crawl for source: {}", simpleName);
        long currentTime = System.currentTimeMillis();

        Elements elements = executeCrawling();
        log.debug("Crawling executed, found {} elements", elements.size());

        List<TitleAndLink> titleAndLinks = removeDupplicateEntries(elements);

        log.debug("Crawling for {} took time: {} ms", simpleName, System.currentTimeMillis() - currentTime);
        return CompletableFuture.completedFuture(new HeadlineSourceAndContent(simpleName, titleAndLinks));
    }

    private List<TitleAndLink> removeDupplicateEntries(Elements elements) {
        List<TitleAndLink> result = new ArrayList<>();
        Set<String> seenLinks = new HashSet<>();
        log.debug("Removing duplicate entries from {} elements", elements.size());
        for (Element element : elements) {
            processOneTitleAndLink(element, seenLinks, result);
            processMultipleTitlesAndLinks(element, seenLinks, result);
        }
        log.debug("Removed duplicates, {} unique entries found", result.size());
        return result;
    }

    private void processOneTitleAndLink(Element element, Set<String> seenLinks, List<TitleAndLink> result) {
        TitleAndLink titleAndLink = extractHeadLinesAndLinksFromDom(element);
        log.debug("Processing element, extracted title: {}, link: {}", titleAndLink.title(), titleAndLink.link());
        filterKnownHeadlines(seenLinks, result, titleAndLink);
    }

    private void processMultipleTitlesAndLinks(Element element, Set<String> seenLinks, List<TitleAndLink> result) {
        List<TitleAndLink> multipleTitlesAndLinks = extractMultipleHeadLinesAndLinksFromDom(element);
        log.debug("Processing multiple titles and links, found {} entries", multipleTitlesAndLinks.size());
        for (TitleAndLink multipleTitleAndLink : multipleTitlesAndLinks) {
            log.debug("Processing multiple entry, extracted title: {}, link: {}", multipleTitleAndLink.title(), multipleTitleAndLink.link());
            filterKnownHeadlines(seenLinks, result, multipleTitleAndLink);
        }
    }

    private void filterKnownHeadlines(Set<String> seenLinks, List<TitleAndLink> result, TitleAndLink titleAndLink) {
        if (seenLinks.add(titleAndLink.link())) {
            log.debug("Adding new entry: title: {}, link: {}", titleAndLink.title(), titleAndLink.link());
            result.add(titleAndLink);
        } else {
            log.debug("Duplicate entry found, skipping: title: {}, link: {}", titleAndLink.title(), titleAndLink.link());
        }
    }

    private String getSourceNameFromCrawler() {
        String simpleName = this.getClass().getSimpleName();
        simpleName = simpleName.substring(0, simpleName.indexOf("Crawler"));
        log.debug("Source name extracted from crawler: {}", simpleName);
        return simpleName;
    }

    @Override
    public CompletableFuture<LinkAndDescription> crawleText(String url) {
        Elements elements = client.load(url, "p");
        String description = elements.text();
        return CompletableFuture.completedFuture(new LinkAndDescription(url, description));
    }

    protected abstract Elements executeCrawling();

    protected abstract TitleAndLink extractHeadLinesAndLinksFromDom(Element element);

    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element element) {
        log.debug("The {} does not provide multiple headlines and links. Skipping this element.", this.getClass().getSimpleName());
        return List.of();
    }

    ;

}