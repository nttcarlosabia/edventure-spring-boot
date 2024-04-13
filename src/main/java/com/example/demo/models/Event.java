package com.example.demo.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userOwner;

    private String name;

    private String type;

    private String description;

    private String image;

    private Date date;

    private String address;

    private String assistants;

    @ManyToMany
    private List<User> usersFollowing;

    public Event() {
    }

    public Event(Long userOwner, String name, String type, String description, String image, Date date, String address,
            String assistants) {
        this.userOwner = userOwner;
        this.name = name;
        this.description = description;
        this.type = type;
        this.image = image;
        this.date = date;
        this.address = address;
        this.assistants = assistants;
        this.usersFollowing = new ArrayList<User>();
    }

}
