package com.example.demo.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Country")
public class Country extends DemoObject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_seq")
    @SequenceGenerator(schema = "public", name = "country_id_seq", sequenceName = "country_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "iso")
    private String iso;

    @Column(name = "description")
    private String description;

    @Column(name = "prefix")
    private String prefix;

    public Country() {

    }

    public Country(int id, String iso, String description, String prefix, Timestamp lastUpdateDate) {
        this.setLastUpdateDate(lastUpdateDate);
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
        return "Country: [" + id + ", " + iso + ", " + prefix + ", " + description + ", " + getLastUpdateDate() + "]";
    }
}