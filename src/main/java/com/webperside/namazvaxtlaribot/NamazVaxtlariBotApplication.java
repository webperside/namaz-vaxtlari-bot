package com.webperside.namazvaxtlaribot;

import com.gargoylesoftware.htmlunit.html.*;
import com.webperside.namazvaxtlaribot.config.Config;
import com.webperside.namazvaxtlaribot.external.APIService;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.CitySettlement;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.repository.CityRepository;
import com.webperside.namazvaxtlaribot.repository.CitySettlementRepository;
import com.webperside.namazvaxtlaribot.repository.SettlementRepository;
import com.webperside.namazvaxtlaribot.service.WebscrapService;
import com.webperside.namazvaxtlaribot.telegram.TelegramListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class NamazVaxtlariBotApplication implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final SettlementRepository settlementRepository;
    private final CitySettlementRepository citySettlementRepository;
    private final WebscrapService webscrapService;

    public static void main(String[] args) {
        SpringApplication.run(NamazVaxtlariBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        testMethod();
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        System.out.println(localDateTime);
//        getSettlementsAndSave();
//        getCitiesAndSave();
        //		testMethod();
//		apiService.get();
//		telegramListener.listener();
    }

    public void testMethod() throws IOException{
        String url = "http://ahlibeyt.az/namazan";
        DomElement element = webscrapService.scrapById(url, "table-center");

//        System.out.println(element.asText());


        List<DomElement> element2 = element.getByXPath("//table[@class='namaz']");

        HtmlTable table = (HtmlTable) element2.get(0);

        HtmlTableBody body = table.getBodies().get(0);

        body.getChildElements().forEach(domElement -> {
            HtmlTableRow row = (HtmlTableRow) domElement;
            row.getCells().forEach(cell -> {
                System.out.println(cell.asText());
            });
//            System.out.println(cell.asText());
        });

//        System.out.println(body.getDefaultStyleDisplay().value());

//        System.out.println(table.asText());
    }

    public void getSettlementsAndSave() throws IOException{
    	List<City> cities = cityRepository.findAll();
        Settlement defaultSettlement = settlementRepository.findById(1).orElse(null);

        for(City city : cities){
            String params = city.getValue() + Config.VALUE_SEPARATOR;
            String url = Config.URL_DS_NAMAZ_ZAMANI_NET.replace("{REPLACE}",params);

            DomElement element = webscrapService.scrapById(url, Config.DS_NAMAZ_ZAMANI_NET_SETT_ID);

            if(element instanceof HtmlSelect){
                HtmlSelect selectElement = (HtmlSelect) element;

                List<HtmlOption> options = selectElement.getOptions();
                List<Settlement> settlements = new ArrayList<>();

                if(options.size() == 2){
                    citySettlementRepository.save(
                            CitySettlement.builder()
                                    .city(city)
                                    .settlement(defaultSettlement)
                                    .build()
                    );
                    continue;
                }

                for(HtmlOption option : options){
                    String name = option.getText();
                    String value = option.getValueAttribute();

                    settlements.add(
                            Settlement.builder()
                                    .name(name)
                                    .value(value)
                                    .build()
                    );
                }

                settlementRepository.saveAll(settlements);

                List<CitySettlement> citySettlements = new ArrayList<>();

                for(Settlement settlement : settlements){
                    citySettlements.add(
                            CitySettlement.builder()
                                    .city(city)
                                    .settlement(settlement)
                                    .build()
                    );
                }

                citySettlementRepository.saveAll(citySettlements);
            }
        }
    	
	}

    public void getCitiesAndSave() throws IOException {
        String params = Config.DS_NAMAZ_ZAMANI_NET_DEFAULT_CITY_VALUE;
        String url = Config.URL_DS_NAMAZ_ZAMANI_NET.replace("{REPLACE}", params);

        DomElement element = webscrapService.scrapById(url, Config.DS_NAMAZ_ZAMANI_NET_CITY_ID);

        if (element instanceof HtmlSelect) {
            HtmlSelect selectElement = (HtmlSelect) element;

            List<HtmlOption> options = selectElement.getOptions();
            List<City> cities = new ArrayList<>();

            for (HtmlOption option : options) {
                String name = option.getText();
                String value = option.getValueAttribute();

                cities.add(
                        City.builder()
                                .name(name)
                                .value(value)
                                .build()
				);
            }

            cityRepository.saveAll(cities);
        }
    }
}
