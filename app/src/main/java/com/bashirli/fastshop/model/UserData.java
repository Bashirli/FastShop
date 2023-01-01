package com.bashirli.fastshop.model;

public class UserData {
    private String nickname;
    private String email;
    private String number;
    private String imageURL;

    public UserData(String nickname, String email, String number, String imageURL) {
        this.nickname = nickname;
        this.email = email;
        this.number = number;
        this.imageURL = imageURL;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getImageURL() {
        return imageURL;
    }
}
