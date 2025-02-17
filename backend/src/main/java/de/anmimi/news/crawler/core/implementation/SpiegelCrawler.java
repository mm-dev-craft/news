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
public class SpiegelCrawler extends AbstractCrawler {

    private static final String SPIEGEL_DE = "https://www.spiegel.de/";
    private final CrawlerClient client;

    @Override
    protected Elements executeCrawling() {
        return client.load(SPIEGEL_DE, "article[aria-label]");
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        String title = element.attr("aria-label").toLowerCase();
        String link = element.getElementsByTag("a").attr("href");
        return new TitleAndLink(title, link);
    }

    @Override
    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element element) {
        log.warn("The Spiegel does not provide multiple headlines and links. Skipping this element.");
        return List.of();
    }

}
