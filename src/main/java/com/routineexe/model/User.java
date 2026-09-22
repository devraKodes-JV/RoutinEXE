package com.routineexe.model;

import java.util.Optional;

/**
 * Representa un usuario de la aplicación.
 * username es obligatorio; age, height y weight son optionales.
 */
public class User {

    private Long id;
    private String username;
    private Integer age;
    private Double height;
    private Double weight;

    public User() {
    }

    public User(Long id, String username, Integer age, Double height, Double weight) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Optional<Integer> getAge() {
        return Optional.ofNullable(age);
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Optional<Double> getHeight() {
        return Optional.ofNullable(height);
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Optional<Double> getWeight() {
        return Optional.ofNullable(weight);
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}