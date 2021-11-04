package com.webperside.namazvaxtlaribot.service.impl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.webperside.namazvaxtlaribot.dto.PrayTimeDto;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.service.WebScrapService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.webperside.namazvaxtlaribot.config.Constants.ahlibeytAzTimes;

@Service
public class WebScrapServiceImpl implements WebScrapService {

    @Override
    public HtmlPage scrap(String url) throws IOException {
        try(final WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            return webClient.getPage(url);
        }
    }

    @Override
    public DomElement scrapById(String url, String id) throws IOException {
        final HtmlPage page = scrap(url);
        return page.getElementById(id);
    }

    @Override
    public PrayTimeDto prepareDataForNamazZamaniNet(String url) {
        String htmlId = "timeScale";
        String htmlTag = "ul";
        try {
            DomElement dom = scrapById(url, htmlId);
            if(dom instanceof HtmlDivision){
                HtmlDivision div = (HtmlDivision) dom;

                DomNodeList<HtmlElement> list = div.getElementsByTagName(htmlTag);

                PrayTimeDto prayTime = new PrayTimeDto();

                for(HtmlElement element : list){
                    DomElement d = element.getLastElementChild();

                    if(d instanceof HtmlListItem){
                        HtmlListItem li = (HtmlListItem) d;
                        prayTime.addForNamazZamaniNet(li.getId(), li.asText());
                    }

                }

                return prayTime;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void prepareDataForAhlibeytAz(Source source) {
        try {
            String htmlId = "table-center";
            String htmlXpath = "//table[@class='namaz']";
            DomElement element = scrapById(source.getUrl(), htmlId);

            List<DomElement> element2 = element.getByXPath(htmlXpath);

            HtmlTable table = (HtmlTable) element2.get(0);
            List<HtmlTableRow> rows = table.getRows();

            for (int i = 1; i < rows.size(); i++) {
                HtmlTableRow row = rows.get(i);
                List<HtmlTableCell> cells = row.getCells();

                PrayTimeDto ptd = new PrayTimeDto();

                for (int j = 1; j < cells.size(); j++) {
                    HtmlTableCell cell = cells.get(j);
                    String value = cell.asText().replace(";", ":");
                    ptd.addByIndex(j, value);
                }

                ahlibeytAzTimes.put(i, ptd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrayTimeDto prepareDataForMetbuatAz(String url) {
        try{
            final String htmlXpath = "//table[@class='table namaz-daily-table']";
            HtmlPage page = scrap(url);
            PrayTimeDto prayTime = new PrayTimeDto();

            List<DomElement> element2 = page.getByXPath(htmlXpath);

            HtmlTable table = (HtmlTable) element2.get(0);
            HtmlTableRow row = table.getRows().get(1);
            List<HtmlTableCell> cells = row.getCells();

            for(int i = 0 ; i < cells.size() ; i++){
                prayTime.addByIndex(i + 1, cells.get(i).asText());
            }

            System.out.println(prayTime);

            return prayTime;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
