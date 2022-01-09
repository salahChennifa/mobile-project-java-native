package com.example.projecttestconnection;

public class Client {
    private int clientID;
    private String fName, clientEmail, passwd, clientPhone, identityNumber, lName, userType;

    public Client(int clientID, String  fName, String clientEmail, String passwd, String clientPhone, String identityNumber, String lName, String userType) {
        this.clientID = clientID;
        this.fName = fName;
        this.clientEmail = clientEmail;
        this.passwd = passwd;
        this.clientPhone = clientPhone;
        this.identityNumber = identityNumber;
        this.lName = lName;
        this.userType = userType;
    }

    public Client(int clientID, String clientEmail, String passwd, String clientPhone, String fName, String lName) {
        this.clientID = clientID;
        this.clientEmail = clientEmail;
        this.passwd = passwd;
        this.clientPhone = clientPhone;
        this.fName = fName;
        this.lName = lName;
    }


    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }


}
