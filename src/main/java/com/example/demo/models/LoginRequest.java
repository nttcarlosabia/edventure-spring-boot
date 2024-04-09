package com.example.demo.models;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    String email;
    boolean email_verified;
    String family_name;
    String given_name;
    String locale;
    String name;
    String nickname;
    String picture;
    String sub;
    Date updated_at;

}
