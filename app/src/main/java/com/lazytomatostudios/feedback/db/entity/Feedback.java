package com.lazytomatostudios.feedback.db.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName="feedback")
public class Feedback {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int index;
    private float q1_rating, q2_rating, q3_rating, q4_rating, q5_rating;
    private String phone, date, waiter, table, comments, frequency, wait_time, customer;

    @NonNull
    public int getIndex() {
        return index;
    }

    public void setIndex(@NonNull int index) {
        this.index = index;
    }

    public float getQ1_rating() {
        return q1_rating;
    }

    public void setQ1_rating(float q1_rating) {
        this.q1_rating = q1_rating;
    }

    public float getQ2_rating() {
        return q2_rating;
    }

    public void setQ2_rating(float q2_rating) {
        this.q2_rating = q2_rating;
    }

    public float getQ3_rating() {
        return q3_rating;
    }

    public void setQ3_rating(float q3_rating) {
        this.q3_rating = q3_rating;
    }

    public float getQ4_rating() {
        return q4_rating;
    }

    public void setQ4_rating(float q4_rating) {
        this.q4_rating = q4_rating;
    }

    public float getQ5_rating() {
        return q5_rating;
    }

    public void setQ5_rating(float q5_rating) {
        this.q5_rating = q5_rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getWait_time() {
        return wait_time;
    }

    public void setWait_time(String wait_time) {
        this.wait_time = wait_time;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
