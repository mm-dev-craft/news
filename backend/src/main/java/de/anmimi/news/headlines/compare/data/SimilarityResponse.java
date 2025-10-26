package de.anmimi.news.headlines;

// Expected response object for similarity evaluation.
public class SimilarityResponse {
    private String summary;
    private int similarityScore;

    // Default constructor and getters (or use Lombok @Data/@Value if preferred)
    public SimilarityResponse() {
    }

    public String getSummary() {
        return summary;
    }

    public int getSimilarityScore() {
        return similarityScore;
    }
}
