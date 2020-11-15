package com.webperside.namazvaxtlaribot.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webperside.namazvaxtlaribot.dto.DateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.webperside.namazvaxtlaribot.config.Config.dates;

@Service
@Slf4j
public class APIService {

    private static final String URL = "https://ezanvakti.herokuapp.com/vakitler/11631";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void get(){
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                process(response.getBody());
            }
        } catch (JsonProcessingException ex){
            log.error(ex.getMessage());
        }

    }

    //util

    private void process(String response) throws JsonProcessingException {
        JsonNode root = objectMapper.readTree(response);
        for(JsonNode node : root){
            String key = node.get("MiladiTarihKisa").asText();
            DateDto dto = DateDto.builder()
                    .subh(node.get("Imsak").asText())
                    .gunChixir(node.get("GunesDogus").asText())
                    .zohr(node.get("Ogle").asText())
                    .esr(node.get("Ikindi").asText())
                    .axsham(node.get("Aksam").asText())
                    .isha(node.get("Yatsi").asText())
                    .build();
            dates.put(key, dto);
        }
    }

}
