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
@Table(name = "admin1")
@Proxy(lazy = false)
public class Admin1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String admin1Name_ar;

    private String admin1Name_en;

    @OneToMany(
            mappedBy = "admin1",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Admin2> admin2s  = new ArrayList<>();

    /*@OneToMany(
            mappedBy = "admin1",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    private List<Admin4> admin4s  = new ArrayList<>();
*/
    /*public Admin1(){};

    public Admin1(String admin1Name_ar, String admin1Name_en) {
        this.admin1Name_ar = admin1Name_ar;
        this.admin1Name_en = admin1Name_en;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin1Name_ar() {
        return admin1Name_ar;
    }

    public void setAdmin1Name_ar(String admin1Name_ar) {
        this.admin1Name_ar = admin1Name_ar;
    }

    public String getAdmin1Name_en() {
        return admin1Name_en;
    }

    public void setAdmin1Name_en(String admin1Name_en) {
        this.admin1Name_en = admin1Name_en;
    }
}
