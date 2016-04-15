package com.victor.polskavisa;

public class LocationEntity {

    private double lat;
    private double lng;
    private String title;
    private String adress;

    public LocationEntity(double lat, double lng, String title, String adress) throws NullPointerException{
        if(title == null || adress == null) {
            throw new NullPointerException();
        }
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.adress = adress;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }

    public String getAdress() {
        return adress;
    }
}
