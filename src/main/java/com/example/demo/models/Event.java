package com.example.demo.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User userOwner;

    private String name;

    private String type;

    @Lob
    private byte[] image;

    private Date date;

    private String address;

    private String assistantsExpected;

    @ManyToMany
    private List<User> usersFollowing;

    public Event(Long id, User userOwner, String name, String type, byte[] image, Date date, String address,
            String assistantsExpected) {
        this.id = id;
        this.userOwner = userOwner;
        this.name = name;
        this.type = type;
        this.image = image;
        this.date = date;
        this.address = address;
        this.assistantsExpected = assistantsExpected;
        this.usersFollowing = new ArrayList<User>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User name) {
        this.userOwner = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAssistantsExpected() {
        return assistantsExpected;
    }

    public void setAssistantsExpected(String assistantsExpected) {
        this.assistantsExpected = assistantsExpected;
    }

    public List<User> getusersFollowing() {
        return usersFollowing;
    }

    public void setusersFollowing(List<User> usersFollowing) {
        this.usersFollowing = usersFollowing;
    }

}
