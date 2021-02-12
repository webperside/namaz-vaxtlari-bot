package com.webperside.namazvaxtlaribot.service;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.models.Source;

import java.io.IOException;

public interface WebscrapService {

    DomElement scrapById(String url, String id) throws IOException;

    PrayTimeDto prepareDataForNamazZamaniNet(String url);

    void prepareDataForAhlibeytAz(Source source);

}
