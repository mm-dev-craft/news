package de.anmimi.news.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
@Document(collection = "headlines")
public class Headline {

    @Id
    private String id;

    @EqualsAndHashCode.Exclude
    private final String title;
    private final String link;
    private final String source;
    @EqualsAndHashCode.Exclude
    private final Set<String> keywords;
    @EqualsAndHashCode.Exclude
    private final LocalDateTime crawlingDate;

}
