package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Country")
public class Country {

    @Id
    private int id;
    private String iso;
    private String description;
    private String prefix;

    public Country(int id, String iso, String description, String prefix) {
        this.id = id;
        this.iso = iso;
        this.description = description;
        this.prefix = prefix;
    }

    public String getIso() {
        return iso;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "Country: ["+id+", "+prefix+", "+description+"]";
    }
}