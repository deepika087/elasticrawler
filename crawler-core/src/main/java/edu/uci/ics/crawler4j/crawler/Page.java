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

package edu.uci.ics.crawler4j.crawler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.github.nicosensei.elasticrawler.crawler.CrawlUrl;

/**
 * This class contains the data for a fetched and parsed page.
 *
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class Page {
	
	public static enum Type {
		html,
		text,
		binary
	}

    /**
     * The URL of this page.
     */
    protected CrawlUrl url;

    /**
     * The content of this page in binary format.
     */
    protected byte[] contentData;

    /**
     * The ContentType of this page.
     * For example: "text/html; charset=UTF-8"
     */
    protected String contentType;

    /**
     * The encoding of the content.
     * For example: "gzip"
     */
    protected String contentEncoding;

    /**
     * The charset of the content.
     * For example: "UTF-8"
     */
    protected String contentCharset;
    
    /**
     * Headers which were present in the response of the
     * fetch request
     */
    protected Header[] fetchResponseHeaders;

    /**
     * The type of data
     */
    protected Type dataType;
    
    /**
     * Only available for HTML type
     */
    protected String title;
    
    protected List<CrawlUrl> outgoingUrls;

	public Page(CrawlUrl url) {
		this.url = url;
	}

	public CrawlUrl getCrawlUrl() {
		return url;
	}

	public void setCrawlUrl(CrawlUrl url) {
		this.url = url;
	}

    /**
     * Loads the content of this page from a fetched
     * HttpEntity.
     */
	public void load(HttpEntity entity) throws Exception {

		contentType = null;
		Header type = entity.getContentType();
		if (type != null) {
			contentType = type.getValue();
		}

		contentEncoding = null;
		Header encoding = entity.getContentEncoding();
		if (encoding != null) {
			contentEncoding = encoding.getValue();
		}

		Charset charset = ContentType.getOrDefault(entity).getCharset();
		if (charset != null) {
			contentCharset = charset.displayName();	
		}

		contentData = EntityUtils.toByteArray(entity);
	}
	
	/**
     * Returns headers which were present in the response of the
     * fetch request
     */
	public Header[] getFetchResponseHeaders() {
		return fetchResponseHeaders;
	}
	
	public void setFetchResponseHeaders(Header[] headers) {
		fetchResponseHeaders = headers;
	}

    public Type getPayloadType() {
		return dataType;
	}

	public void setDataType(Type dataType) {
		this.dataType = dataType;
	}

	public String getContentAsText() throws UnsupportedEncodingException {
		switch (dataType) {
			case text:
			case html:
				if (contentCharset == null) {
					return new String(contentData);
				} else {
					return new String(contentData, contentCharset);
				}
			default: 
				return null;
		}		
	}
	
	/**
     * Returns the content of this page in binary format.
     */
	public byte[] getContentData() {
		return contentData;
	}

	public void setContentData(byte[] contentData) {
		this.contentData = contentData;
	}

    /**
     * Returns the ContentType of this page.
     * For example: "text/html; charset=UTF-8"
     */
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

    /**
     * Returns the encoding of the content.
     * For example: "gzip"
     */
    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    /**
     * Returns the charset of the content.
     * For example: "UTF-8"
     */
	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<CrawlUrl> getOutgoingUrls() {
		return outgoingUrls;
	}

	public void setOutgoingUrls(List<CrawlUrl> outgoingUrls) {
		this.outgoingUrls = outgoingUrls;
	}

}
