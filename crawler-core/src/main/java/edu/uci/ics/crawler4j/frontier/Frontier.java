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

package edu.uci.ics.crawler4j.frontier;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Configurable;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */

public abstract class Frontier extends Configurable {

	protected static final Logger logger = LoggerFactory.getLogger(Frontier.class);

	protected final Object mutex = new Object();
	protected final Object waitingList = new Object();

	protected boolean isFinished = false;

	public Frontier(CrawlConfig config) {
		super(config);
	}
	
	public boolean hasBeenDiscovered(String url) {
		return false; //FIXME make this abstract
	}

	public void scheduleAll(List<WebURL> urls) {
		long maxPagesToFetch = config.getMaxPagesToFetch();
		long scheduledPages = getScheduledPagesCount();
		synchronized (mutex) {
			int newScheduledPage = 0;
			for (WebURL url : urls) {
				if (maxPagesToFetch > 0 && (scheduledPages + newScheduledPage) >= maxPagesToFetch) {
					break;
				}
				store(url);
			}			
			synchronized (waitingList) {
				waitingList.notifyAll();
			}
		}
	}
	
	public void schedule(WebURL url) {
		long maxPagesToFetch = config.getMaxPagesToFetch();
		synchronized (mutex) {
			if (maxPagesToFetch < 0 || getScheduledPagesCount() < maxPagesToFetch) {
				store(url);
			}
		}
	}

	public void getNextURLs(int max, List<WebURL> result) {
		while (true) {
			synchronized (mutex) {
				if (isFinished) {
					return;
				}
				List<WebURL> curResults = getNextUrlsToProcess(max);
				result.addAll(curResults);
				if (result.size() > 0) {
					return;
				}
			}
			try {
				synchronized (waitingList) {
					waitingList.wait();
				}
			} catch (InterruptedException ignored) {
				// Do nothing
			}
			if (isFinished) {
				return;
			}
		}
	}

	public abstract void setProcessed(WebURL webURL);

	public boolean isFinished() {
		return isFinished;
	}

	public abstract void close();

	public void finish() {
		isFinished = true;
		synchronized (waitingList) {
			waitingList.notifyAll();
		}
	}
	
	protected abstract long getScheduledPagesCount();
	
	protected abstract void store(WebURL url);
	
	protected abstract List<WebURL> getNextUrlsToProcess(int max);
	
}
