package com.example.polls.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "telecome")
public class Telecome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer idnum;

    @NotBlank
    @Size(max = 5)
    private String gender;

    @NotBlank
    @Size(max = 10)
    private String city;

    private Long Phone;

    public Telecome(){}

    public Telecome(Long id, Integer idnum, String gender, String city, Long phone) {
        this.id = id;
        this.idnum = idnum;
        this.gender = gender;
        this.city = city;
        this.Phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdnum() {
        return idnum;
    }

    public void setIdnum(Integer idnum) {
        this.idnum = idnum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long phone) {
        Phone = phone;
    }
}
