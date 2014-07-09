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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class Parser extends Configurable {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Parser.class.getName());
	
	private UrlCanonicalizer urlCanonicalizer;

	private UrlResolver urlResolver;

	public Parser(CrawlConfig config) {
		super(config);
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
			return true;
		}

		// Check content charset if any
		String contentCharset = page.getContentCharset();
		if (contentCharset != null && !Charset.isSupported(contentCharset)) {
			LOGGER.error(
					"Unsupported encoding " + contentCharset, 
					new UnsupportedEncodingException(contentCharset));
			return false;
		}
		
		parseHtml(page, contextURL);
		
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
	
	protected abstract void parseHtml(Page page, String contextURL);

}
