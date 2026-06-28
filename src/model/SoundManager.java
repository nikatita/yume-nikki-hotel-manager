package model;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * SoundManager
 * ─────────────────────────────────────────────────────────────────────
 * Loads and plays short WAV/OGG sound effects using JavaFX AudioClip.
 * AudioClip keeps audio decoded in memory — perfect for UI clicks.
 *
 * ── HOW TO GET THE ACTUAL YUME NIKKI SOUNDS ──────────────────────────
 * The original game (free download at yumenikki.net) ships its sounds
 * inside the RPG Maker folder:
 *
 * YumeNikki/Audio/SE/
 *
 * Copy these files into your project at:
 *
 * src/main/resources/resources/sounds/
 *
 * Original name → Rename to (event)
 * ────────────────────────────────────────────────────
 * Cursor1.wav → click.wav tab switch / hover
 * Select.wav / OK.wav → confirm.wav OK alerts
 * Cancel.wav → cancel.wav error/warning alerts
 * Door.wav → checkin.wav check-in button
 * Step1.wav → checkout.wav check-out button
 * Knock.wav / Bell.wav → addroom.wav add room button
 * Buzz.wav / NG.wav → error.wav validation error
 *
 * Missing files are silently skipped — the app won't crash.
 * ─────────────────────────────────────────────────────────────────────
 */
public class SoundManager {

    public static final String CLICK = "click";
    public static final String CONFIRM = "confirm";
    public static final String CANCEL = "cancel";
    public static final String CHECKIN = "checkin";
    public static final String CHECKOUT = "checkout";
    public static final String ADD_ROOM = "addroom";
    public static final String ERROR = "error";
    public static final String TAB = "tab";

    private static final Map<String, AudioClip> clips = new HashMap<>();
    private static boolean muted = false;

    // ── INIT ─────────────────────────────────────────────────────────
    public static void init(Class<?> resourceBase) {
        load(resourceBase, CLICK, "click.wav");
        load(resourceBase, CONFIRM, "confirm.wav");
        load(resourceBase, CANCEL, "error.wav");
        load(resourceBase, CHECKIN, "confirm,.wav");
        load(resourceBase, CHECKOUT, "confirm.wav");
        load(resourceBase, ADD_ROOM, "click.wav");
        load(resourceBase, ERROR, "error.wav");

        // TAB shares the click sound
        if (clips.containsKey(CLICK)) {
            clips.put(TAB, clips.get(CLICK));
        }
    }

    private static void load(Class<?> base, String key, String filename) {
        try {
            URL url = base.getResource("/resources/sounds/" + filename);
            if (url != null) {
                AudioClip clip = new AudioClip(url.toExternalForm());
                clip.setVolume(0.7);
                clips.put(key, clip);
                System.out.println("[SoundManager] Loaded: " + filename);
            } else {
                System.out.println("[SoundManager] Not found (skipping): " + filename);
            }
        } catch (Exception e) {
            System.out.println("[SoundManager] Failed: " + filename + " — " + e.getMessage());
        }
    }

    // ── PLAY ─────────────────────────────────────────────────────────
    public static void play(String key) {
        if (muted)
            return;
        AudioClip clip = clips.get(key);
        if (clip != null)
            clip.play();
    }

    // ── MUTE ─────────────────────────────────────────────────────────
    public static void toggleMute() {
        muted = !muted;
    }

    public static boolean isMuted() {
        return muted;
    }
}
