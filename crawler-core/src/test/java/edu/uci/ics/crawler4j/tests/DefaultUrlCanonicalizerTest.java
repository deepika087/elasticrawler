package edu.uci.ics.crawler4j.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.uci.ics.crawler4j.url.DefaultUrlCanonicalizer;
import edu.uci.ics.crawler4j.url.DefaultUrlResolver;

public class DefaultUrlCanonicalizerTest {
	
	private static final DefaultUrlCanonicalizer canonicalizer = new DefaultUrlCanonicalizer();
	
	private static final DefaultUrlResolver resolver = new DefaultUrlResolver();

	@Test
	public void testCanonizalier() {

		assertEquals("http://www.example.com/display?category=foo%2Fbar%2Bbaz",
				canonicalizer.canonicalize("http://www.example.com/display?category=foo/bar+baz"));

		assertEquals("http://www.example.com/?q=a%2Bb",
				canonicalizer.canonicalize("http://www.example.com/?q=a+b"));

		assertEquals("http://www.example.com/display?category=foo%2Fbar%2Bbaz",
				canonicalizer.canonicalize("http://www.example.com/display?category=foo%2Fbar%2Bbaz"));

		assertEquals("http://somedomain.com/uploads/1/0/2/5/10259653/6199347.jpg?1325154037",
				canonicalizer.canonicalize("http://somedomain.com/uploads/1/0/2/5/10259653/6199347.jpg?1325154037"));

		assertEquals("http://hostname.com/", canonicalizer.canonicalize("http://hostname.com"));

		assertEquals("http://hostname.com/", canonicalizer.canonicalize("http://HOSTNAME.com"));

		assertEquals("http://www.example.com/index.html",
				canonicalizer.canonicalize("http://www.example.com/index.html?&"));

		assertEquals("http://www.example.com/index.html",
				canonicalizer.canonicalize("http://www.example.com/index.html?"));

		assertEquals("http://www.example.com/", canonicalizer.canonicalize("http://www.example.com"));

		assertEquals("http://www.example.com/bar.html",
				canonicalizer.canonicalize("http://www.example.com:80/bar.html"));

		assertEquals("http://www.example.com/index.html?name=test&rame=base",
				canonicalizer.canonicalize("http://www.example.com/index.html?name=test&rame=base#123"));

		assertEquals("http://www.example.com/~username/",
				canonicalizer.canonicalize("http://www.example.com/%7Eusername/"));

		assertEquals("http://www.example.com/A/B/index.html",
				canonicalizer.canonicalize("http://www.example.com//A//B/index.html"));

		assertEquals("http://www.example.com/index.html?x=y",
				canonicalizer.canonicalize("http://www.example.com/index.html?&x=y"));

		assertEquals("http://www.example.com/a.html",
				canonicalizer.canonicalize("http://www.example.com/../../a.html"));

		assertEquals("http://www.example.com/a/c/d.html",
				canonicalizer.canonicalize("http://www.example.com/../a/b/../c/./d.html"));

		assertEquals("http://foo.bar.com/?baz=1", canonicalizer.canonicalize("http://foo.bar.com?baz=1"));

		assertEquals("http://www.example.com/index.html?a=b&c=d&e=f",
				canonicalizer.canonicalize("http://www.example.com/index.html?&c=d&e=f&a=b"));

		assertEquals("http://www.example.com/index.html?q=a%20b",
				canonicalizer.canonicalize("http://www.example.com/index.html?q=a b"));

		assertEquals("http://www.example.com/search?height=100%&width=100%",
				canonicalizer.canonicalize("http://www.example.com/search?width=100%&height=100%"));

		assertEquals("http://foo.bar/mydir/myfile?page=2",
				canonicalizer.canonicalize(resolver.resolve("http://foo.bar/mydir/myfile", "?page=2")));

	}
}
