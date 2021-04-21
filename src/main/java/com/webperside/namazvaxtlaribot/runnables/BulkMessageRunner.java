package com.webperside.namazvaxtlaribot.runnables;

import com.webperside.namazvaxtlaribot.config.Constants;
import com.webperside.namazvaxtlaribot.models.User;
import com.webperside.namazvaxtlaribot.telegram.TelegramHelper;

import java.util.List;

public class BulkMessageRunner implements Runnable{

    private final List<User> users;
    private final String message;
    private final TelegramHelper.Executor executor;


    public BulkMessageRunner(List<User> users, String message, TelegramHelper.Executor executor) {
        this.users = users;
        this.message = message;
        this.executor = executor;
    }

    @Override
    public void run() {
        String alertMessage="";
        if(!users.isEmpty()){
            try{
                for(User user : users){
                    executor.sendText(Long.parseLong(user.getUserTgId()),message);
                }
                alertMessage = Constants.BULK_MESSAGE_SUCCESS;
            } catch (Exception ex){
                alertMessage = String.format(Constants.BULK_MESSAGE_FAIL, ex.getClass().getSimpleName(), ex.getMessage());
            } finally {
                executor.sendText(Constants.ADMIN_TELEGRAM_ID, alertMessage);
            }
        }
    }
}
