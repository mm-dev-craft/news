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
public class TheGuardianCrawler extends AbstractCrawler {

    private static final String THE_GUARDIAN = "https://www.theguardian.com/international";
    private final CrawlerClient client;

    @Override
    protected Elements executeCrawling() {
        return client.load(THE_GUARDIAN, "a[data-link-name='article']");
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        return new TitleAndLink(element.text(), element.attr("href"));
    }

    @Override
    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element element) {
        log.warn("The Guardian does not provide multiple headlines and links. Skipping this element.");
        return List.of();
    }
}
