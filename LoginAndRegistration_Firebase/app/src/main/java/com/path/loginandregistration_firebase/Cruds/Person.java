package com.path.loginandregistration_firebase.Cruds;

public class Person {

    private String fullName;
    private int phoneNumber;

    public Person() {

    }

    public Person(String fullName, int phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
