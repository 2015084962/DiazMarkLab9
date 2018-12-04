package com.diaz.mark;

public class Person {

    String fullname, gender;
    int age;

    public Person() {
    }

    public Person(String fullname, int age, String gender) {
        this.fullname = fullname;
        this.age = age;
        this.gender = gender;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
