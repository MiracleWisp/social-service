package social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social.entity.Chat;
import social.repository.ChatRepository;

@Service("chatService")
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;


    public Chat findChatByChatId(Integer chatId) {
        return chatRepository.findByChatId(chatId);
    }

    public Chat saveChat(Chat chat) {
        return chatRepository.save(chat);
    }

}

