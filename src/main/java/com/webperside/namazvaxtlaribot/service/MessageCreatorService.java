package com.webperside.namazvaxtlaribot.service;

import com.webperside.namazvaxtlaribot.dto.MessageDto;

public interface MessageCreatorService {

    MessageDto testCreator();

    MessageDto startCreator(String from);

    MessageDto selectSourceCreator(Integer sourcePage);

    MessageDto selectSourceDescriptionCreator(Integer sourceId, Integer sourcePage);

    MessageDto selectCityCreator(Integer cityPage, Integer sourceId, Integer sourcePage);

    MessageDto selectCityDescriptionCreator(Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage);

    MessageDto selectCitySettlementDescriptionCreator(Integer citySettlementId, Integer cityId, Integer cityPage, Integer sourceId, Integer sourcePage);

    String selectCitySettlementConfirmCreator(String from, long userTgId, Integer citySettlementId);
}
