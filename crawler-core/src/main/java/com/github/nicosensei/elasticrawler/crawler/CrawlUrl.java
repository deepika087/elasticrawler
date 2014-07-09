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
	
	public static final String KEY_CRAWL_INFO = "crawlinfo";

	private final String url;
	
	private final String crawlId;
	
	private final long dateDiscovered;
	
	private String anchor;
	
	private String parentUrl;
	
	private int depth;
	
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
		this.dateDiscovered = System.currentTimeMillis();
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

	public long getDateDiscovered() {
		return dateDiscovered;
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
	
	public void setCrawlInfo(CrawlInfo crawlInfo) {
		metadata.put(KEY_CRAWL_INFO, crawlInfo);
	}
	
}
