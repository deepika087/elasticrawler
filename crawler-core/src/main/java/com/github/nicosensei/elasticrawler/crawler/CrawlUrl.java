/**
 * 
 */
package com.github.nicosensei.elasticrawler.crawler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nicolas
 *
 */
public class CrawlUrl {
	
	private final String url;
	
	private final String crawlId;
	
	private String anchor;
	
	private String parentUrl;
	
	private int depth;
	
	private long discoveryDate;
	
	private long crawlStartDate;
	
	private long crawlEndDate;
	
	private Integer httpCode;
	
	private String contentMimeType;
	
	private String contentHash;
	
	private long contentLength;
	
	private CrawlResult result;
	
	/**
	 * A generic map of metadata. Will handle:
	 * <ul>
	 * <li>Crawl information : start/end dates, status, content info @see {@link CrawlInfo}</li>
	 * <li>HTTP headers ?</li>
	 * <li>user tags ?</li>
	 * <li>anything other custom data</li>
	 * </ul>
	 */
	private Map<String, Object> metadata = new HashMap<>(5);
	
	public CrawlUrl(final String url, final String crawlId) {
		this.url = url;
		this.crawlId = crawlId;
		this.discoveryDate = System.currentTimeMillis();
	}

	public CrawlUrl(final String url, final CrawlUrl copyFrom) {
		this(url, copyFrom.getCrawlId());
		setAnchor(copyFrom.getAnchor());
		setDepth(copyFrom.getDepth());
		setMetadata(copyFrom.getMetadata());
		setParentUrl(copyFrom.getParentUrl());
	}
	
	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getUrl() {
		return url;
	}

	public String getCrawlId() {
		return crawlId;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * Returns the anchor string. For example, in <a href="example.com">A sample anchor</a>
	 * the anchor string is 'A sample anchor'
	 */
	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public long getDiscoveryDate() {
		return discoveryDate;
	}

	public void setDiscoveryDate(long discoveryDate) {
		this.discoveryDate = discoveryDate;
	}

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

	public Integer getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
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

	public CrawlResult getResult() {
		return result;
	}

	public void setResult(CrawlResult result) {
		this.result = result;
	}
	
}
