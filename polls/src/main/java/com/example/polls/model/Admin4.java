package com.example.polls.model;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin4")
@Proxy(lazy = false)
public class Admin4 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String admin4Name_en;

    private String admin4Name_ar;

    @OneToMany(
            mappedBy = "admin4",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<LocalChoice> choices  = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin3_id", nullable = false)
    private Admin3 admin3;

    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin2_id", nullable = false)
    private Admin2 admin2;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin1_id", nullable = false)
    private Admin1 admin1;*/

    public Admin4(){}

    public Admin4(String admin4Name_ar, String admin4Name_en) {
        this.admin4Name_ar = admin4Name_ar;
        this.admin4Name_en = admin4Name_en;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdmin4Name_en() {
        return admin4Name_en;
    }

    public void setAdmin4Name_en(String admin4Name_en) {
        this.admin4Name_en = admin4Name_en;
    }

    public String getAdmin4Name_ar() {
        return admin4Name_ar;
    }

    public void setAdmin4Name_ar(String admin4Name_ar) {
        this.admin4Name_ar = admin4Name_ar;
    }

    public List<LocalChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<LocalChoice> choices) {
        this.choices = choices;
    }

    public Admin3 getAdmin3() {
        return admin3;
    }

    public void setAdmin3(Admin3 admin3) {
        this.admin3 = admin3;
    }

    /*public Admin2 getAdmin2() {
        return admin2;
    }

    public void setAdmin2(Admin2 admin2) {
        this.admin2 = admin2;
    }

    public Admin1 getAdmin1() {
        return admin1;
    }

    public void setAdmin1(Admin1 admin1) {
        this.admin1 = admin1;
    }*/

    public void addChoice(LocalChoice choice) {
        choices.add(choice);
        choice.setAdmin4(this);
    }

    public void removeChoice(LocalChoice choice) {
        choices.remove(choice);
        choice.setAdmin4(null);
    }
}
