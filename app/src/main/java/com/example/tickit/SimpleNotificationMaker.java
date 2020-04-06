package com.example.tickit;

public class SimpleNotificationMaker {

    String tickerText;
    String packageName;
    long timeofPost;
    String AppName;
    String text;
    String title;
    String firebaseID;

    public SimpleNotificationMaker(String tickerText, String packageName, long timeofPost, String appName, String text, String title, String firebaseID) {
        this.tickerText = tickerText;
        this.packageName = packageName;
        this.timeofPost = timeofPost;
        AppName = appName;
        this.text = text;
        this.title = title;
        this.firebaseID = firebaseID;
    }

    public SimpleNotificationMaker() {
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getTimeofPost() {
        return timeofPost;
    }

    public void setTimeofPost(long timeofPost) {
        this.timeofPost = timeofPost;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }
}
