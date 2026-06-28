package model;

public class Room {
    private int roomNo;
    private String type;
    private double price;
    private boolean available;
    private boolean breakfast;
    private boolean extraBed;
    private boolean parking;

    public Room(int roomNo, String type, double price) {
        this.roomNo = roomNo;
        this.type = type;
        this.price = price;
        this.available = true;
    }

    public void setExtras(boolean breakfast, boolean extraBed, boolean parking) {
        this.breakfast = breakfast;
        this.extraBed = extraBed;
        this.parking = parking;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isBreakfast() {
        return breakfast;
    }

    public boolean isExtraBed() {
        return extraBed;
    }

    public boolean isParking() {
        return parking;
    }

}