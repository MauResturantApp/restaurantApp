package mau.restaurantapp.data.types;

/**
 * Created by Yoouughurt on 07-12-2016.
 */

public class UserProfileType {
    private String email;
    private String name;
    private String phoneNumber;

    public UserProfileType() {
    }

    public UserProfileType(String email, String name, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
