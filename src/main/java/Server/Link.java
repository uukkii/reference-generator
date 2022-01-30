package Server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class Link {

    private final String original;
    private final String link;
    private int rank;
    private final AtomicInteger count = new AtomicInteger();

    public Link(
            @JsonProperty("original") String original,
            @JsonProperty("link") String link,
            @JsonProperty("rank") int rank,
            @JsonProperty("count") int count
    ) {
       this.original = original;
       this.link = link;
       this.rank = rank;
       this.count.set(count);
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
        return count.get();
    }

    public void setCount(int count) {
       this.count.addAndGet(count);
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\"original\": " + "\"" + original + "\"" +
                "\n\"link\": " + "\"" + link + "\"" +
                "\n\"rank\": " + "\"" + rank + "\"" +
                "\n\"count\": " + "\"" + count + "\"" +
                "\n}";
    }
}