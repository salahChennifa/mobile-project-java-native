package com.example.projecttestconnection;

public class Tache {
    private  String titre;
    private String date_time;



    public Tache(String titre, String date_time) {
        this.titre = titre;
        this.date_time = date_time;

    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

}
