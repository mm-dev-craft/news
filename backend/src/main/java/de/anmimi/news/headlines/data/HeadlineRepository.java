package de.anmimi.news.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HeadlineRepository extends MongoRepository<Headline, Long> {
    List<Headline> findByCrawlingDateAfterOrderByCrawlingDateDesc(LocalDateTime localDateTime);

    boolean existsByLinkAndSource(String link, String source);

    Set<Headline> findHeadlineByKeywordsIn(Set<String> keywords);

    Optional<Headline> findById(String headlineId);
}
