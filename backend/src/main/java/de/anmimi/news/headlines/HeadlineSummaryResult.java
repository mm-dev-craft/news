package de.anmimi.news.headlines;

import lombok.Value;

@Value
public class HeadlineSummaryResult {
    String id;
    String title;
    String link;
    String source;
    String summary;
    int similarityScore;
}