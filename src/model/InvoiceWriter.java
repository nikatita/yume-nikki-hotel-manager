package model;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * InvoiceWriter
 * ─────────────────────────────────────────────────────────────────────
 * Generates a plain-text invoice file and saves it to:
 *
 *   invoices/
 *     ROOM_<roomNo>_<timestamp>.txt
 *
 * The invoices/ folder is created automatically next to the JAR.
 * Returns the saved file path so Main.java can show it in the alert.
 */
public class InvoiceWriter {

    private static final String FOLDER = "invoices";
    private static final DateTimeFormatter STAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DISPLAY_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");

    /**
     * Writes a .txt invoice and returns the path of the saved file.
     *
     * @param guestName  guest's name (or "—" if not recorded)
     * @param roomNo     room number
     * @param roomType   room type string
     * @param nights     number of nights stayed
     * @param roomRate   price per night
     * @param breakfast  whether breakfast add-on was selected
     * @param extraBed   whether extra bed add-on was selected
     * @param parking    whether parking add-on was selected
     * @param total      final bill total
     * @return absolute path to the saved invoice file
     * @throws IOException if the file cannot be written
     */
    public static String write(
            String  guestName,
            int     roomNo,
            String  roomType,
            int     nights,
            double  roomRate,
            boolean breakfast,
            boolean extraBed,
            boolean parking,
            double  total) throws IOException {

        // ── ensure invoices/ folder exists ────────────────────────────
        Path dir = Paths.get(FOLDER);
        Files.createDirectories(dir);

        // ── file name ─────────────────────────────────────────────────
        String timestamp = LocalDateTime.now().format(STAMP_FMT);
        String fileName  = String.format("ROOM_%d_%s.txt", roomNo, timestamp);
        Path   filePath  = dir.resolve(fileName);

        // ── build invoice text ────────────────────────────────────────
        String now = LocalDateTime.now().format(DISPLAY_FMT);

        StringBuilder sb = new StringBuilder();

        // header
        line(sb, "═", 52);
        center(sb, "夢日記  H O T E L", 52);
        center(sb, "DREAM LOG  ·  GUEST INVOICE", 52);
        line(sb, "═", 52);
        sb.append("\n");

        // meta
        kv(sb, "DATE / TIME",  now);
        kv(sb, "GUEST",        guestName);
        kv(sb, "ROOM NO",      String.valueOf(roomNo));
        kv(sb, "ROOM TYPE",    roomType);
        sb.append("\n");

        // charges
        line(sb, "─", 52);
        center(sb, "CHARGES", 52);
        line(sb, "─", 52);

        kvMoney(sb, String.format("Room rate  (%d night%s × ¥%.2f)",
                nights, nights == 1 ? "" : "s", roomRate),
                roomRate * nights);

        if (breakfast) kvMoney(sb, String.format("Breakfast  (%d × ¥200.00)", nights), 200.0 * nights);
        if (extraBed)  kvMoney(sb, String.format("Extra bed  (%d × ¥500.00)", nights), 500.0 * nights);
        if (parking)   kvMoney(sb, String.format("Parking    (%d × ¥100.00)", nights), 100.0 * nights);

        sb.append("\n");
        line(sb, "─", 52);

        // total
        String totalStr = String.format("¥ %.2f", total);
        String totalLine = "TOTAL DUE" + " ".repeat(Math.max(1, 52 - 9 - totalStr.length())) + totalStr;
        sb.append(totalLine).append("\n");

        line(sb, "═", 52);
        sb.append("\n");

        // footer
        center(sb, "thank you for staying in the dream", 52);
        center(sb, "・  ゆめにっき  ・", 52);
        sb.append("\n");
        center(sb, "[ saved: " + fileName + " ]", 52);
        line(sb, "═", 52);

        // ── write to disk ─────────────────────────────────────────────
        Files.writeString(filePath, sb.toString());

        return filePath.toAbsolutePath().toString();
    }

    // ── FORMATTING HELPERS ────────────────────────────────────────────

    private static void line(StringBuilder sb, String ch, int width) {
        sb.append(ch.repeat(width)).append("\n");
    }

    private static void center(StringBuilder sb, String text, int width) {
        int pad = Math.max(0, (width - text.length()) / 2);
        sb.append(" ".repeat(pad)).append(text).append("\n");
    }

    private static void kv(StringBuilder sb, String key, String value) {
        sb.append(String.format("  %-16s  %s%n", key, value));
    }

    private static void kvMoney(StringBuilder sb, String desc, double amount) {
        String amtStr = String.format("¥ %8.2f", amount);
        int gap = Math.max(1, 52 - 2 - desc.length() - amtStr.length());
        sb.append("  ").append(desc).append(" ".repeat(gap)).append(amtStr).append("\n");
    }
}
