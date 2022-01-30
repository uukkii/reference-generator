package Server;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {

    private final String original;
    private final String link;
    private final int rank;
    private final int count;

    public Link(
            @JsonProperty("original") String original,
            @JsonProperty("link") String link,
            @JsonProperty("rank") int rank,
            @JsonProperty("count") int count
    ) {
       this.original = original;
       this.link = link;
       this.rank = rank;
       this.count = count;
    }

    public String getOriginal() {
        return original;
    }

    public String getLink() {
        return link;
    }

    public int getRank() {
        return rank;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\"original\": " + original +
                "\n\"link\": " + link +
                "\n\"rank\": " + rank +
                "\n\"count\": " + count +
                "\n}";
    }
}