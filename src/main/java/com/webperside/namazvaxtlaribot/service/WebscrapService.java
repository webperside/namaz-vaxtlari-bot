package com.webperside.namazvaxtlaribot.service;

import com.gargoylesoftware.htmlunit.html.DomElement;

import java.io.IOException;

public interface WebscrapService {

    DomElement scrapById(String url, String id) throws IOException;

}
