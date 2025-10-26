package de.anmimi.news.headlines.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SimilarHeadlineRepository extends MongoRepository<SimilarHeadline, String> {
    List<SimilarHeadline> findAllByHeadline_Id(String headlineId);
}
