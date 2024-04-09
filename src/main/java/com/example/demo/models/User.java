package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "public")

public class User {

	@Id
	private Long id;

	private String nickname;

	private String name;

	private String email;

	@Lob
	private byte[] avatar;

	@OneToMany(mappedBy = "userOwner")
	private List<Event> userEvents;

	@ManyToMany(mappedBy = "usersFollowing")
	private List<Event> followingEvents;

	public User(Long id, String nickname, String name, String email, byte[] avatar) {
		this.id = id;
		this.nickname = nickname;
		this.name = name;
		this.email = email;
		this.avatar = avatar;
		this.userEvents = new ArrayList<Event>();
		this.followingEvents = new ArrayList<Event>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public List<Event> getUserEvents() {
		return userEvents;
	}

	public void setUserEvents(List<Event> userEvents) {
		this.userEvents = userEvents;
	}

	public List<Event> getFollowingEvents() {
		return followingEvents;
	}

	public void setFollowingEvents(List<Event> followingEvents) {
		this.followingEvents = followingEvents;
	}

}