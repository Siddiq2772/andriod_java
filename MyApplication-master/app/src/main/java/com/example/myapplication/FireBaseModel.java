package com.example.myapplication;

public class FireBaseModel {
    private String Title;
    private String Content;
    public FireBaseModel() {}

    public FireBaseModel(String Title, String Content) {
        this.Title = Title;
        this.Content = Content;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }
    public void setContent(String Content) {
        this.Content = Content;
    }

}
