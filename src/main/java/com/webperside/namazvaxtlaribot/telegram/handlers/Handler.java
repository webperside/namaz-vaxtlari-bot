package com.webperside.namazvaxtlaribot.telegram.handlers;

import com.pengrad.telegrambot.model.Update;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import com.webperside.namazvaxtlaribot.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static com.webperside.namazvaxtlaribot.config.Constants.BUTTON_CB_NAV_FIRST_LOAD;

@Service
@RequiredArgsConstructor
public class Handler {

    // todo blogda bunun haqqda yaz
    
    private final MessageCreatorService messageCreatorService;
    private final TelegramHelper helper;
    private final HashMap<String, HandlerInterface> endpoints = new HashMap<>();

    public HandlerInterface get(String key){
        try{
            if(!endpoints.containsKey(key)){
                initializeHandler(key);
            }
            return endpoints.get(key);
        } catch(Exception e){ // exception
            e.printStackTrace();
        }

        return null;
    }

    private void initializeHandler(String prefix) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Class<?>[] classes = Handler.class.getClasses();

        for(Class<?> c : classes){
            String className = c.getSimpleName();
            if(className.toLowerCase().startsWith(prefix)){
                HandlerInterface foundedHandler =(HandlerInterface) c.getConstructors()[0].newInstance(this);
                endpoints.put(prefix, foundedHandler);
                break;
            }
        }
    }

    public class TestHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Update update = (Update) args[0];
            Long chatId = update.message().chat().id();
            MessageDto dto = messageCreatorService.testCreator();
            helper.executor().sendText(chatId, dto);
        }
    }

    public class StartHandler implements HandlerInterface {

        @Override
        public void process(Object... args) {
            Update update = (Update) args[0];
            Long chatId = update.message().chat().id();

            String alreadyExist = messageCreatorService.userAlreadyExistCreator(chatId);

            if(alreadyExist != null){
                helper.executor().sendText(chatId, alreadyExist);
                return ;
            }

            String from = helper.executor().getUserInfo(update.message().from());
            MessageDto messageDto = messageCreatorService.startCreator(chatId, from);
            helper.executor().sendText(chatId, messageDto);
        }
    }

    public class VaxtlarHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Update update = (Update) args[0];
            Long chatId = update.message().chat().id();
            MessageDto dto = messageCreatorService.prayTimeByUserIdCreator(chatId);
            helper.executor().sendText(chatId, dto);
        }
    }

    /**
     * @implNote Select Source callback Handler
     */
    public class SSCBHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Long userTgId = (Long) args[0];
            Integer messageId = (Integer) args[1];
            Integer sourcePage = (Integer) args[2];
            String navigateTo = (String) args[3];

            if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                utilProcessSelectSource(userTgId, messageId);
            } else {
                utilProcessSelectSourceNavigate(userTgId, navigateTo, messageId, sourcePage);
            }
        }

        private void utilProcessSelectSource(Long userTgId, Integer messageId) {
            MessageDto dto = messageCreatorService.selectSourceCreator(0);
            helper.executor().editText(userTgId, messageId, dto);
        }

        private void utilProcessSelectSourceNavigate(Long userTgId, String navigateTo, Integer messageId, int page) {
            page = CommonUtil.getPageByValue(navigateTo, page);

            MessageDto dto = messageCreatorService.selectSourceCreator(page);
            helper.executor().editText(userTgId, messageId, dto);
        }
    }

    /**
     * @implNote Select Source Description callback Handler
     */
    public class SSDCBHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Long userTgId = (Long) args[0];
            Integer messageId = (Integer) args[1];
            Integer sourceId = (Integer) args[2];
            Integer sourcePage = (Integer) args[3];

            MessageDto dto = messageCreatorService.selectSourceDescriptionCreator(sourceId, sourcePage);
            helper.executor().editText(userTgId, messageId, dto);
        }
    }

    /**
     * @implNote Select City callback Handler
     */
    public class SCCBHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Long userTgId = (Long) args[0];
            Integer messageId = (Integer) args[1];
            Integer sourceId = (Integer) args[2];
            Integer sourcePage = (Integer) args[3];
            Integer cityPage = (Integer) args[4];
            String navigateTo = (String) args[5];

            if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                utilProcessSelectCity(userTgId, sourceId, sourcePage, messageId);
            } else {
                utilProcessSelectCityNavigate(userTgId, navigateTo, cityPage, sourceId, sourcePage, messageId);
            }

        }

        private void utilProcessSelectCity(Long userTgId, Integer sourceId, Integer sourcePage, Integer messageId){
            MessageDto dto = messageCreatorService.selectCityCreator(0,sourceId, sourcePage);
            helper.executor().editText(userTgId, messageId, dto);
        }

        private void utilProcessSelectCityNavigate(Long userTgId, String navigateTo, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
            cityPage = CommonUtil.getPageByValue(navigateTo, cityPage);

            MessageDto dto = messageCreatorService.selectCityCreator(cityPage, sourceId, sourcePage);
            helper.executor().editText(userTgId, messageId, dto);
        }
    }

    /**
     * @implNote Select City Description callback Handler
     */
    public class SCDCBHandler implements HandlerInterface{

        @Override
        public void process(Object... args) {
            Long userTgId = (Long) args[0];
            Integer messageId = (Integer) args[1];
            Integer sourceId = (Integer) args[2];
            Integer sourcePage = (Integer) args[3];
            Integer cityId = (Integer) args[4];
            Integer cityPage = (Integer) args[5];

            MessageDto dto = messageCreatorService.selectCityDescriptionCreator(cityId, cityPage, sourceId, sourcePage);
            helper.executor().editText(userTgId, messageId, dto);
        }
    }
}
