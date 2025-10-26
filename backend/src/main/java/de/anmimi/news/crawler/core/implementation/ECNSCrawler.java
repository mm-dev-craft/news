package de.anmimi.news.crawler.core.implementation;

import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.LinkAndDescription;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ECNSCrawler extends AbstractCrawler {

    private static final String ECNS = "http://www.ecns.cn/";

    public ECNSCrawler(CrawlerClient client) {
        super(client);
    }

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

}
