package com.bashirli.fastshop.model;

import com.google.firebase.Timestamp;

public class CommentData {
    private String email;
    private String comment;
    private Timestamp timestamp;
    private String imageURL;
    private String nickname;




    public CommentData(String email, String comment, Timestamp timestamp, String imageURL) {
        this.email = email;
        this.comment = comment;
        this.timestamp = timestamp;

        this.imageURL = imageURL;

    }
    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public String getImageURL() {
        return imageURL;
    }

}
