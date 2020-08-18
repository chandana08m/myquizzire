package com.example.myquizzire;

import android.view.MenuItem;




public class categorymodel {
    private String name;
    private  String url;
     public categorymodel(){}
    public categorymodel(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
