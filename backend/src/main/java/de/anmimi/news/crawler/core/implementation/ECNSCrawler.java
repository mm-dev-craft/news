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
public class ECNSCrawler extends AbstractCrawler {

    private final CrawlerClient client;
    private static final String ECNS = "http://www.ecns.cn/";

    @Override
    protected Elements executeCrawling() {
        return client.load(ECNS, "a");
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        String title = element.text();
        String href = element.attr("href");
        return new TitleAndLink(title, href);
    }

    @Override
    protected List<TitleAndLink> extractMultipleHeadLinesAndLinksFromDom(Element elements) {
        log.warn("The ECNS does not provide multiple headlines and links. Skipping this element.");
        return List.of();
    }
}
