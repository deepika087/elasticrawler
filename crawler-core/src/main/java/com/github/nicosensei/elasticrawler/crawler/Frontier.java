package com.github.nicosensei.elasticrawler.crawler;

import java.util.List;

/**
 * Generic crawl frontier interface. 
 * The frontier handles the queue of URLs that have been discovered and crawled.
 * 
 * @author nicolas
 *
 */
public interface Frontier {

	/**
	 * Pulls a list of {@link CrawlUrl} from the frontier
	 * @param size the number of elements to pull.
	 * @return a list of {@link CrawlUrl}. The list may never be null, but may be empty if
	 * no more elements are available. NOte that an empty frontier does not mean that the crawl is
	 * over, simply that at this point in time there are no more URLs to crawl. Still the crawler
	 * might be downloading resources or extarcting links form other resources.
	 */
	List<CrawlUrl> pull(final int size); 
	
	/**
	 * Pushes a single  {@link CrawlUrl} to the frontier.
	 * @param crawlURL
	 */
	void push(CrawlUrl crawlURL);
	
	/**
	 * Pushes a list of {@link CrawlUrl} to the frontier.
	 * @param crawlURLs
	 */
	void push(List<CrawlUrl> crawlURLs);
		
}
