package de.anmimi.news.headlines;

import de.anmimi.news.headlines.data.Headline;
import de.anmimi.news.headlines.data.HeadlineRepository;
import de.anmimi.news.headlines.data.SimiliarHeadlines;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class HeadlineService {

    private final HeadlineRepository headlines;
    private final long newsRenewalTime;

    @Autowired
    public HeadlineService(HeadlineRepository headlineRepository,
                           @Value("${de.anmimi.headlines.crawlingDelayInHours}") Long newsRenewalTime) {
        this.headlines = headlineRepository;
        this.newsRenewalTime = newsRenewalTime;
    }

    @Cacheable("headlines")
    public List<Headline> getNewestHeadlines() {
        return headlines.findByCrawlingDateAfterOrderByCrawlingDateDesc(LocalDateTime.now()
                .minusHours(newsRenewalTime));
    }

    @CacheEvict(allEntries = true, cacheNames = "headlines")
    public void saveNewHeadlines(List<Headline> headlines) {
        List<Headline> filteredHeadlines = headlines.stream()
                .filter(h -> !this.headlines.existsByLinkAndSource(h.getLink(), h.getSource()))
                .toList();

        log.info("{} entries will be saved", filteredHeadlines.size());
        if (filteredHeadlines.isEmpty()) {
            return;
        }

        this.headlines.saveAll(filteredHeadlines);
    }

    public SimiliarHeadlines findHeadlinesContainingSameKeywords(String headlineId) {
        Headline headlineToCompare = headlines.findById(headlineId).orElseThrow(NotFoundException::new);
        log.info("Found headlines {} and searching for headlines with keywords: {}", headlineToCompare.getTitle(), String.join(", ", headlineToCompare.getKeywords()));
        Set<Headline> headlinesWithSameKeywords = headlines.findHeadlineByKeywordsIn(headlineToCompare.getKeywords());
        log.info("Found {} headlines with same keywords", headlinesWithSameKeywords.size());
        return new SimiliarHeadlines(headlineToCompare.getTitle(), headlinesWithSameKeywords);
    }

    public List<Headline> getAllHeadlines() {
        return headlines.findAll(Sort.by("crawlingDate"));
    }

    public List<Headline> findHeadLinesOfLast(int pastHours) {
        return headlines.findByCrawlingDateAfterOrderByCrawlingDateDesc(LocalDateTime.now()
                .minusHours(pastHours));
    }
}
