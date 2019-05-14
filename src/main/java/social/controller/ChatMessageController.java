package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import social.dto.Response;
import social.entity.Chat;
import social.entity.ChatMessage;
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

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping
    public ResponseEntity<?> getChatMessages(@RequestHeader(value = "username") String username,
                                             @PathVariable("chatId") Integer chatId,
                                             @RequestParam(value = "since", required = false) Long since) {
        User currentUser = userService.findUserByUsername(username);
        Date sinceDate = (since != null) ? new Date(since) : new Date();
        Chat chat = chatService.findChatByChatId(chatId);
        if (!chat.getParticipants().contains(currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not in this chat"));
        }
        return ResponseEntity.status(200).body(new Response(true,
                chatMessageService.getPreviousMessages(sinceDate, chat)));
    }

    @PostMapping
    public ResponseEntity<?> addTrackMessage(@RequestHeader(value = "username") String username,
                                             @RequestParam(value = "track_list_id") String trackListId,
                                             @RequestParam(value = "track_name") String trackName) {
        Chat chat = chatService.findByTrackListId(trackListId);
        User user = userService.findUserByUsername(username);
        ChatMessage chatMessage = new ChatMessage(user, username + " добавил(а) песню " + trackName,
                chat, ChatMessage.MessageType.ADD_TRACK);
        chatMessage = chatMessageService.saveChatMessage(chatMessage);
        template.convertAndSend("/topic/" + chat.getChatId(), chatMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, chatMessage));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTrackMessage(@RequestHeader(value = "username") String username,
                                                @RequestParam(value = "track_list_id") String trackListId,
                                                @RequestParam(value = "track_name") String trackName) {
        Chat chat = chatService.findByTrackListId(trackListId);
        User user = userService.findUserByUsername(username);
        ChatMessage chatMessage = new ChatMessage(user, username + " удалил(а) песню " + trackName,
                chat, ChatMessage.MessageType.DELETE_TRACK);
        chatMessage = chatMessageService.saveChatMessage(chatMessage);
        template.convertAndSend("/topic/" + chat.getChatId(), chatMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(true, chatMessage));
    }
}