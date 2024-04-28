package com.example.demo.models;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.demo.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = { "loggedDate", "userEvents", "followingEvents" })
    private User userOwner;

    private String name;
    private String type;
    private String description;
    private String image;
    private Date date;
    private String address;
    private String placeId;
    private String assistants;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Integer> followersHistory = new HashMap<>();

    @JsonIgnoreProperties({ "userEvents", "followingEvents" })
    @ManyToMany(mappedBy = "followingEvents", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<User> usersFollowing = new ArrayList<>();

    public Event() {}

    public Event(User userOwner, String name, String type, String description, String placeId, String image, Date date,
            String address, String assistants) {
        this.userOwner = userOwner;
        this.name = name;
        this.description = description;
        this.type = type;
        this.image = image;
        this.date = date;
        this.address = address;
        this.placeId = placeId;
        this.assistants = assistants;
        this.usersFollowing = new ArrayList<>();
        this.followersHistory.put(Utils.getCurrentDateAsString(), 0);
    }
}
