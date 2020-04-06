package com.example.tickit;

public class NotificationMaker {
    String packageName;
    CharSequence TickerText;
    String NotificationID;
    String text;
    String title;

    public NotificationMaker(String packageName, CharSequence tickerText, String notificationID, String text, String title) {
        this.packageName = packageName;
        TickerText = tickerText;
        NotificationID = notificationID;
        this.text = text;
        this.title = title;

    }

    public String getPackageName() {
        return packageName;
    }

    public CharSequence getTickerText() {
        return TickerText;
    }

    public String getNotificationID() {
        return NotificationID;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTickerText(CharSequence tickerText) {
        TickerText = tickerText;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
