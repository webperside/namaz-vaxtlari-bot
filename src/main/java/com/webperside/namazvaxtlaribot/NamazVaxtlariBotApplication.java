package com.webperside.namazvaxtlaribot;

import com.gargoylesoftware.htmlunit.html.*;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.models.City;
import com.webperside.namazvaxtlaribot.models.CitySettlement;
import com.webperside.namazvaxtlaribot.models.Settlement;
import com.webperside.namazvaxtlaribot.models.Source;
import com.webperside.namazvaxtlaribot.repository.CityRepository;
import com.webperside.namazvaxtlaribot.repository.CitySettlementRepository;
import com.webperside.namazvaxtlaribot.repository.SettlementRepository;
import com.webperside.namazvaxtlaribot.repository.SourceRepository;
import com.webperside.namazvaxtlaribot.service.WebscrapService;
import com.webperside.namazvaxtlaribot.telegram.TelegramListener;
import com.webperside.namazvaxtlaribot.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.webperside.namazvaxtlaribot.telegram.TelegramConfig.execute;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class NamazVaxtlariBotApplication implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final SettlementRepository settlementRepository;
    private final CitySettlementRepository citySettlementRepository;
    private final WebscrapService webscrapService;
    private final SourceRepository sourceRepository;

    private final TelegramListener listener;

    public static void main(String[] args) {
        SpringApplication.run(NamazVaxtlariBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        saveCities();
//        testMethod();
//        LocalDateTime localDateTime = LocalDateTime.now();
//
//        System.out.println(localDateTime);
//        getCitiesAndSave();
//        getSettlementsAndSave();
//		apiService.get();
		listener.listener();
    }

    public void saveDefaultSettlement(){
        settlementRepository.save(Settlement.builder().name("empty").value("empty").build());
    }

    public void saveCities(){
        Source source = sourceRepository.findById(2).orElse(null);
        Settlement defaultSettlement = settlementRepository.findById(1).orElse(null);
        List<City> cities = new ArrayList<>();

        cities.add(City.builder().name("Ağdam").value("11").source(source).build());
        cities.add(City.builder().name("Astara").value("2").source(source).build());
        cities.add(City.builder().name("Gəncə").value("14").source(source).build());
        cities.add(City.builder().name("Qazax").value("19").source(source).build());
        cities.add(City.builder().name("Quba").value("6").source(source).build());
        cities.add(City.builder().name("Lənkəran").value("3").source(source).build());
        cities.add(City.builder().name("Saatlı").value("7").source(source).build());
        cities.add(City.builder().name("Sabirabad").value("5").source(source).build());
        cities.add(City.builder().name("Şamaxı").value("6").source(source).build());
        cities.add(City.builder().name("Şəki").value("12").source(source).build());
        cities.add(City.builder().name("Xaçmaz").value("10").source(source).build());
        cities.add(City.builder().name("Yevlax").value("11").source(source).build());
        cities.add(City.builder().name("Naxçıvan").value("17").source(source).build());
        cities.add(City.builder().name("Göyçay").value("8").source(source).build());
        cities.add(City.builder().name("Zaqatala").value("14").source(source).build());

        cities = cityRepository.saveAll(cities);

        List<CitySettlement> citySettlements = new ArrayList<>();

        for (City city : cities) {
            citySettlements.add(CitySettlement.builder().settlement(defaultSettlement).city(city).build());
        }

        citySettlementRepository.saveAll(citySettlements);

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
        Source source = sourceRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Source not found"));

        for(City city : cities){
            String params = city.getValue() + Constants.VALUE_SEPARATOR;
            String url = source.getUrl().replace("{REPLACE}",params);

            DomElement element = webscrapService.scrapById(url, Constants.DS_NAMAZ_ZAMANI_NET_SETT_ID);

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
        Source source = sourceRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Source not found"));

        String params = Constants.DS_NAMAZ_ZAMANI_NET_DEFAULT_CITY_VALUE;
        String url = source.getUrl().replace("{REPLACE}", params);

        DomElement element = webscrapService.scrapById(url, Constants.DS_NAMAZ_ZAMANI_NET_CITY_ID);

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
                                .source(Source.builder().id(1).build()) //namazzamani.net
                                .build()
				);
            }

            cityRepository.saveAll(cities);
        }
    }
}
