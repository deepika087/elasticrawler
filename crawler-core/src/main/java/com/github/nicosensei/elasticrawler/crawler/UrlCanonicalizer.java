/**
 * 
 */
package com.github.nicosensei.elasticrawler.crawler;

/**
 * Generic URL canonicalizer interface.
 * 
 * @author nicolas
 *
 */
public interface UrlCanonicalizer {
	
	/**
	 * Canonicalizes an absolute URL
	 * @param url the raw URL
	 * @return the canonicalized URL
	 */
	String getCanonicalURL(String url);

}
