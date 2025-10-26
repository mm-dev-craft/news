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
public class SpiegelCrawler extends AbstractCrawler {

    private static final String SPIEGEL_DE = "https://www.spiegel.de/";

    public SpiegelCrawler(CrawlerClient client) {
        super(client);
    }

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

}
