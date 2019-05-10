package social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.dto.Response;
import social.dto.TrackListDTO;
import social.entity.Chat;
import social.entity.User;
import social.feign.MusicClient;
import social.service.ChatService;
import social.service.UserService;

import java.util.ArrayList;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MusicClient musicClient;

    @GetMapping
    public ResponseEntity<?> getUserChats(@RequestHeader(value = "username") String username) {
        User currentUser = userService.findUserByUsername(username);
        return ResponseEntity.ok(new Response(true, currentUser.getChats()));
    }

    @PostMapping
    public ResponseEntity<?> createChat(@RequestBody Chat chat,
                                        @RequestHeader(value = "username") String username) {
        User currentUser = userService.findUserByUsername(username);
        chat.setOwner(currentUser);
        chat.setParticipants(new ArrayList<User>() {{
            add(currentUser);
        }});
        TrackListDTO trackList = musicClient.createTrackList(chat.getChatName());
        musicClient.addUserToTrackList(trackList.getTrackListId(), username);
        chat.setTrackListId(trackList.getTrackListId());
        chat = chatService.saveChat(chat);
        return ResponseEntity.ok(new Response(true, chat));
    }
}