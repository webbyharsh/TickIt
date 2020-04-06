package com.example.tickit;

public class MyNots {
    String title;
    String packageName;
    String Count;
    String key;

    public MyNots(String title, String packageName, String count, String key) {
        this.title = title;
        this.packageName = packageName;
        Count = count;
        this.key = key;
    }

    public MyNots(){

    }

    public String getTitle() {
        return title;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getCount() {
        return Count;
    }

    public String getKey() {
        return key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setCount(String count) {
        Count = count;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
