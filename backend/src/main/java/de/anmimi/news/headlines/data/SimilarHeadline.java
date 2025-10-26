package de.anmimi.news.headlines.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
@Document(collection = "similar-headlines")
public class SimilarHeadline {
    @Id
    private String id;
    private final String summary;
    private final int similarityScore;

    private final Headline headline;
}
