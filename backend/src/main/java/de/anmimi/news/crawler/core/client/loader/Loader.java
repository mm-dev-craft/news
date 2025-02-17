package de.anmimi.news.crawler.core.client.loader;


import org.jsoup.nodes.Document;

public interface Loader {
    Document load(String url);
}
