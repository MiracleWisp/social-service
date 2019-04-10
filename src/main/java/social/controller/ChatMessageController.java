package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.dto.Response;
import social.entity.Chat;
import social.entity.User;
import social.service.ChatMessageService;
import social.service.ChatService;
import social.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("/chats/{chatId}/messages")
public class ChatMessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    ChatMessageService chatMessageService;

    @GetMapping
    public ResponseEntity<?> getChatMessages(@RequestHeader(value = "username") String username,
                                             @PathVariable("chatId") Integer chatId,
                                             @RequestParam(value ="since", required = false) Long since) {
        User currentUser = userService.findUserByUsername(username);
        Date sinceDate = (since != null) ? new Date(since) : new Date();
        Chat chat = chatService.findChatByChatId(chatId);
        if (!chat.getParticipants().contains(currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not in this chat"));
        }
        return ResponseEntity.status(200).body(new Response(true, chatMessageService.getPreviousMessages(sinceDate, chat)));
    }
}