package com.example.oauth.user;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Entity
@Getter
@Setter
public class User {


    @Id
    @GeneratedValue
    Long id;
    String name;
    String username;
    String remark;

    public User() {
    }

    @Builder
    public User(String name, String username, String remark) {
        this.name = name;
        this.username = username;
        this.remark = remark;
    }
}
