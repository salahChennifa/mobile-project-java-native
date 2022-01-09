package com.example.projecttestconnection;

public class Monitor {
    String id, fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Monitor(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}
