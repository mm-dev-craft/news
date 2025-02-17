package de.anmimi.news.crawler;

import de.anmimi.news.crawler.core.Crawler;
import de.anmimi.news.crawler.filter.ContentFilter;
import de.anmimi.news.crawler.filter.FilterdContent;
import de.anmimi.news.crawler.core.HeadlineSourceAndContent;
import de.anmimi.news.data.Headline;
import de.anmimi.news.headlines.HeadlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class CrawlerService {

    private final List<Crawler> crawlers;
    private final HeadlineService headlineService;
    private final ContentFilter filter;

    @Autowired
    public CrawlerService(List<Crawler> crawlers, HeadlineService headlineService, ContentFilter filter) {
        this.crawlers = crawlers;
        this.headlineService = headlineService;
        this.filter = filter;
    }

    @Scheduled(fixedDelayString = "${de.anmimi.headlines.crawlingDelayInHours}", timeUnit = TimeUnit.HOURS, initialDelay = 0)
    public void loadHeadlines() {
        Stream<Headline> headlinesStream = crawlers.stream().map(Crawler::crawleForHeadlines)
                .collect(Collectors.collectingAndThen(Collectors.toList(), c -> c.stream()
                        .map(CompletableFuture::join)))
                .flatMap(this::filterAndParseToHeadlines);

        List<Headline> headlines = filterHeadlinesAgainstLatestDatabaseEntries(headlinesStream);

        log.info("Found: {} new Headlines compared to last hour", headlines.size());
        headlineService.saveNewHeadlines(headlines);
    }

    private List<Headline> filterHeadlinesAgainstLatestDatabaseEntries(Stream<Headline> headlines) {
        List<Headline> latestHeadlinesFromDatabase = headlineService.getNewestHeadlines();
        List<Headline> list = headlines.toList();
        return list.stream().filter(h -> !latestHeadlinesFromDatabase.contains(h)).toList();
    }

    private Stream<Headline> filterAndParseToHeadlines(HeadlineSourceAndContent headlineSourceAndContent) {
        log.debug("Crawled Headlines count for: {} is {}", headlineSourceAndContent.source(), headlineSourceAndContent.content().size());
        FilterdContent filterdContent = filter.filterRelevantContent(headlineSourceAndContent.content());
        return filterdContent.content()
                .stream()
                .map(titleAndLinkWithKeyword ->
                        new Headline(titleAndLinkWithKeyword.title(),
                                titleAndLinkWithKeyword.link(),
                                headlineSourceAndContent.source(),
                                titleAndLinkWithKeyword.matchingKeywords(),
                                LocalDateTime.now())
                );
    }

}
