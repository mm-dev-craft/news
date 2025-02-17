package de.anmimi.news.crawler.core.client.loader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JSoupLoader implements Loader{

    @Override
    public Document load(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("Error loading: {}", url, e);
            return new Document("Error loading");
        }
    }
}
