/**
 * 
 */
package com.github.nicosensei.elasticrawler.crawler;

import edu.uci.ics.crawler4j.url.TLDList;

/**
 * @author nicolas
 *
 */
public class ParsedUrl {
	
	private final String url;
	
	private String domain;
	
	private String subDomain;
	
	private String path;
	
	public ParsedUrl(final String url) {
		this.url = url;

		int domainStartIdx = url.indexOf("//") + 2;
		int domainEndIdx = url.indexOf('/', domainStartIdx);
		domain = url.substring(domainStartIdx, domainEndIdx);
		subDomain = "";
		String[] parts = domain.split("\\.");
		if (parts.length > 2) {
			domain = parts[parts.length - 2] + "." + parts[parts.length - 1];
			int limit = 2;
			if (TLDList.getInstance().contains(domain)) { //FIXME inject
				domain = parts[parts.length - 3] + "." + domain;
				limit = 3;
			}
			for (int i = 0; i < parts.length - limit; i++) {
				if (subDomain.length() > 0) {
					subDomain += ".";
				}
				subDomain += parts[i];
			}
		}
		path = url.substring(domainEndIdx);
		int pathEndIdx = path.indexOf('?');
		if (pathEndIdx >= 0) {
			path = path.substring(0, pathEndIdx);
		}
	}

	public String getUrl() {
		return url;
	}

	public String getDomain() {
		return domain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public String getPath() {
		return path;
	}

}
