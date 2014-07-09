/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nicosensei.elasticrawler.crawler.CrawlUrl;
import com.github.nicosensei.elasticrawler.crawler.UrlCanonicalizer;
import com.github.nicosensei.elasticrawler.crawler.UrlResolver;

import edu.uci.ics.crawler4j.crawler.Configurable;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.Page.Type;
import edu.uci.ics.crawler4j.util.Util;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class Parser extends Configurable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Parser.class.getName());

	private HtmlParser htmlParser;
	private ParseContext parseContext;
	
	private UrlCanonicalizer urlCanonicalizer;

	private UrlResolver urlResolver;

	public Parser(CrawlConfig config) {
		super(config);
		htmlParser = new HtmlParser();
		parseContext = new ParseContext();
	}

	public boolean parse(Page page, String contextURL) {

		if (Util.hasBinaryContent(page.getContentType())) {
			if (!config.isIncludeBinaryContentInCrawling()) {
				return false;
			}

			page.setDataType(Type.binary);
			return true;

		} else if (Util.hasPlainTextContent(page.getContentType())) {
			page.setDataType(Type.text);
		}

		Metadata metadata = new Metadata();
		HtmlContentHandler contentHandler = new HtmlContentHandler();
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(page.getContentData());
			htmlParser.parse(inputStream, contentHandler, metadata, parseContext);
		} catch (Exception e) {
			LOGGER.error(e.getMessage() + ", while parsing: " + page.getCrawlUrl().getUrl());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage() + ", while parsing: " + page.getCrawlUrl().getUrl());
			}
		}

		if (page.getContentCharset() == null) {
			page.setContentCharset(metadata.get("Content-Encoding"));
		}

		page.setTitle(metadata.get(DublinCore.TITLE));

		List<CrawlUrl> outgoingUrls = new ArrayList<>();

		String baseURL = contentHandler.getBaseUrl();
		if (baseURL != null) {
			contextURL = baseURL;
		}

		int urlCount = 0;
		for (ExtractedUrlAnchorPair urlAnchorPair : contentHandler.getOutgoingUrls()) {
			String href = urlAnchorPair.getHref();
			href = href.trim();
			if (href.length() == 0) {
				continue;
			}
			String hrefWithoutProtocol = href.toLowerCase();
			if (href.startsWith("http://")) {
				hrefWithoutProtocol = href.substring(7);
			}
			if (!hrefWithoutProtocol.contains("javascript:") && !hrefWithoutProtocol.contains("mailto:")
					&& !hrefWithoutProtocol.contains("@")) {
				String url = urlCanonicalizer.canonicalize(
						urlResolver.resolve(contextURL, href));
				if (url != null) {
					CrawlUrl cUrl = new CrawlUrl(url, ""); //FIXME
					cUrl.setAnchor(urlAnchorPair.getAnchor());
					outgoingUrls.add(cUrl);
					urlCount++;
					if (urlCount > config.getMaxOutgoingLinksToFollow()) {
						break;
					}
				}
			}
		}

		page.setOutgoingUrls(outgoingUrls);

		String contentCharset = page.getContentCharset();
		if (contentCharset != null && !Charset.isSupported(contentCharset)) {
			LOGGER.error(
					"Unsupported encoding " + contentCharset, 
					new UnsupportedEncodingException(contentCharset));
			return false;
		}
		
		return true;

	}

	public UrlCanonicalizer getUrlCanonicalizer() {
		return urlCanonicalizer;
	}

	public void setUrlCanonicalizer(UrlCanonicalizer urlCanonicalizer) {
		this.urlCanonicalizer = urlCanonicalizer;
	}

	public UrlResolver getUrlResolver() {
		return urlResolver;
	}

	public void setUrlResolver(UrlResolver urlResolver) {
		this.urlResolver = urlResolver;
	}

}
