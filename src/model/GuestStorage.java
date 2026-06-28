package model;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.*;

/**
 * GuestStorage
 * ─────────────────────────────────────────────────────────────────────
 * Saves and loads Guest records to/from  hotel_guests.csv
 *
 * CSV format (one guest per line):
 *   name,roomNo,checkInDate,checkOutDate,breakfast,extraBed,parking,totalBill
 *
 * Example:
 *   Madotsuki,101,2025-06-01,2025-06-03,true,false,true,2600.00
 */
public class GuestStorage {

    private static final String FILE_NAME = "hotel_guests.csv";
    private static final String HEADER =
            "name,roomNo,checkInDate,checkOutDate,breakfast,extraBed,parking,totalBill";

    // ── SAVE ─────────────────────────────────────────────────────────
    public static void save(ObservableList<Guest> guests) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println(HEADER);
            for (Guest g : guests) {
                pw.printf("%s,%d,%s,%s,%b,%b,%b,%.2f%n",
                        escapeCsv(g.getName()),
                        g.getRoomNo(),
                        g.getCheckInDate(),
                        g.getCheckOutDate(),
                        g.isBreakfast(),
                        g.isExtraBed(),
                        g.isParking(),
                        g.getTotalBill());
            }
        } catch (IOException e) {
            System.err.println("[GuestStorage] Save failed: " + e.getMessage());
        }
    }

    // ── LOAD ─────────────────────────────────────────────────────────
    public static void load(ObservableList<Guest> guests) {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                line = line.trim();
                if (line.isEmpty()) continue;

                // Split on comma but respect quoted fields (names may have spaces)
                String[] p = splitCsv(line);
                if (p.length != 8) continue;

                try {
                    String  name         = unescapeCsv(p[0]);
                    int     roomNo       = Integer.parseInt(p[1].trim());
                    String  checkIn      = p[2].trim();
                    String  checkOut     = p[3].trim();
                    boolean breakfast    = Boolean.parseBoolean(p[4].trim());
                    boolean extraBed     = Boolean.parseBoolean(p[5].trim());
                    boolean parking      = Boolean.parseBoolean(p[6].trim());
                    double  totalBill    = Double.parseDouble(p[7].trim());

                    guests.add(new Guest(name, roomNo, checkIn, checkOut,
                            breakfast, extraBed, parking, totalBill));
                } catch (Exception ignored) { }
            }
        } catch (IOException e) {
            System.err.println("[GuestStorage] Load failed: " + e.getMessage());
        }
    }

    // ── CSV HELPERS ───────────────────────────────────────────────────
    /** Wraps value in quotes if it contains a comma. */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String unescapeCsv(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    /**
     * Splits a CSV line respecting double-quoted fields.
     * Handles names like  "Smith, Jane"  correctly.
     */
    private static String[] splitCsv(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}
