package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Guest
 * ─────────────────────────────────────────────────────────────────────
 * Represents a single guest record.
 * Created on check-in, updated (bill) on check-out.
 */
public class Guest {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String    name;
    private int       roomNo;
    private LocalDate checkInDate;
    private String    checkOutDate;   // "—" until checked out
    private boolean   breakfast;
    private boolean   extraBed;
    private boolean   parking;
    private double    totalBill;      // 0.0 until checked out

    // ── CONSTRUCTOR (check-in) ────────────────────────────────────────
    public Guest(String name, int roomNo,
                 boolean breakfast, boolean extraBed, boolean parking) {
        this.name         = name;
        this.roomNo       = roomNo;
        this.checkInDate  = LocalDate.now();
        this.checkOutDate = "—";
        this.breakfast    = breakfast;
        this.extraBed     = extraBed;
        this.parking      = parking;
        this.totalBill    = 0.0;
    }

    // ── CONSTRUCTOR (CSV restore) ─────────────────────────────────────
    public Guest(String name, int roomNo, String checkInDate, String checkOutDate,
                 boolean breakfast, boolean extraBed, boolean parking, double totalBill) {
        this.name         = name;
        this.roomNo       = roomNo;
        this.checkInDate  = LocalDate.parse(checkInDate, FMT);
        this.checkOutDate = checkOutDate;
        this.breakfast    = breakfast;
        this.extraBed     = extraBed;
        this.parking      = parking;
        this.totalBill    = totalBill;
    }

    // ── CHECK-OUT ─────────────────────────────────────────────────────
    public void checkOut(double bill) {
        this.totalBill    = bill;
        this.checkOutDate = LocalDate.now().format(FMT);
    }

    // ── GETTERS (JavaFX PropertyValueFactory needs these exact names) ──
    public String  getName()         { return name; }
    public int     getRoomNo()       { return roomNo; }
    public String  getCheckInDate()  { return checkInDate.format(FMT); }
    public String  getCheckOutDate() { return checkOutDate; }
    public boolean isBreakfast()     { return breakfast; }
    public boolean isExtraBed()      { return extraBed; }
    public boolean isParking()       { return parking; }
    public double  getTotalBill()    { return totalBill; }

    /** Human-readable extras summary for the table cell. */
    public String getExtras() {
        StringBuilder sb = new StringBuilder();
        if (breakfast) sb.append("BF ");
        if (extraBed)  sb.append("BED ");
        if (parking)   sb.append("PKG");
        return sb.length() == 0 ? "—" : sb.toString().trim();
    }

    /** Formatted bill — shows "pending" until checkout. */
    public String getBillDisplay() {
        return totalBill == 0.0 ? "pending" : String.format("¥ %.2f", totalBill);
    }
}
