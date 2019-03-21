package social.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatId")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer chatId;

    @NotEmpty
    @NotNull
    @Column(name = "chatName")
    String chatName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    User owner;

    @JsonIgnore
    @ElementCollection
    @CollectionTable(
            name = "tracks",
            joinColumns = @JoinColumn(name = "chat_id")
    )
    @Column(name = "track")
    List<Integer> playlist = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_CHAT",
            joinColumns = {@JoinColumn(name = "chatId")},
            inverseJoinColumns = {@JoinColumn(name = "username")})
    List<User> participants;

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    List<ChatMessage> messages;
}
