package com.adrar.sqlcda.model;

public class Category {
    /*
     * Attributs
     * */
    private int id;
    private String categoryName;

    /*
     * Constructeurs
     * */

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /*
     * Getters et Setters
     * */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /*
     * Méthodes
     * */

    @Override
    public String toString() {
        return "Category{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }
}
