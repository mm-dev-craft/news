package de.anmimi.news.crawler.core.client.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
public class SeleniumLoader implements Loader {

    private static final String SELENIUM_GRID_URL = "http://localhost:4444/wd/hub";

    private RemoteWebDriver createWebDriver() throws MalformedURLException {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--remote-debugging-port=9222");

        return new RemoteWebDriver(new URL(SELENIUM_GRID_URL), chromeOptions);
    }

    @Override
    public Document load(String url) {
        log.info("Requesting URL: {}", url);
        RemoteWebDriver driver = null;
        try {
            driver = createWebDriver();
            driver.get(url);
            log.debug("Successfully retrieved page source for: {}", url);
            String pageSource = driver.getPageSource();
            if (pageSource == null) {
                log.error("Failed to retrieve page source for: {}", url);
                return new Document("Failed to retrieve page source");
            }
            return Jsoup.parse(pageSource);
        } catch (MalformedURLException e) {
            log.error("Invalid URL: {}", url, e);
            return new Document("Invalid URL");
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

    }

}
