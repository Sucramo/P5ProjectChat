package com.example.p5projectchat.Database;

public class User {

    public User (String firstname, String lastname, String email, String password, String userID, boolean isLoggedIn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
        this.userID = userID;
    }

    public User(){
    }

    public User(String firstName, String lastName, String email, String password, boolean isLoggedIn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    private String userID;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }


}
