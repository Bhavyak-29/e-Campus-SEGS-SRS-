package com.segs.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User2 {
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String height;
    private String weight;

    public User2(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public User2(Long id, String height, String weight) {
        this.id = id;
        this.height = height;
        this.weight = weight;
    }
    

}
