package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "public")

public class User {

	@Id
	@Column(name = "nickname")
	private String nickname;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "avatar")
	private String avatar;

	public User() {
	}

	public User(String name, String nickname, String email, String avatar) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.avatar = avatar;

	}

	public String getName() {
		return this.name;
	}

	public String getNickname() {
		return this.nickname;
	}

	public String getEmail() {
		return this.email;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setName(String value) {
		this.name = value;
	}

	public void setNickname(String value) {
		this.nickname = value;
	}

	public void setEmail(String value) {
		this.email = value;
	}

	public void setAvatar(String value) {
		this.avatar = value;
	}

}