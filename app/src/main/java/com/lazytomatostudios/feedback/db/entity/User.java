package com.lazytomatostudios.feedback.db.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "user")
public class User {

    @NonNull
    @PrimaryKey
    private String phone_number;
    private String name, dob, mail , doa;

    @NonNull
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@NonNull String phone_number) {
        this.phone_number = phone_number;
    }

    @NonNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }
}
