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
@Table(name = "admin2")
@Proxy(lazy = false)
public class Admin2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String admin2Name_ar;

    private String admin2Name_en;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin1_id", nullable = false)
    private Admin1 admin1;

    @OneToMany(
            mappedBy = "admin2",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Admin3> admin3s  = new ArrayList<>();

    /*@OneToMany(
            mappedBy = "admin2",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Admin4> admin4s  = new ArrayList<>();*/

    public Admin2(){};

    public Admin2(String admin2Name_ar, String admin2Name_en) {
        this.admin2Name_ar = admin2Name_ar;
        this.admin2Name_en = admin2Name_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin2Name_ar() {
        return admin2Name_ar;
    }

    public void setAdmin2Name_ar(String admin2Name_ar) {
        this.admin2Name_ar = admin2Name_ar;
    }

    public String getAdmin2Name_en() {
        return admin2Name_en;
    }

    public void setAdmin2Name_en(String admin2Name_en) {
        this.admin2Name_en = admin2Name_en;
    }

    public Admin1 getAdmin1() {
        return admin1;
    }

    public void setAdmin1(Admin1 admin1) {
        this.admin1 = admin1;
    }
}
