package de.anmimi.news.crawler.core.implementation;

import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.LinkAndDescription;
import de.anmimi.news.crawler.core.TitleAndLink;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class AljazeeraCrawler extends AbstractCrawler {

    private static final String ALJAZEERA_COM = "https://www.aljazeera.com";

    public AljazeeraCrawler(CrawlerClient client) {
        super(client);
    }

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

}
