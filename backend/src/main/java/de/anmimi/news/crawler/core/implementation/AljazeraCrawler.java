package de.anmimi.news.crawler.core.implementation;

import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AljazeraCrawler extends AbstractCrawler {

    private static final String ALJAZEERA_COM = "https://www.aljazeera.com";
    private final CrawlerClient client;

    @Override
    protected Elements executeCrawling() {
        Elements elements = client.load(ALJAZEERA_COM, "a.u-clickable-card__link");
        log.info("Found {} elements", elements.size());
        return elements;
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        String link = ALJAZEERA_COM + element.attr("href");
        String headlineText = element.select("span").text();
        return new TitleAndLink(headlineText, link);
    }

    @Override
    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element elements) {
        log.warn("The Aljazera does not provide multiple headlines and links. Skipping this element.");
        return List.of();
    }

}
