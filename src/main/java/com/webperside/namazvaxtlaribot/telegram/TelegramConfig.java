package com.webperside.namazvaxtlaribot.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUserProfilePhotos;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.webperside.namazvaxtlaribot.dto.view.UserDto;

import java.util.Base64;

public class TelegramConfig {

    private static final String TOKEN = "MTQ0NDkwMjEzNjpBQUZxaF90Z3dvMl8zdG5MbGFxSXh3TjZGYlBKemVmbnFLVQ==";
    private static TelegramBot bot;

    public static TelegramBot getInstance(){
        if(bot != null) return bot;

        bot = new TelegramBot(new String(Base64.getDecoder().decode(TOKEN)));
        return bot;
    }

    public static <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(T request){
        return getInstance().execute(request);
    }

    public static UserDto getUserInfoDetail(long userTgId){
        GetChatResponse chatResponse = execute(new GetChat(userTgId));
        Chat chat = chatResponse.chat();
        String photoPath = "/images/user-no-photo.jpg";

        if(chat.photo() != null){
            String fileId = chat.photo().bigFileId();
            GetFileResponse fileResponse = execute(new GetFile(fileId));
            photoPath = getInstance().getFullFilePath(fileResponse.file());
        }

        return UserDto.builder()
                .firstName(chat.firstName())
                .lastName(chat.lastName())
                .username(chat.username() != null ? "@"+ chat.username() : "no-username")
                .photoUrl(photoPath)
                .userTelegramId(String.valueOf(chat.id()))
                .build();
    }

}
