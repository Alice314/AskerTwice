package com.wusui.askertwice.model;

import java.io.Serializable;

/**
 * Created by fg on 2016/7/23.
 */

public class QuestionsBean implements Serializable {

    private String id;
    private String contentId;
    private String title;
    private String date;
    private String recent;
    private String type;
    private String answerCount;
    private Object bestAnswerId;
    private String starCount;
    private String authorName;
    private String content;
    private boolean stared;


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public Object getBestAnswerId() {
        return bestAnswerId;
    }

    public void setBestAnswerId(Object bestAnswerId) {
        this.bestAnswerId = bestAnswerId;
    }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
