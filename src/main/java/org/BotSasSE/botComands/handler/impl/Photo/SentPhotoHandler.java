package org.BotSasSE.botComands.handler.impl.Photo;

import org.BotSasSE.botComands.file.FileDownloader;
import org.BotSasSE.botComands.handler.DbHandler;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.BotSasSE.botComands.enums.ConversationState;
import org.BotSasSE.botComands.handler.UserRequestHandler;
import org.BotSasSE.botComands.helper.KeyboardHelper;
import org.BotSasSE.botComands.model.UserRequest;
import org.BotSasSE.botComands.model.UserSession;
import org.BotSasSE.botComands.service.TelegramService;
import org.BotSasSE.botComands.service.UserSessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

@Slf4j
@Component
public class SentPhotoHandler extends UserRequestHandler {
    @Value("${bot.token}")
    private String botToken;
    private final DbHandler userSearchService;
    private final TelegramService telegramService;
    private final KeyboardHelper keyboardHelper;
    private final UserSessionService userSessionService;

    public SentPhotoHandler(DbHandler userSearchService, TelegramService telegramService, KeyboardHelper keyboardHelper,
                            UserSessionService userSessionService) {
        this.userSearchService = userSearchService;
        this.telegramService = telegramService;
        this.keyboardHelper = keyboardHelper;
        this.userSessionService = userSessionService;
    }

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return isTextMessage(userRequest.getUpdate())
                && ConversationState.WAITING_FOR_PHOTO.equals(userRequest.getUserSession().getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardHelper.buildMenuWithCancel();
        UserSession session = userRequest.getUserSession();
try {
    List<PhotoSize> photos = userRequest.getUpdate().getMessage().getPhoto();
    PhotoSize photo = photos.get(photos.size() - 1);
    String fileId = photo.getFileId();
    GetFile getFileRequest = new GetFile();
    getFileRequest.setFileId(fileId);
    org.telegram.telegrambots.meta.api.objects.File file1 = TelegramService.execute(getFileRequest);
    String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file1.getFilePath();
    String fileName = userSearchService.surnameUserforPhoto(session.getPeselNumber());//добавить имя файла с БД session.getSurnamePhoto()

    FileDownloader.sentFilePhoto(fileUrl,fileName,session.getPhotoYear(),session.getPhotoMonth());
    ReplyKeyboardMarkup replyKeyboardMarkupNew = keyboardHelper.buildMainMenu();
    telegramService.sendMessage(userRequest.getChatId(), "Dzięki, dane zapisane!", replyKeyboardMarkupNew);
    session.setState(ConversationState.CONVERSATION_STARTED);
    userSessionService.saveSession(userRequest.getChatId(), session);

} catch (NullPointerException e) {
    telegramService.sendMessage(userRequest.getChatId(), "Załącz zdjęcie godzinówki!", replyKeyboardMarkup);
    session.setState(ConversationState.WAITING_FOR_PHOTO);
    userSessionService.saveSession(userRequest.getChatId(), session);

}


    }

    @Override
    public boolean isGlobal() {
        return false;
    }

}

