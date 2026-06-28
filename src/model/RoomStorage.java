package model;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.*;

/**
 * Handles persistent storage of Room data using a CSV flat file.
 * File is saved next to the JAR as: hotel_rooms.csv
 *
 * CSV format (one room per line):
 * roomNo,type,price,available,breakfast,extraBed,parking
 *
 * Example:
 * 101,Single,1200.0,true,false,false,false
 */
public class RoomStorage {

    private static final String FILE_NAME = "hotel_rooms.csv";
    private static final String HEADER = "roomNo,type,price,available,breakfast,extraBed,parking";

    // ── SAVE ─────────────────────────────────────────────────────────
    /**
     * Overwrites the CSV file with the current room list.
     * Called after every Add / Check-In / Check-Out action.
     */
    public static void save(ObservableList<Room> rooms) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {

            pw.println(HEADER);

            for (Room r : rooms) {
                pw.printf("%d,%s,%.2f,%b,%b,%b,%b%n",
                        r.getRoomNo(),
                        r.getType(),
                        r.getPrice(),
                        r.isAvailable(),
                        r.isBreakfast(),
                        r.isExtraBed(),
                        r.isParking());
            }

        } catch (IOException e) {
            System.err.println("[RoomStorage] Save failed: " + e.getMessage());
        }
    }

    // ── LOAD ─────────────────────────────────────────────────────────
    /**
     * Reads the CSV file and populates the observable list.
     * Skips the header and any malformed lines silently.
     * Called once on application start.
     */
    public static void load(ObservableList<Room> rooms) {
        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            return; // first run — no file yet, that's fine
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {

                // skip header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // skip blank lines
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",", -1);
                if (parts.length != 7)
                    continue; // skip malformed

                try {
                    int roomNo = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    boolean available = Boolean.parseBoolean(parts[3].trim());
                    boolean breakfast = Boolean.parseBoolean(parts[4].trim());
                    boolean extraBed = Boolean.parseBoolean(parts[5].trim());
                    boolean parking = Boolean.parseBoolean(parts[6].trim());

                    Room r = new Room(roomNo, type, price);
                    r.setAvailable(available);
                    r.setExtras(breakfast, extraBed, parking);
                    rooms.add(r);

                } catch (NumberFormatException ignored) {
                    // skip lines with bad numbers
                }
            }

        } catch (IOException e) {
            System.err.println("[RoomStorage] Load failed: " + e.getMessage());
        }
    }
}
