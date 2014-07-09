/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"), you may not use this file except in compliance with
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

package com.github.nicosensei.elasticrawler.crawler;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.fetcher.CustomFetchStatus;


/**
 * @author nicolas
 */
public final class FetchStatus {

 	private final Integer httpCode;
	private Integer customCode;
	private final String desc;

	public FetchStatus(
			final Integer httpCode, 
			final Integer customCode,
			final String desc) {
		this.httpCode = httpCode;
		this.customCode = customCode;
		this.desc = desc;
	}

	public Integer getHttpCode() {
		return httpCode;
	}

	public Integer getCustomCode() {
		return customCode;
	}

	public void setCustomCode(Integer customCode) {
		this.customCode = customCode;
	}

	public String getDesc() {
		return desc;
	}
	
	public static FetchStatus getFetchStatus(int code) {
		switch (code) {
		case HttpStatus.SC_OK:
			return new FetchStatus(HttpStatus.SC_OK, null, "OK");
		case HttpStatus.SC_CREATED:
			return new FetchStatus(HttpStatus.SC_CREATED, null, "Created");
		case HttpStatus.SC_ACCEPTED:
			return new FetchStatus(HttpStatus.SC_ACCEPTED, null, "Accepted");
		case HttpStatus.SC_NO_CONTENT:
			return new FetchStatus(HttpStatus.SC_NO_CONTENT, null, "No Content");
		case HttpStatus.SC_MOVED_PERMANENTLY:
			return new FetchStatus(HttpStatus.SC_MOVED_PERMANENTLY, null, "Moved Permanently");
		case HttpStatus.SC_MOVED_TEMPORARILY:
			return new FetchStatus(HttpStatus.SC_MOVED_TEMPORARILY, null, "Moved Temporarily");
		case HttpStatus.SC_NOT_MODIFIED:
			return new FetchStatus(HttpStatus.SC_NOT_MODIFIED, null, "Not Modified");
		case HttpStatus.SC_BAD_REQUEST:
			return new FetchStatus(HttpStatus.SC_BAD_REQUEST, null, "Bad Request");
		case HttpStatus.SC_UNAUTHORIZED:
			return new FetchStatus(HttpStatus.SC_UNAUTHORIZED, null, "Unauthorized");
		case HttpStatus.SC_FORBIDDEN:
			return new FetchStatus(HttpStatus.SC_FORBIDDEN, null, "Forbidden");
		case HttpStatus.SC_NOT_FOUND:
			return new FetchStatus(HttpStatus.SC_NOT_FOUND, null, "Not Found");
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			return new FetchStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, "Internal Server Error");
		case HttpStatus.SC_NOT_IMPLEMENTED:
			return new FetchStatus(HttpStatus.SC_NOT_IMPLEMENTED, null, "Not Implemented");
		case HttpStatus.SC_BAD_GATEWAY:
			return new FetchStatus(HttpStatus.SC_BAD_GATEWAY, null, "Bad Gateway");
		case HttpStatus.SC_SERVICE_UNAVAILABLE:
			return new FetchStatus(HttpStatus.SC_SERVICE_UNAVAILABLE, null, "Service Unavailable");
		case HttpStatus.SC_CONTINUE:
			return new FetchStatus(HttpStatus.SC_CONTINUE, null, "Continue");
		case HttpStatus.SC_TEMPORARY_REDIRECT:
			return new FetchStatus(HttpStatus.SC_TEMPORARY_REDIRECT, null, "Temporary Redirect");
		case HttpStatus.SC_METHOD_NOT_ALLOWED:
			return new FetchStatus(HttpStatus.SC_METHOD_NOT_ALLOWED, null, "Method Not Allowed");
		case HttpStatus.SC_CONFLICT:
			return new FetchStatus(HttpStatus.SC_CONFLICT, null, "Conflict");
		case HttpStatus.SC_PRECONDITION_FAILED:
			return new FetchStatus(HttpStatus.SC_PRECONDITION_FAILED, null, "Precondition Failed");
		case HttpStatus.SC_REQUEST_TOO_LONG:
			return new FetchStatus(HttpStatus.SC_REQUEST_TOO_LONG, null, "Request Too Long");
		case HttpStatus.SC_REQUEST_URI_TOO_LONG:
			return new FetchStatus(HttpStatus.SC_REQUEST_URI_TOO_LONG, null, "Request-URI Too Long");
		case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
			return new FetchStatus(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, null, "Unsupported Media Type");
		case HttpStatus.SC_MULTIPLE_CHOICES:
			return new FetchStatus(HttpStatus.SC_MULTIPLE_CHOICES, null, "Multiple Choices");
		case HttpStatus.SC_SEE_OTHER:
			return new FetchStatus(HttpStatus.SC_SEE_OTHER, null, "See Other");
		case HttpStatus.SC_USE_PROXY:
			return new FetchStatus(HttpStatus.SC_USE_PROXY, null, "Use Proxy");
		case HttpStatus.SC_PAYMENT_REQUIRED:
			return new FetchStatus(HttpStatus.SC_PAYMENT_REQUIRED, null, "Payment Required");
		case HttpStatus.SC_NOT_ACCEPTABLE:
			return new FetchStatus(HttpStatus.SC_NOT_ACCEPTABLE, null, "Not Acceptable");
		case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
			return new FetchStatus(HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED, null, "Proxy Authentication Required");
		case HttpStatus.SC_REQUEST_TIMEOUT:
			return new FetchStatus(HttpStatus.SC_REQUEST_TIMEOUT, null, "Request Timeout");
		case CustomFetchStatus.PageTooBig:
			return new FetchStatus(null, CustomFetchStatus.PageTooBig, "Page size was too big");
		case CustomFetchStatus.FatalTransportError:
			return new FetchStatus(null, CustomFetchStatus.FatalTransportError, "Fatal transport error");
		case CustomFetchStatus.UnknownError:
			return new FetchStatus(null, CustomFetchStatus.UnknownError, "Unknown error");
		default:
			return null;
		}
	}

}
