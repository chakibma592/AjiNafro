package com.example.ajinafro.profile;

public class Picture {
    private int pictureId;
    private String uri;

    public Picture(int pictureId, String uri) {
        this.pictureId = pictureId;
        this.uri = uri;
    }

    public Picture() {
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
