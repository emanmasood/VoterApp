package com.example.polls.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "localchoices")
@Proxy(lazy = false)
public class LocalChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String text;

    @Column(name = "voteCount")
    private Long voteCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin4_id", nullable = false)
    @JsonIgnore
    private Admin4 admin4;

    public LocalChoice(){}

    public LocalChoice(String text, Long voteCount,Admin4 admin4) {
        this.text = text;
        this.voteCount = voteCount;
        this.admin4 = admin4;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Admin4 getAdmin4() {
        return admin4;
    }

    public void setAdmin4(Admin4 admin4) {
        this.admin4 = admin4;
    }
}
