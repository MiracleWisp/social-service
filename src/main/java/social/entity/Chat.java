package social.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatId")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer chatId;

    @NotEmpty
    @NotNull
    @Column(name = "chatName")
    private String chatName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private User owner;

    @JsonIgnore
    private String TrackListId;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_CHAT",
            joinColumns = {@JoinColumn(name = "chatId")},
            inverseJoinColumns = {@JoinColumn(name = "username")},
            uniqueConstraints = @UniqueConstraint(name = "UK_participants", columnNames = {"chatId", "username"}))
    private List<User> participants;

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages;
}
