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
@Table(name = "admin3")
@Proxy(lazy = false)
public class Admin3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String Admin3Name_ar;

    private String Admin3Name_en;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Admin2_id", nullable = false)
    private Admin2 admin2;

    @OneToMany(
            mappedBy = "admin3",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Admin4> admin4s  = new ArrayList<>();

    public Admin3(){}

    public Admin3(String admin3Name_ar, String admin3Name_en) {
        this.Admin3Name_ar = admin3Name_ar;
        this.Admin3Name_en = admin3Name_en;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdmin3Name_ar() {
        return Admin3Name_ar;
    }

    public void setAdmin3Name_ar(String admin3Name_ar) {
        Admin3Name_ar = admin3Name_ar;
    }

    public String getAdmin3Name_en() {
        return Admin3Name_en;
    }

    public void setAdmin3Name_en(String admin3Name_en) {
        Admin3Name_en = admin3Name_en;
    }

    public Admin2 getAdmin2() {
        return admin2;
    }

    public void setAdmin2(Admin2 admin2) {
        this.admin2 = admin2;
    }
}
