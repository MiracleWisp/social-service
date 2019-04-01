package social.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @NotNull
    @NotEmpty
    @Column(name = "username")
    private String username;


    @JsonIgnore
    @ManyToMany(mappedBy = "participants")
    private List<Chat> chats;


}