package com.example.footforward;

public class PriceHolder {
    private String shoeThumbnail, shoeName, shoeCost;

    public PriceHolder(String shoeThumbnail, String shoeName, String shoeCost){
        this.shoeThumbnail = shoeThumbnail;
        this.shoeName = shoeName;
        this.shoeCost = shoeCost;
    }
    public String getShoeThumbnail(){
        return shoeThumbnail;
    }
    public void setShoeThumbnail(String shoeThumbnail){ this.shoeThumbnail = shoeThumbnail; }

    public String getShoeName(){
        return shoeName;
    }
    public void setShoeName(String shoeName){
        this.shoeName = shoeName;
    }

    public String getShoeCost(){
        return shoeCost;
    }
    public void setShoeCost(String shoeCost){
        this.shoeCost = shoeCost;
    }

}