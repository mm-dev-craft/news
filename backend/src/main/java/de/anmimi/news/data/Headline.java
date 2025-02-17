package de.anmimi.news.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
@Document(collection = "headlines")
public class Headline {

    @EqualsAndHashCode.Exclude
    private String title;
    private String link;
    private String source;
    @EqualsAndHashCode.Exclude
    private Set<String> keywords;
    @EqualsAndHashCode.Exclude
    private LocalDateTime crawlingDate;

}
