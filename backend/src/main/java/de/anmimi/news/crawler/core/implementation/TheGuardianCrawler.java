package de.anmimi.news.crawler.core.implementation;

import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.LinkAndDescription;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class TheGuardianCrawler extends AbstractCrawler {

    private static final String THE_GUARDIAN = "https://www.theguardian.com";
    private static final String THE_GUARDIAN_INTERNATIONAL = THE_GUARDIAN + "/international";

    public TheGuardianCrawler(CrawlerClient client) {
        super(client);
    }

    @Override
    protected Elements executeCrawling() {
        return client.load(THE_GUARDIAN, "a[data-link-name='article']");
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        String href = element.attr("href");
        log.debug("Original href: {}", href);
        if (!href.startsWith("http")) {
            href = THE_GUARDIAN + href;
            log.debug("New href: {}", href);
        }
        return new TitleAndLink(element.text(), href);
    }

}
