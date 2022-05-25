package app0.com.autoselfie;

import java.util.ArrayList;

public class UserModel {

    private int id;
    private String email, firstName, lastName;
    private ArrayList<String> images;

    public UserModel(String email) {
        this(email, null, null, -1);
    }

    public UserModel(String email, String firstName, String lastName, int id) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;

        images = new ArrayList<>();
    }


    public void addToImages(String imageLocation) {
        images.add(imageLocation);
    }

    public ArrayList<String> getImages() {

        return images;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
