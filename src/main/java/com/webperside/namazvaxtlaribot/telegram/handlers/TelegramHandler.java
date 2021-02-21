package com.webperside.namazvaxtlaribot.telegram.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.webperside.namazvaxtlaribot.dto.MessageDto;
import com.webperside.namazvaxtlaribot.telegram.enums.TelegramCommand;
import com.webperside.namazvaxtlaribot.service.MessageCreatorService;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;
import com.webperside.namazvaxtlaribot.telegram.exceptions.CommandNotFoundException;
import com.webperside.namazvaxtlaribot.util.CommonUtil;
import com.webperside.namazvaxtlaribot.util.Params;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static com.webperside.namazvaxtlaribot.config.Constants.BUTTON_CB_NAV_FIRST_LOAD;

@Service
@RequiredArgsConstructor
public class TelegramHandler {

    // todo blogda bunun haqqda yaz
    
    private final MessageCreatorService messageCreatorService;
    private final TelegramHelper.Executor executor;
    private final HashMap<String, HandlerInterface> endpoints = new HashMap<>();
    private static final String CONSTANT_SUFFIX_HANDLER = "handler";
    private static final String CONSTANT_SUFFIX_CB_HANDLER = "cbhandler";

    public HandlerInterface handle(String key) throws Exception{
        String command = isCommand(key);
        if(!endpoints.containsKey(command)){
            initialize(command);
        }
        return endpoints.get(command);
    }

    private String isCommand(String com){
        if(com.contains("/")){
            return TelegramCommand.getValue(com);
        }
        return com;
    }

    private void initialize(String prefix){
        Class<?>[] classes = TelegramHandler.class.getClasses();

        for(Class<?> c : classes){
            String className = c.getSimpleName().toLowerCase();
            if(className.length() > prefix.length()){
                String classPrefix = className.substring(0, prefix.length());
                String classSuffix = className.substring(prefix.length());
                if(classPrefix.equals(prefix) &&
                        (classSuffix.equals(CONSTANT_SUFFIX_CB_HANDLER) || classSuffix.equals(CONSTANT_SUFFIX_HANDLER))){
                    HandlerInterface foundedHandler = initializeHandler(c);
                    endpoints.put(prefix, foundedHandler);
                    return;
                }
            }
        }

        throw new CommandNotFoundException(String.format("%s command not found", prefix));
    }

    private HandlerInterface initializeHandler(Class<?> myClass)  {
        try{
            return (HandlerInterface) myClass.getConstructors()[0].newInstance(this);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void processRequest(Update update) throws Exception {
        if (update.message() == null && update.callbackQuery() != null) {
            CallbackQuery query = update.callbackQuery();

            Params params = Params.split(query.data());
            String main = params.getMain();
            Map<String, String> values = params.getValues();

            handle(main).run(update, query, values);
        } else {
            String text = update.message().text();

            handle(text).run(update, null, new HashMap<>());

        }
    }

    public class TestHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            Update update = processParams.getUpdate();
            Long chatId = update.message().chat().id();
            MessageDto dto = messageCreatorService.testCreator();
            executor.sendText(chatId, dto);
        }
    }

    public class StartHandler implements HandlerInterface {

        @Override
        public void process(ProcessParams processParams) {
            Update update = processParams.getUpdate();
            Long chatId = update.message().chat().id();

            String alreadyExist = messageCreatorService.userAlreadyExistCreator(chatId);

            if(alreadyExist != null){

                executor.sendText(chatId, alreadyExist);
                return ;
            }

            String from = executor.getUserInfo(update.message().from());
            MessageDto messageDto = messageCreatorService.startCreator(chatId, from);
            executor.sendText(chatId, messageDto);
        }
    }

    public class VaxtlarHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            Update update = processParams.getUpdate();
            Long chatId = update.message().chat().id();
            MessageDto dto = messageCreatorService.prayTimeByUserIdCreator(chatId);
            executor.sendText(chatId, dto);
        }
    }

    /**
     * @implNote Select Source callback Handler
     */
    public class SSCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();
            Long userTgId = query.message().chat().id();
            Integer messageId = query.message().messageId();
            String navigateTo = processParams.getNavigateTo();

            if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                utilProcessSelectSource(userTgId, messageId);
            } else {
                utilProcessSelectSourceNavigate(userTgId, navigateTo, messageId, processParams.getSourcePage());
            }

            executor.answerCallbackQuery(query.id());
        }

        private void utilProcessSelectSource(Long userTgId, Integer messageId) {
            MessageDto dto = messageCreatorService.selectSourceCreator(0);
            executor.editText(userTgId, messageId, dto);
        }

        private void utilProcessSelectSourceNavigate(Long userTgId, String navigateTo, Integer messageId, int page) {
            page = CommonUtil.getPageByValue(navigateTo, page);

            MessageDto dto = messageCreatorService.selectSourceCreator(page);
            executor.editText(userTgId, messageId, dto);
        }
    }

    /**
     * @implNote Select Source Description callback Handler
     */
    public class SSDCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();

            MessageDto dto = messageCreatorService.selectSourceDescriptionCreator(
                    processParams.getSourceId(),
                    processParams.getSourcePage());

            executor.editText(
                    query.message().chat().id(),
                    query.message().messageId(),
                    dto);

            executor.answerCallbackQuery(query.id());
        }
    }

    /**
     * @implNote Select City callback Handler
     */
    public class SCCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();
            Long userTgId = query.message().chat().id();
            Integer messageId = query.message().messageId();
            Integer sourceId = processParams.getSourceId();
            Integer sourcePage = processParams.getSourcePage();
            String navigateTo = processParams.getNavigateTo();

            if (navigateTo.equals(BUTTON_CB_NAV_FIRST_LOAD)) {
                utilProcessSelectCity(userTgId, sourceId, sourcePage, messageId);
            } else {
                utilProcessSelectCityNavigate(userTgId, navigateTo, processParams.getCityPage(), sourceId, sourcePage, messageId);
            }

            executor.answerCallbackQuery(query.id());
        }

        private void utilProcessSelectCity(Long userTgId, Integer sourceId, Integer sourcePage, Integer messageId){
            MessageDto dto = messageCreatorService.selectCityCreator(0,sourceId, sourcePage);
            executor.editText(userTgId, messageId, dto);
        }

        private void utilProcessSelectCityNavigate(Long userTgId, String navigateTo, Integer cityPage, Integer sourceId, Integer sourcePage, Integer messageId){
            cityPage = CommonUtil.getPageByValue(navigateTo, cityPage);

            MessageDto dto = messageCreatorService.selectCityCreator(cityPage, sourceId, sourcePage);
            executor.editText(userTgId, messageId, dto);
        }
    }

    /**
     * @implNote Select City Description callback Handler
     */
    public class SCDCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();

            MessageDto dto = messageCreatorService.selectCityDescriptionCreator(
                    processParams.getCityId(),
                    processParams.getCityPage(),
                    processParams.getSourceId(),
                    processParams.getSourcePage());

            executor.editText(
                    query.message().chat().id(),
                    query.message().messageId(),
                    dto);

            executor.answerCallbackQuery(query.id());
        }
    }

    /**
     * @implNote Select City Settlement Description callback Handler
     */
    public class SCSDCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();

            MessageDto dto = messageCreatorService.selectCitySettlementDescriptionCreator(
                    processParams.getSettlementId(),
                    processParams.getCityId(),
                    processParams.getCityPage(),
                    processParams.getSourceId(),
                    processParams.getSourcePage());

            executor.editText(query.message().chat().id(), query.message().messageId(), dto);
            executor.answerCallbackQuery(query.id());
        }
    }

    /**
     * @implNote Select City Settlement Confirm callback Handler
     */
    public class SCSCCBHandler implements HandlerInterface{

        @Override
        public void process(ProcessParams processParams) {
            CallbackQuery query = processParams.getQuery();
            Long userTgId = query.message().chat().id();
            Integer settlementId = processParams.getSettlementId();

            executor.deleteMessage(userTgId, query.message().messageId());

            String msg = messageCreatorService.selectCitySettlementConfirmCreator(
                    executor.getUserInfo(query.from()),
                    userTgId,
                    settlementId
            );

            executor.sendText(userTgId, msg);

            String waitMessage = messageCreatorService.selectCitySettlementConfirmAfterCreator();

            executor.sendText(userTgId, waitMessage);

            MessageDto dto = messageCreatorService.prayTimeCreator(null, settlementId);

            executor.sendText(userTgId, dto);
        }
    }
}
