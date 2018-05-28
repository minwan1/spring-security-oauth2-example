package com.example.oauth.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {


    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String remark;

    @Builder
    public User(String name, String username, String remark) {
        this.name = name;
        this.username = username;
        this.remark = remark;
    }
}
