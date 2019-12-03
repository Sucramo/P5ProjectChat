package com.example.p5projectchat.Database;

public class User {

    public User (String firstname, String lastname, String email, String password, String userID, boolean isLoggedIn){
        this.firstName = firstname;
        this.lastName = lastname;
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

    private String profilePicture;

    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture(){
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture){
        this.profilePicture = profilePicture;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
}
