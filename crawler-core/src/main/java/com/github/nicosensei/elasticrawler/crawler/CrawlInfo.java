package com.github.nicosensei.elasticrawler.crawler;


/**
 * Crawl information attached to a {@link CrawlUrl} once it has been processed.
 * 
 * @author nicolas
 *
 */
public class CrawlInfo {
	
	private long crawlStartDate;
	
	private long crawlEndDate;
	
	private FetchStatus fetchStatus;
	
	private String contentMimeType;
	
	private String contentHash;
	
	private long contentLength;

	public long getCrawlStartDate() {
		return crawlStartDate;
	}

	public void setCrawlStartDate(long crawlStartDate) {
		this.crawlStartDate = crawlStartDate;
	}

	public long getCrawlEndDate() {
		return crawlEndDate;
	}

	public void setCrawlEndDate(long crawlEndDate) {
		this.crawlEndDate = crawlEndDate;
	}

	public String getContentMimeType() {
		return contentMimeType;
	}

	public void setContentMimeType(String contentMimeType) {
		this.contentMimeType = contentMimeType;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public FetchStatus getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(FetchStatus fetchStatus) {
		this.fetchStatus = fetchStatus;
	}
	
}
