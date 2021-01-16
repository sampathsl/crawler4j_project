import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlCrawler extends WebCrawler {

    private String seed;
    private CrawlerStatistics crawlerStatistics;
    private FileWriter fileWriter;

    public HtmlCrawler(String seed, CrawlerStatistics crawlerStatistics, FileWriter fileWriter) {
        this.seed = seed;
        this.crawlerStatistics = crawlerStatistics;
        this.fileWriter = fileWriter;
    }

    private final static Pattern EXCLUSIONS
            = Pattern.compile(".*(\\.(css|js|xml|gif|jpg|png|mp3|mp4|zip|gz|pdf))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String urlString = url.getURL().toLowerCase();
        return !EXCLUSIONS.matcher(urlString).matches()
                && urlString.startsWith(seed);
    }

    @Override
    public void visit(Page page) {
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            crawlerStatistics.addPageCount();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            crawlerStatistics.addLinksCount(links.size());
            String title = htmlParseData.getTitle();
            String ref = "<DOC> <DOCNO>" + crawlerStatistics.getTotalPageCount() + "</DOCNO>" + title + "</DOC>\n";
            try {
                fileWriter.write(ref);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
