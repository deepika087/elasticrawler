package edu.uci.ics.crawler4j.tests;

import junit.framework.TestCase;

import com.github.nicosensei.elasticrawler.crawler.ParsedUrl;

public class TLDListTest extends TestCase {
	
	public void testTLD() {
		
		ParsedUrl pUrl = new ParsedUrl("http://example.com");
		assertEquals("example.com", pUrl.getDomain());
		assertEquals("", pUrl.getSubDomain());
		
		pUrl = new ParsedUrl("http://test.example.com");
		assertEquals("example.com", pUrl.getDomain());
		assertEquals("test", pUrl.getSubDomain());
		
		pUrl = new ParsedUrl("http://test2.test.example.com");
		assertEquals("example.com", pUrl.getDomain());
		assertEquals("test2.test", pUrl.getSubDomain());
		
		pUrl = new ParsedUrl("http://test3.test2.test.example.com");
		assertEquals("example.com", pUrl.getDomain());
		assertEquals("test3.test2.test", pUrl.getSubDomain());
		
		pUrl = new ParsedUrl("http://www.example.ac.jp");
		assertEquals("example.ac.jp", pUrl.getDomain());
		assertEquals("www", pUrl.getSubDomain());
		
		pUrl = new ParsedUrl("http://example.ac.jp");
		assertEquals("example.ac.jp", pUrl.getDomain());
		assertEquals("", pUrl.getSubDomain());
		
	}
	
}
