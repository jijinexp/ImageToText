// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

//Note class Implementation
public class Note {

    private long id;
    private String name;
    private String message;

    //Default constructor - Sets the app to safe empty state
    Note() {
        this.id = -1;
        this.name = null;
        this.message = null;
    }

    //Constructor override
    Note(long id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

   //Setters & Getters
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getMessage() {
        return this.message;
    }

    public long getId() {
        return this.id;
    }
}
