package com.example.demo.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
	private String lastname;
	private Boolean showEmail;
	private String email;
	private Date loggedDate;
	private String avatar;

	@OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Event> userEvents = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_event_following", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
	private List<Event> followingEvents = new ArrayList<>();

	public User() {
	}

	public User(Long id, String nickname, String name, String lastname, String email, String avatar, Date date) {
		this.id = id;
		this.nickname = nickname;
		this.name = name;
		this.lastname = lastname;
		this.showEmail = false;
		this.email = email;
		this.avatar = avatar;
		this.loggedDate = date;
		this.userEvents = new ArrayList<Event>();
		this.followingEvents = new ArrayList<Event>();
	}

}