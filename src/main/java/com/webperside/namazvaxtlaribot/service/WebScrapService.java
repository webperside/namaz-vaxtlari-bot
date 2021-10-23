package com.webperside.namazvaxtlaribot.service;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.models.Source;

import java.io.IOException;

public interface WebScrapService {

    HtmlPage scrap(String url) throws IOException;

    DomElement scrapById(String url, String id) throws IOException;

    PrayTimeDto prepareDataForNamazZamaniNet(String url);

    void prepareDataForAhlibeytAz(Source source);

    PrayTimeDto prepareDataForMetbuatAz(String url);



}
