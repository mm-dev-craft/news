package de.anmimi.news.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.anmimi.news.crawler.core.Crawler;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.client.JSoupCrawlerClient;
import de.anmimi.news.crawler.core.client.loader.JSoupLoader;
import de.anmimi.news.crawler.core.client.loader.SeleniumLoader;
import de.anmimi.news.crawler.core.implementation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrawlerConfiguration {

   @Bean
    public CrawlerClient jsoupCrawlerClient() {
        return new JSoupCrawlerClient(new JSoupLoader());
    }

    @Bean
    public CrawlerClient seleniumCrawlerClient() {
        return new JSoupCrawlerClient(new SeleniumLoader());
    }

    @Bean
    public Crawler theguardian(@Qualifier("jsoupCrawlerClient") CrawlerClient crawlerClient) {
        return new TheGuardianCrawler(crawlerClient);
    }

    @Bean
    public Crawler aljazeera(@Qualifier("seleniumCrawlerClient") CrawlerClient crawlerClient) {
        return new AljazeeraCrawler(crawlerClient);
    }

    @Bean
    public Crawler ecns(@Qualifier("jsoupCrawlerClient") CrawlerClient crawlerClient) {
        return new ECNSCrawler(crawlerClient);
    }

    @Bean
    public Crawler spiegel(@Qualifier("jsoupCrawlerClient") CrawlerClient crawlerClient) {
        return new SpiegelCrawler(crawlerClient);
    }

    @Bean
    public Crawler ninegag(@Qualifier("seleniumCrawlerClient") CrawlerClient crawlerClient,
                           ObjectMapper objectMapper) {
        return new NineGagCrawler(crawlerClient, objectMapper);
    }

    @Bean
    public Crawler nyt(@Qualifier("seleniumCrawlerClient") CrawlerClient crawlerClient) {
        return new NYTCrawler(crawlerClient);
    }
}
