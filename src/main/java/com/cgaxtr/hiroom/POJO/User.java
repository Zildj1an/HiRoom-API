package com.cgaxtr.hiroom.POJO;

public class User {

    private int id;
    private String name;
    private String email;
    private String pass;
    private String pathImg;
    private String gender;
    private Boolean smoker;
    private String worker;
    private String description;
    private int partying;
    private int organized;
    private int athlete;
    private int freak;
    private int sociable;
    private int active;
    /*
    public enum Gender {
        MALE("male"),
        FEMALE("female");

        private String gender;

        Gender(String gender){
            this.gender = gender;
        }

        @Override
        public String toString() {
            return gender;
        }
    }
    public enum Worker{
        STUDENT("student"),
        WORKER("worker"),
        BOTH("both");

        private String worker;

        Worker(String type){
            this.worker = type;
        }

        @Override
        public String toString() {
            return worker;
        }
    }
    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) {
        this.smoker = smoker;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPartying() {
        return partying;
    }

    public void setPartying(int partying) {
        this.partying = partying;
    }

    public int getOrganized() {
        return organized;
    }

    public void setOrganized(int organized) {
        this.organized = organized;
    }

    public int getAthlete() {
        return athlete;
    }

    public void setAthlete(int athlete) {
        this.athlete = athlete;
    }

    public int getFreak() {
        return freak;
    }

    public void setFreak(int freak) {
        this.freak = freak;
    }

    public int getSociable() {
        return sociable;
    }

    public void setSociable(int sociable) {
        this.sociable = sociable;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
