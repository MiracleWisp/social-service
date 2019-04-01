package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import social.entity.Chat;
import social.entity.ChatMessage;
import social.entity.User;
import social.service.ChatMessageService;
import social.service.ChatService;
import social.service.UserService;

import java.util.Date;

@Controller
public class ChatMessageController {

    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @MessageMapping("/chat/{chatId}/sendMessage")
    @SendTo("/topic/{chatId}")
    public String sendMessage(@DestinationVariable Integer chatId, @Payload String textMessage, SimpMessageHeaderAccessor accessor) {
        Chat chat = chatService.findChatByChatId(chatId);
        System.out.println(accessor.getUser().getName());
        User user = userService.findUserByUsername(accessor.getUser().getName());
        ChatMessage chatMessage = ChatMessage.builder()
                .chat(chat)
                .content(textMessage)
                .sender(user)
                .type(ChatMessage.MessageType.CHAT)
                .sendTime(new Date()).build();
        chatMessageService.saveChatMessage(chatMessage);
        return textMessage;
    }

    /*@MessageMapping("/chat/{chatId}/addUser")
    @SendTo("/topic/{chatId}")
    public String addUser(@DestinationVariable String chatId, @Payload String chatMessage,
                          SimpMessageHeaderAccessor headerAccessor) {
        return chatMessage;
    }*/
}