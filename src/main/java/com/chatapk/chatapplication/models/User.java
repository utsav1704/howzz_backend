package com.chatapk.chatapplication.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document("user")
public class User {

    @Id
    private String id;
    private String name;
    private String userId;
    private String email;
    private String password;
    private List<String> roles;
    private String imageName;
    @Override
    public String toString() {
        return "User [email=" + email + ", id=" + id + ", imageName=" + imageName + ", name=" + name + ", password="
                + password + ", roles=" + roles + ", userId=" + userId + "]";
    }

}
