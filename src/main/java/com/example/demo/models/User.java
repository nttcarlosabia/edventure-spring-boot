package com.example.demo.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users", schema = "public")

@Getter
@Setter
public class User {

	@Id
	private Long id;

	private String nickname;

	private String name;

	private String email;

	private Date loggedDate;

	private String avatar;

	@OneToMany(mappedBy = "userOwner")
	private List<Event> userEvents;

	@ManyToMany(mappedBy = "usersFollowing")
	private List<Event> followingEvents;

	public User() {
	}

	public User(Long id, String nickname, String name, String email, String avatar, Date date) {
		this.id = id;
		this.nickname = nickname;
		this.name = name;
		this.email = email;
		this.avatar = avatar;
		this.loggedDate = date;
		this.userEvents = new ArrayList<Event>();
		this.followingEvents = new ArrayList<Event>();
	}

}