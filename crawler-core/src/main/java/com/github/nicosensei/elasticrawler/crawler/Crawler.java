/**
 * 
 */
package com.github.nicosensei.elasticrawler.crawler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.Page.Type;
import edu.uci.ics.crawler4j.fetcher.CustomFetchStatus;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * A generic crawler abstract implementation.
 * 
 *  Heavily based on Crawler4j's {@link WebCrawler}, but intended to run continuously.
 * 
 * @author nicolas
 *
 */
public abstract class Crawler implements Runnable {
	
	protected static final Logger logger = LoggerFactory.getLogger(Crawler.class);
	
	private String id;
		
	private int pullSize;
	
	private CrawlConfig crawlConfig;
	
	/**
	 * Time to wait (in milliseconds) between two pulls from the frontier if the previous
	 * pull did not bring URLs.
	 */
	private long pullRetryDelay;
	
	/**
	 * The parser that is used by this crawler instance to parse the content of
	 * the fetched pages.
	 */
	private Parser parser;

	/**
	 * The fetcher that is used by this crawler instance to fetch the content of
	 * pages from the web.
	 */
	private PageFetcher pageFetcher;

	/**
	 * The RobotstxtServer instance that is used by this crawler instance to
	 * determine whether the crawler is allowed to crawl the content of each
	 * page.
	 */
	private RobotstxtServer robotsTxtServer;

	/**
	 * The Frontier object that manages the crawl queues.
	 */
	private Frontier frontier;
	
	/**
	 * The crawl history store.
	 */
	private CrawlHistory crawlHistory;

	public Crawler(
			final String id,
			final CrawlConfig crawlConfig,
			final int pullSize,
			final Frontier frontier,
			final RobotstxtServer robotsTxtServer,
			final PageFetcher pageFetcher,
			final Parser parser) {
		this.id = id;
		this.crawlConfig = crawlConfig;
		this.pullSize = pullSize;
		this.frontier = frontier;
		this.robotsTxtServer = robotsTxtServer;
		this.pageFetcher = pageFetcher;
		this.parser = parser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPullSize() {
		return pullSize;
	}

	public void setPullSize(int pullSize) {
		this.pullSize = pullSize;
	}

	public CrawlConfig getCrawlConfig() {
		return crawlConfig;
	}

	public void setCrawlConfig(CrawlConfig crawlConfig) {
		this.crawlConfig = crawlConfig;
	}

	public long getPullRetryDelay() {
		return pullRetryDelay;
	}

	public void setPullRetryDelay(long pullRetryDelay) {
		this.pullRetryDelay = pullRetryDelay;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public PageFetcher getPageFetcher() {
		return pageFetcher;
	}

	public void setPageFetcher(PageFetcher pageFetcher) {
		this.pageFetcher = pageFetcher;
	}

	public RobotstxtServer getRobotsTxtServer() {
		return robotsTxtServer;
	}

	public void setRobotsTxtServer(RobotstxtServer robotsTxtServer) {
		this.robotsTxtServer = robotsTxtServer;
	}

	public Frontier getFrontier() {
		return frontier;
	}

	public void setFrontier(Frontier frontier) {
		this.frontier = frontier;
	}

	/**
	 * This function is called just before starting the crawl by this crawler
	 * instance. It can be used for setting up the data structures or
	 * initializations needed by this crawler instance.
	 */
	public abstract void onStart();

	/**
	 * This function is called just before the termination of the current
	 * crawler instance. It can be used for persisting in-memory data or other
	 * finalization tasks.
	 */
	public abstract void onBeforeExit();
	
	public void run() {
		onStart();
		while (true) {
			List<CrawlUrl> localQueue = frontier.pull(pullSize);
			if (localQueue.size() == 0) {
				try {
					Thread.sleep(pullRetryDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				for (CrawlUrl cUrl : localQueue) {
					if (cUrl != null) {
						processPage(cUrl);
					}
				}
			}
		}
	}
	
	/**
	 * This function is called once the header of a page is fetched. It can be
	 * overwritten by sub-classes to perform custom logic for different status
	 * codes. For example, 404 pages can be logged, etc.
	 * 
	 * @param crawlUrl
	 * @param statusCode
	 * @param statusDescription
	 */
	protected abstract void handlePageStatusCode(
			CrawlUrl crawlUrl, 
			final int statusCode, 
			final String statusDescription);

	/**
	 * This function is called if the content of a url could not be fetched.
	 * 
	 * @param crawlUrl
	 */
	protected abstract void onContentFetchError(CrawlUrl crawlUrl);

	/**
	 * This function is called if there has been an error in parsing the
	 * content.
	 * 
	 * @param crawlUrl
	 */
	protected abstract void onParseError(CrawlUrl crawlUrl);
	
	/**
	 * Classes that extends WebCrawler can overwrite this function to tell the
	 * crawler whether the given url should be crawled or not. The following
	 * implementation indicates that all urls should be included in the crawl.
	 * 
	 * @param url
	 *            the url which we are interested to know whether it should be
	 *            included in the crawl or not.
	 * @return if the url should be included in the crawl it returns true,
	 *         otherwise false is returned.
	 */
	public boolean shouldVisit(CrawlUrl url) {
		return true;
	}

	/**
	 * Classes that extends WebCrawler can overwrite this function to process
	 * the content of the fetched and parsed page.
	 * 
	 * @param page
	 *            the page object that is just fetched and parsed.
	 */
	public void visit(Page page) {
		// Do nothing by default
		// Sub-classed can override this to add their custom functionality
	}

	private void processPage(CrawlUrl cUrl) {
		String crawlId = cUrl.getCrawlId();
		CrawlInfo crawlInfo = new CrawlInfo();
		crawlInfo.setCrawlStartDate(System.currentTimeMillis());
		
		PageFetchResult fetchResult = null;
		try {
			fetchResult = pageFetcher.fetchHeader(cUrl);
			int statusCode = fetchResult.getStatusCode();
			crawlInfo.setFetchStatus(FetchStatus.getFetchStatus(statusCode));
			handlePageStatusCode(cUrl, statusCode, CustomFetchStatus.getStatusDescription(statusCode));
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					if (crawlConfig.isFollowRedirects()) {
						String movedToUrl = fetchResult.getMovedToUrl();
						if (movedToUrl == null) {
							cUrl.getMetadata().put("aborted", "null redirection"); //FIXME hacky
							addToHistory(cUrl, crawlInfo);
						}						
						
						if (crawlHistory.alreadyVisited(movedToUrl).get(0)) {
							// Redirect page is already seen
							cUrl.getMetadata().put("aborted", "duplicate redirection"); //FIXME hacky
							addToHistory(cUrl, crawlInfo);
							return;
						}

						CrawlUrl redirectUrl = new CrawlUrl(movedToUrl, crawlId);
						redirectUrl.setParentUrl(cUrl.getParentUrl());
						redirectUrl.setDepth(cUrl.getDepth());
						redirectUrl.setAnchor(cUrl.getAnchor());
						if (shouldVisit(redirectUrl) && robotsTxtServer.allows(movedToUrl)) {
							frontier.push(redirectUrl);
						}
					}
				} else if (fetchResult.getStatusCode() == CustomFetchStatus.PageTooBig) {
					logger.info("Skipping a page which was bigger than max allowed size: " + cUrl.getUrl());
				}
				return;
			}

			if (!cUrl.getUrl().equals(fetchResult.getFetchedUrl())) {
				// Server-side redirection happened
				if (crawlHistory.alreadyVisited(fetchResult.getFetchedUrl()).get(0)) {
					// Redirect page is already seen
					cUrl.getMetadata().put("aborted", "duplicate redirection"); //FIXME hacky
					addToHistory(cUrl, crawlInfo);
					return;
				}
				
				cUrl.getMetadata().put("aborted", "server side redirection"); //FIXME hacky
				addToHistory(cUrl, crawlInfo);
				
				cUrl = new CrawlUrl(fetchResult.getFetchedUrl(), cUrl);
			}

			Page page = new Page(cUrl);

			if (!fetchResult.fetchContent(page)) {
				onContentFetchError(cUrl);
				return;
			}

			if (!parser.parse(page, cUrl.getUrl())) {
				onParseError(cUrl);
				return;
			}

			if (Type.html.equals(page.getPayloadType())) {
				
				List<CrawlUrl> frontierOutLinks = new ArrayList<>();
				int maxCrawlDepth = crawlConfig.getMaxDepthOfCrawling();
				for (CrawlUrl outLink : page.getOutgoingUrls()) {
					outLink.setParentUrl(cUrl.getUrl());
					if (crawlHistory.alreadyVisited(outLink.getUrl()).get(0)) {
						outLink.getMetadata().put("aborted", "already seen outlink"); //FIXME hacky
						crawlHistory.add(outLink);
					} else {
						outLink.setDepth((short) (cUrl.getDepth() + 1));
						if (maxCrawlDepth == -1 || cUrl.getDepth() < maxCrawlDepth) {
							if (shouldVisit(outLink) && robotsTxtServer.allows(outLink.getUrl())) {
								frontierOutLinks.add(outLink);
							}
						}
					}
				}
				frontier.push(frontierOutLinks);
			}
			try {
				visit(page);
			} catch (Exception e) {
				logger.error("Exception while running the visit method. Message: '" + e.getMessage() + "' at " + e.getStackTrace()[0]);
			}

		} catch (Exception e) {
			logger.error(e.getMessage() + ", while processing: " + cUrl.getUrl());
		} finally {
			if (fetchResult != null) {
				fetchResult.discardContentIfNotConsumed();
			}
		}
	}
	
	private void addToHistory(CrawlUrl url, CrawlInfo crawlInfo) {
		crawlInfo.setCrawlEndDate(System.currentTimeMillis());
		url.setCrawlInfo(crawlInfo);
		crawlHistory.add(url);
	}

}
