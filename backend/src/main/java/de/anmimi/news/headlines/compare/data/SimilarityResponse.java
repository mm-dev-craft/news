package de.anmimi.news.headlines.compare.data;

public class SimilarityResponse {
    private String summary;
    private int similarityScore;

    public SimilarityResponse() {
    }

    public String getSummary() {
        return summary;
    }

    public int getSimilarityScore() {
        return similarityScore;
    }
}
