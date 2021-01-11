package com.webperside.namazvaxtlaribot.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.webperside.namazvaxtlaribot.service.WebscrapService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebscrapServiceImpl implements WebscrapService {

    @Override
    public DomElement scrapById(String url, String id) throws IOException {
        try(final WebClient webClient = new WebClient(BrowserVersion.CHROME)){
			webClient.getOptions().setCssEnabled(false);

			final HtmlPage page = webClient.getPage(url);

			return page.getElementById(id);
		}
    }
}
