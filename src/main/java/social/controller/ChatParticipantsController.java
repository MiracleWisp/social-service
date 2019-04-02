package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

@RestController
@RequestMapping("/chats/{chatId}/participants")
@EnableAutoConfiguration
public class ChatParticipantsController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private SimpMessagingTemplate template;

    @GetMapping
    public ResponseEntity<?> getChatParticipants(@PathVariable("chatId") Integer chatId,
                                                 @RequestHeader(value = "username") String username) {
        Chat chat = chatService.findChatByChatId(chatId);
        if (chat == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Chat does not exist"));
        }
        User currentUser = userService.findUserByUsername(username);
        if (!chat.getParticipants().contains(currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not in this chat"));
        }
        return ResponseEntity.ok(new Response(true, chat.getParticipants()));
    }

    @PostMapping
    public ResponseEntity<?> addUserToChat(@RequestBody User user,
                                           @PathVariable("chatId") Integer chatId,
                                           @RequestHeader(value = "username") String username) {
        Chat chat = chatService.findChatByChatId(chatId);
        if (chat == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Chat does not exist"));
        }
        User currentUser = userService.findUserByUsername(username);
        if (!chat.getParticipants().contains(currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not in this chat"));
        }
        User addUser = userService.findUserByUsername(user.getUsername());
        if (addUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "User not found"));
        }
        chat.getParticipants().add(addUser);
        chat = chatService.saveChat(chat);
        String inviteString = currentUser.getUsername() + " добавил(а) " + addUser.getUsername() + " в чат";
        ChatMessage chatMessage = chatMessageService.saveChatMessage(
                new ChatMessage(currentUser, inviteString, chat, ChatMessage.MessageType.JOIN));

        template.convertAndSend("/topic/" + chatId, chatMessage);

        return ResponseEntity.ok(new Response(true, chat.getParticipants()));
    }

    @DeleteMapping
    public ResponseEntity<?> removeUserFromChat(@RequestBody User user,
                                                @PathVariable("chatId") Integer chatId,
                                                @RequestHeader(value = "username") String username) {
        Chat chat = chatService.findChatByChatId(chatId);
        if (chat == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Chat does not exist"));
        }

        User currentUser = userService.findUserByUsername(username);
        User removeUser = userService.findUserByUsername(user.getUsername());

        if (!(chat.getOwner().equals(currentUser) || currentUser.equals(removeUser))) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not chat owner"));
        }

        if (removeUser == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "User not found"));
        }

        if (!chat.getParticipants().contains(removeUser)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "User not in this chat"));
        }
        chat.getParticipants().remove(removeUser);
        chat = chatService.saveChat(chat);

        String removeString = currentUser.equals(removeUser) ?
                currentUser.getUsername() + " покинул(а) чат " :
                currentUser.getUsername() + " удалил(а) " + removeUser.getUsername() + " из чата";
        ChatMessage chatMessage = chatMessageService.saveChatMessage(
                new ChatMessage(currentUser, removeString, chat, ChatMessage.MessageType.LEAVE));
        template.convertAndSend("/topic/" + chatId, chatMessage);

        return ResponseEntity.ok(new Response(true, chat.getParticipants()));
    }
}
