// NoteModel.java
package com.example.todo.Model;

public class NoteModel {

    private String noteId;
    private String title;
    private String content;

    private String timestamp;

    public NoteModel() {
        // Required empty public constructor
    }

    public NoteModel(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public  String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public NoteModel withId(String id) {
        this.noteId = id;
        return this;
    }
}
