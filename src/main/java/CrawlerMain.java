import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CrawlerMain {

    private static String CRAWLER_STORAGE_BASE_PATH = "crawler_data";
    private static String CRAWLER_DATA_FILE = "travel.xml";
    private static int NUMBER_OF_CRAWLERS = 12;
    private static String SEED = "https://edition.cnn.com/travel";
    private final static Logger LOGGER = Logger.getLogger(CrawlerMain.class);

    public static void main(String[] args) throws IOException {

        // Add crawl storage
        File crawlStorage = new File(CRAWLER_STORAGE_BASE_PATH);
        String crawlStorageOriginalPath = crawlStorage.getAbsolutePath();
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageOriginalPath);

        // Add page fetcher
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotsTxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        // Add crawler statistics
        CrawlerStatistics crawlerStatistics = new CrawlerStatistics();
        String crawlerDataFilePath = crawlStorageOriginalPath + File.separator + CRAWLER_DATA_FILE;
        File crawlerData = new File(crawlerDataFilePath);
        if (crawlerData.createNewFile()) {
            LOGGER.info("File created: " + crawlerData.getName());
        } else {
            LOGGER.info("File already exists.");
        }

        FileWriter writer = new FileWriter(crawlerDataFilePath);
        CrawlController.WebCrawlerFactory<HtmlCrawler> factory = () -> new HtmlCrawler(SEED, crawlerStatistics, writer);

        try {
            // Add crawl controller
            CrawlController controller = new CrawlController(config, pageFetcher, robotsTxtServer);
            controller.addSeed(SEED);
            controller.start(factory, NUMBER_OF_CRAWLERS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            writer.close();
            LOGGER.info("Finish writing the file " + CRAWLER_DATA_FILE + ".");
        }

    }

}
