package com.routineexe.model;

import java.util.Optional;

/**
 * Representa un ejercicio de la aplicación.
 */
public class Exercise {

    private Long id;
    private String name;
    private Category category;
    private String description;
    private boolean timeBased;

    public Exercise() {
    }

    public Exercise(Long id, String name, Category category, String description, boolean timeBased) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.timeBased = timeBased;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Optional<Category> getCategoryOptional() {
        return Optional.ofNullable(category);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<String> getDescriptionOptional() {
        return Optional.ofNullable(description);
    }

    public boolean isTimeBased() {
        return timeBased;
    }

    public void setTimeBased(boolean timeBased) {
        this.timeBased = timeBased;
    }

    @Override
    public String toString() {
        return "Exercise{id=" + id + ", name='" + name + "', category=" + (category != null ? category.getName() : "null") + ", timeBased=" + timeBased + "}";
    }
}