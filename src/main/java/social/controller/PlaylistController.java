package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.dto.Response;
import social.dto.TrackDTO;
import social.entity.Chat;
import social.entity.User;
import social.service.ChatService;
import social.service.UserService;

@RestController
@RequestMapping("/chats/{chatId}/playlist")
@EnableAutoConfiguration
public class PlaylistController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<?> getChatPlaylist(@PathVariable("chatId") Integer chatId,
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
        return ResponseEntity.ok(new Response(true, chat.getPlaylist()));
    }

    @PostMapping
    public ResponseEntity<?> addTrackToChatPlaylist(@RequestBody TrackDTO track,
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
        if (chat.getPlaylist().contains(track.getTrackId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Response(false, "Track already exist in playlist"));
        }

        chat.getPlaylist().add(track.getTrackId());
        chat = chatService.saveChat(chat);
        return ResponseEntity.ok(new Response(true, chat.getPlaylist()));
    }

    @DeleteMapping
    public ResponseEntity<?> removeTrackFromChatPlaylist(@RequestBody TrackDTO track,
                                                    @PathVariable("chatId") Integer chatId,
                                                    @RequestHeader(value = "username") String username) {
        Chat chat = chatService.findChatByChatId(chatId);
        if (chat == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, "Chat does not exist"));
        }
        User currentUser = userService.findUserByUsername(username);
        if (!chat.getOwner().equals(currentUser)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(false, "You are not chat owner"));
        }
        if (!chat.getPlaylist().contains(track.getTrackId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new Response(false, "This track is not in the playlist."));
        }
        chat.getPlaylist().remove(track.getTrackId());
        chat = chatService.saveChat(chat);
        return ResponseEntity.ok(new Response(true, chat.getPlaylist()));
    }
}
