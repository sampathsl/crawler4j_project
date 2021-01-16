import lombok.Getter;

@Getter
public class CrawlerStatistics {

    private int totalPageCount = 0;
    private int totalLinksCount = 0;

    public void addPageCount() {
        totalPageCount++;
    }

    public void addLinksCount(int linksCount) {
        totalLinksCount += linksCount;
    }
}
