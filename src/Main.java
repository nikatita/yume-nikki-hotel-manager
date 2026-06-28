import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation;
import javafx.util.Duration;
import model.Guest;
import model.GuestStorage;
import model.Room;
import model.RoomStorage;
import model.SoundManager;
import model.InvoiceWriter;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class Main extends Application {

    // ── DATA ──────────────────────────────────────────────────────────
    ObservableList<Room> roomList = FXCollections.observableArrayList();
    ObservableList<Guest> guestList = FXCollections.observableArrayList();

    // filtered view for the search bar
    FilteredList<Guest> filteredGuests;

    // ── PALETTE ───────────────────────────────────────────────────────
    private static final String BG_DARK = "#1a0d1f";
    private static final String BG_CARD = "#120a18";
    private static final String BG_TOPBAR = "#0f0714";
    private static final String PINK = "#d4a0e8";
    private static final String PINK_DIM = "#7a4a9a";
    private static final String BLUE = "#a0b4f0";
    private static final String BLUE_DIM = "#4a5a9a";
    private static final String PURPLE = "#c8a0e0";
    private static final String YELLOW = "#f5d76e";
    private static final String GREEN = "#a0e8b4";
    private static final String TEXT_MAIN = "#ede0f5";
    private static final String TEXT_MUTED = "#7a6090";
    private static final String BORDER = "#3a2050";
    private static final String INPUT_BG = "#160d20";

    // ── FONTS ─────────────────────────────────────────────────────────
    private static final String PIXEL_FONT = "'Press Start 2P', 'Courier New', monospace";
    private static final String BODY_FONT = "'VT323', 'Courier New', monospace";

    Font pixelFont = Font.loadFont(
            getClass().getResource("/resources/fonts/PressStart2P-Regular.ttf").toExternalForm(), 14);
    Font bodyFont = Font.loadFont(
            getClass().getResource("/resources/fonts/VT323-Regular.ttf").toExternalForm(), 18);

    // ── SHARED STYLES ─────────────────────────────────────────────────
    private static final String INPUT_STYLE = "-fx-background-color: " + INPUT_BG + ";" +
            "-fx-border-color: " + BORDER + " " + BORDER + " " + BLUE_DIM + " " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-family: " + BODY_FONT + ";" +
            "-fx-font-size: 16px;" +
            "-fx-background-radius: 0;" +
            "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
            "-fx-padding: 7 10 7 10;";

    private static final String BTN_NORMAL = "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + BLUE + ";" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: " + BLUE + ";" +
            "-fx-font-family: " + PIXEL_FONT + ";" +
            "-fx-font-size: 8px;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 10 18 10 18;" +
            "-fx-cursor: hand;";

    private static final String BTN_HOVER = "-fx-background-color: " + BLUE + ";" +
            "-fx-border-color: " + BLUE + ";" +
            "-fx-border-width: 2;" +
            "-fx-text-fill: " + BG_DARK + ";" +
            "-fx-font-family: " + PIXEL_FONT + ";" +
            "-fx-font-size: 8px;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 10 18 10 18;" +
            "-fx-cursor: hand;";

    private static final String BTN_PINK_NORMAL = "-fx-background-color: " + BG_CARD + ";-fx-border-color: " + PINK
            + ";-fx-border-width: 2;" +
            "-fx-text-fill: " + PINK + ";-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 8px;" +
            "-fx-background-radius: 0;-fx-padding: 10 18 10 18;-fx-cursor: hand;";

    private static final String BTN_PINK_HOVER = "-fx-background-color: " + PINK + ";-fx-border-color: " + PINK
            + ";-fx-border-width: 2;" +
            "-fx-text-fill: " + BG_DARK + ";-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 8px;" +
            "-fx-background-radius: 0;-fx-padding: 10 18 10 18;-fx-cursor: hand;";

    private static final String LABEL_STYLE = "-fx-font-family: " + PIXEL_FONT + ";" +
            "-fx-font-size: 7px;" +
            "-fx-text-fill: " + PURPLE + ";";

    private static final String TAB_NORMAL = "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + TEXT_MUTED + ";" +
            "-fx-font-family: " + BODY_FONT + ";" +
            "-fx-font-size: 18px;" +
            "-fx-padding: 14 30 14 30;" +
            "-fx-background-radius: 0;" +
            "-fx-cursor: hand;";

    private static final String TAB_ACTIVE = "-fx-background-color: " + BG_DARK + ";" +
            "-fx-border-color: " + PINK + " " + PINK + " " + BG_DARK + " " + PINK + ";" +
            "-fx-border-width: 2 2 0 2;" +
            "-fx-text-fill: " + PINK + ";" +
            "-fx-font-family: " + BODY_FONT + ";" +
            "-fx-font-size: 18px;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 14 30 14 30;" +
            "-fx-cursor: hand;";

    private static final String TAB_HOVER = "-fx-background-color: #1f1230;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-family: " + BODY_FONT + ";" +
            "-fx-font-size: 18px;" +
            "-fx-background-radius: 0;" +
            "-fx-padding: 14 30 14 30;" +
            "-fx-cursor: hand;";

    private static final String MUTE_BASE = "-fx-background-color: transparent;" +
            "-fx-border-width: 1;" +
            "-fx-background-radius: 0;" +
            "-fx-font-family: " + PIXEL_FONT + ";" +
            "-fx-font-size: 7px;" +
            "-fx-padding: 6 10 6 10;" +
            "-fx-cursor: hand;";

    private static final String MUTE_OFF = MUTE_BASE + "-fx-border-color: " + PINK_DIM + ";-fx-text-fill: " + TEXT_MUTED
            + ";";
    private static final String MUTE_ON = MUTE_BASE + "-fx-border-color: " + PINK + ";-fx-text-fill: " + PINK + ";";

    private TableView<Room> roomTable;

    // ─────────────────────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {

        // SoundManager.init(getClass());
        RoomStorage.load(roomList);
        GuestStorage.load(guestList);
        filteredGuests = new FilteredList<>(guestList, g -> true);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BG_DARK + ";");

        // ══ ACCENT LINE ═══════════════════════════════════════════════
        HBox accentLine = new HBox();
        accentLine.setPrefHeight(4);
        accentLine.setStyle("-fx-background-color: " + PINK + ";");

        // ══ TOP BAR ═══════════════════════════════════════════════════
        HBox topBar = new HBox(0);
        topBar.setStyle(
                "-fx-background-color: " + BG_TOPBAR + ";" +
                        "-fx-border-color: transparent transparent " + PINK + " transparent;" +
                        "-fx-border-width: 0 0 2 0;");
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setAlignment(Pos.BOTTOM_LEFT);

        // Logo
        ImageView logoView = null;
        try {
            Image img = new Image(
                    getClass().getResource("/resources/yume_nikki.jpg").toExternalForm());
            if (!img.isError()) {
                logoView = new ImageView(img);
                logoView.setFitHeight(130);
                logoView.setPreserveRatio(true);
                logoView.setStyle("-fx-effect: dropshadow(gaussian, #c7a0d4, 15, 0.4, 0, 0);");
                TranslateTransition fl = new TranslateTransition(Duration.seconds(3), logoView);
                fl.setFromY(0);
                fl.setToY(-10);
                fl.setAutoReverse(true);
                fl.setCycleCount(TranslateTransition.INDEFINITE);
                fl.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tabs (4 now)
        Button spacesTab = createTabButton("spaces");
        Button entryTab = createTabButton("entry");
        Button exitTab = createTabButton("exit");
        Button guestsTab = createTabButton("guests"); // ← NEW

        HBox tabsBox = new HBox(4, spacesTab, entryTab, exitTab, guestsTab);
        tabsBox.setAlignment(Pos.BOTTOM_LEFT);
        tabsBox.setPadding(new Insets(0, 0, 0, 20));

        // Icon buttons
        Button aboutBtn = makeIconBtn("[?]");
        aboutBtn.setOnAction(e -> {
            SoundManager.play(SoundManager.CLICK);
            openAboutDialog(stage);
        });

        Button muteBtn = new Button("♪ ON");
        muteBtn.setStyle(MUTE_OFF);
        muteBtn.setOnMouseEntered(e -> muteBtn.setStyle(
                MUTE_BASE + "-fx-border-color:" + PINK + ";-fx-text-fill:" + PINK + ";"));
        muteBtn.setOnMouseExited(e -> muteBtn.setStyle(SoundManager.isMuted() ? MUTE_ON : MUTE_OFF));
        muteBtn.setOnAction(e -> {
            SoundManager.toggleMute();
            if (SoundManager.isMuted()) {
                muteBtn.setText("♪ OFF");
                muteBtn.setStyle(MUTE_ON);
            } else {
                muteBtn.setText("♪ ON");
                muteBtn.setStyle(MUTE_OFF);
                SoundManager.play(SoundManager.CLICK);
            }
        });

        Label hotelTag = new Label("\n▛ DREAM LOG ▜\n ▌ registry ▐\n       \n");
        hotelTag.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-line-spacing: 10px;" +
                        "-fx-padding: 0 20 0 0;");
        Timeline flicker = new Timeline(
                new KeyFrame(Duration.ZERO, e -> hotelTag.setOpacity(1)),
                new KeyFrame(Duration.millis(60), e -> hotelTag.setOpacity(0.3)),
                new KeyFrame(Duration.millis(100), e -> hotelTag.setOpacity(1)),
                new KeyFrame(Duration.millis(140), e -> hotelTag.setOpacity(0.5)),
                new KeyFrame(Duration.millis(180), e -> hotelTag.setOpacity(1)),
                new KeyFrame(Duration.seconds(2.5)));

        flicker.setCycleCount(Animation.INDEFINITE);
        flicker.play();
        HBox iconRow = new HBox(6, muteBtn, aboutBtn);
        iconRow.setAlignment(Pos.TOP_RIGHT);
        iconRow.setPadding(new Insets(6, 0, 0, 0));

        VBox rightBox = new VBox(4, iconRow, hotelTag);
        rightBox.setAlignment(Pos.TOP_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (logoView != null) {
            topBar.getChildren().addAll(logoView, tabsBox, spacer, rightBox);
        } else {
            VBox textLogo = new VBox(4);
            textLogo.setAlignment(Pos.CENTER);
            Label jp = new Label("ゆめにっき");
            jp.setFont(pixelFont);
            jp.setStyle("-fx-text-fill: " + PINK + ";");
            Label en = new Label("YUME NIKKI");
            en.setFont(pixelFont);
            en.setStyle("-fx-text-fill: " + YELLOW + ";");
            textLogo.getChildren().addAll(jp, en);
            topBar.getChildren().addAll(textLogo, tabsBox, spacer, rightBox);
        }

        // ══ CONTENT HEADER ════════════════════════════════════════════
        HBox contentHeader = new HBox(10);
        contentHeader.setPadding(new Insets(14, 28, 0, 28));
        contentHeader.setAlignment(Pos.BASELINE_LEFT);

        Label contentTitle = new Label("Spaces Registry");
        contentTitle.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";" +
                        "-fx-font-size: 11px;" +
                        "-fx-text-fill: " + TEXT_MAIN + ";");
        contentHeader.getChildren().add(contentTitle);

        HBox pixelRule = new HBox();
        pixelRule.setPrefHeight(1);
        pixelRule.setMaxWidth(Double.MAX_VALUE);
        pixelRule.setStyle("-fx-background-color: " + PINK_DIM + ";");
        VBox.setMargin(pixelRule, new Insets(6, 28, 0, 28));

        StackPane content = new StackPane();
        content.setPadding(new Insets(16, 28, 24, 28));
        VBox.setVgrow(content, Priority.ALWAYS);

        root.getChildren().addAll(accentLine, topBar, contentHeader, pixelRule, content);

        // ══ SPACES PANEL ══════════════════════════════════════════════
        VBox spacesPanel = new VBox(14);

        HBox inputRow = new HBox(14);
        VBox roomNoBox = labeledField("ROOM NO", new TextField());
        VBox typeBox = labeledCombo("TYPE", new ComboBox<>());
        VBox priceBox = labeledField("PRICE / NIGHT", new TextField());

        TextField roomNoField = (TextField) roomNoBox.getChildren().get(1);
        ComboBox<String> typeCombo = (ComboBox<String>) typeBox.getChildren().get(1);
        TextField priceField = (TextField) priceBox.getChildren().get(1);

        typeCombo.getItems().addAll("Single", "Double", "Deluxe");
        typeCombo.setPromptText("— select —");
        typeCombo.setStyle(INPUT_STYLE + "-fx-pref-width: 160;");
        typeCombo.setOnMousePressed(e -> SoundManager.play(SoundManager.CLICK));

        roomNoField.setPromptText("101");
        priceField.setPromptText("0.00");
        addFocusSound(roomNoField);
        addFocusSound(priceField);

        HBox.setHgrow(roomNoBox, Priority.ALWAYS);
        HBox.setHgrow(typeBox, Priority.ALWAYS);
        HBox.setHgrow(priceBox, Priority.ALWAYS);
        inputRow.getChildren().addAll(roomNoBox, typeBox, priceBox);

        Button addBtn = createActionButton("ADD ROOM");

        VBox tableCard = new VBox(0);
        tableCard.setStyle("-fx-border-color: " + BORDER + ";-fx-border-width: 2;");

        roomTable = new TableView<>(roomList);
        styleTable(roomTable);
        roomTable.setPrefHeight(180);
        roomTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, ov, nv) -> {
                    if (nv != null)
                        SoundManager.play(SoundManager.CLICK);
                });
        roomTable.getColumns().addAll(
                styledColumn("NO", "roomNo"),
                styledColumn("TYPE", "type"),
                styledColumn("PRICE", "price"),
                styledColumn("STATUS", "available"));
        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        roomTable.skinProperty().addListener((obs, o, n) -> styleColumnHeaders(roomTable));
        roomTable.setRowFactory(tv -> styledRow());
        tableCard.getChildren().add(roomTable);

        addBtn.setOnAction(e -> {
            try {
                int rNo = Integer.parseInt(roomNoField.getText().trim());
                String rTyp = typeCombo.getValue();
                double rPrc = Double.parseDouble(priceField.getText().trim());
                if (rTyp == null) {
                    SoundManager.play(SoundManager.ERROR);
                    showAlert("!! NOTICE", "Please select a room type.");
                    return;
                }
                roomList.add(new Room(rNo, rTyp, rPrc));
                RoomStorage.save(roomList);
                SoundManager.play(SoundManager.ADD_ROOM);
                roomNoField.clear();
                priceField.clear();
                typeCombo.setValue(null);
                showAlert(">> OK", "Room registered.");
            } catch (Exception ex) {
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! ERROR", "Check your input values.");
            }
        });

        spacesPanel.getChildren().addAll(inputRow, addBtn, tableCard);

        // ══ ENTRY PANEL ═══════════════════════════════════════════════
        VBox entryPanel = new VBox(16);

        VBox guestNameBox = labeledField("GUEST NAME", new TextField());
        VBox entryRoomBox = labeledField("ROOM NO", new TextField());

        TextField guestNameField = (TextField) guestNameBox.getChildren().get(1);
        TextField entryRoomField = (TextField) entryRoomBox.getChildren().get(1);
        guestNameField.setPromptText("traveller's name...");
        entryRoomField.setPromptText("101");
        addFocusSound(guestNameField);
        addFocusSound(entryRoomField);

        HBox entryRow = new HBox(14);
        HBox.setHgrow(guestNameBox, Priority.ALWAYS);
        HBox.setHgrow(entryRoomBox, Priority.ALWAYS);
        entryRow.getChildren().addAll(guestNameBox, entryRoomBox);

        Label extrasTitle = new Label("▛ ADD-ONS ▜");
        extrasTitle.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 10px;" +
                        "-fx-text-fill: " + PINK + ";-fx-effect: dropshadow(gaussian,#c8a0e0,10,0.4,0,0);");

        String EXTRA_STYLE = "-fx-background-color: " + BG_CARD + ";-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 2;-fx-text-fill: " + TEXT_MAIN + ";" +
                "-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 8px;" +
                "-fx-padding: 10 16 10 16;-fx-cursor: hand;";

        CheckBox breakfast = styledCheckbox("BREAKFAST (+200)", EXTRA_STYLE);
        CheckBox extraBed = styledCheckbox("EXTRA BED (+500)", EXTRA_STYLE);
        CheckBox parking = styledCheckbox("PARKING (+100)", EXTRA_STYLE);

        VBox extrasBox = new VBox(12, extrasTitle, breakfast, extraBed, parking);

        Button checkInBtn = createActionButton("CHECK IN");
        checkInBtn.setOnAction(e -> {
            String gName = guestNameField.getText().trim();
            if (gName.isEmpty()) {
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! NOTICE", "Please enter a guest name.");
                return;
            }
            try {
                int rNo = Integer.parseInt(entryRoomField.getText().trim());
                for (Room r : roomList) {
                    if (r.getRoomNo() == rNo) {
                        if (!r.isAvailable()) {
                            SoundManager.play(SoundManager.ERROR);
                            showAlert("!! NOTICE", "Room is occupied.");
                            return;
                        }
                        r.setAvailable(false);
                        r.setExtras(breakfast.isSelected(), extraBed.isSelected(), parking.isSelected());
                        roomTable.refresh();
                        RoomStorage.save(roomList);

                        // ── CREATE GUEST RECORD ──────────────────── NEW
                        Guest g = new Guest(gName, rNo,
                                breakfast.isSelected(),
                                extraBed.isSelected(),
                                parking.isSelected());
                        guestList.add(g);
                        GuestStorage.save(guestList);

                        SoundManager.play(SoundManager.CHECKIN);
                        guestNameField.clear();
                        entryRoomField.clear();
                        breakfast.setSelected(false);
                        extraBed.setSelected(false);
                        parking.setSelected(false);
                        showAlert(">> OK", "Guest checked in.");
                        return;
                    }
                }
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! NOTICE", "Room not found.");
            } catch (Exception ex) {
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! ERROR", "Check your input values.");
            }
        });

        entryPanel.getChildren().addAll(entryRow, extrasBox, checkInBtn);

        // ══ EXIT PANEL ════════════════════════════════════════════════
        // ══ EXIT PANEL ════════════════════════════════════════════════
        VBox exitPanel = new VBox(14);

        VBox exitRoomBox = labeledField("ROOM NO", new TextField());
        VBox daysBox = labeledField("NIGHTS STAYED", new TextField());

        TextField exitRoomField = (TextField) exitRoomBox.getChildren().get(1);
        TextField daysField = (TextField) daysBox.getChildren().get(1);

        exitRoomField.setPromptText("101");
        daysField.setPromptText("1");

        addFocusSound(exitRoomField);
        addFocusSound(daysField);

        HBox exitRow = new HBox(14);
        HBox.setHgrow(exitRoomBox, Priority.ALWAYS);
        HBox.setHgrow(daysBox, Priority.ALWAYS);
        exitRow.getChildren().addAll(exitRoomBox, daysBox);

        Button checkOutBtn = createActionButton("CHECK OUT");

        // ── BILL CARD ───────────────────────────────────────────────
        HBox billCard = new HBox(20);
        billCard.setAlignment(Pos.CENTER_LEFT);
        billCard.setPadding(new Insets(14, 24, 14, 24));
        billCard.setStyle("-fx-background-color: " + BG_CARD + ";-fx-border-color: " + PINK + ";-fx-border-width: 2;");
        billCard.setVisible(false);
        billCard.setManaged(false);

        Label billLabel = new Label("▓ TOTAL DUE ▓");
        billLabel.setStyle("-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 8px;-fx-text-fill: " + PINK + ";");

        Label billAmount = new Label("¥ 0.00");
        billAmount.setStyle("-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 18px;-fx-text-fill: " + YELLOW + ";");

        billCard.getChildren().addAll(billLabel, billAmount);

        // ── INVOICE BUTTON ──────────────────────────────────────────
        Button invoiceBtn = new Button("▼ DOWNLOAD INVOICE");
        invoiceBtn.setStyle(BTN_PINK_NORMAL);
        invoiceBtn.setOnMouseEntered(e -> invoiceBtn.setStyle(BTN_HOVER));
        invoiceBtn.setOnMouseExited(e -> invoiceBtn.setStyle(BTN_PINK_NORMAL));
        invoiceBtn.setVisible(false);
        invoiceBtn.setManaged(false);

        // ── CONTEXT STORAGE ─────────────────────────────────────────
        final int[] lastRoomNo = { -1 };
        final int[] lastNights = { -1 };
        final double[] lastBill = { 0 };
        final String[] lastRoomType = { "" };
        final String[] lastGuest = { "" };
        final boolean[] lastBreakfast = { false };
        final boolean[] lastExtraBed = { false };
        final boolean[] lastParking = { false };

        // ── INVOICE ACTION ──────────────────────────────────────────
        invoiceBtn.setOnAction(e -> {
            try {
                double roomRate = lastBill[0];
                for (Room r : roomList) {
                    if (r.getRoomNo() == lastRoomNo[0]) {
                        roomRate = r.getPrice();
                        break;
                    }
                }

                String path = InvoiceWriter.write(
                        lastGuest[0],
                        lastRoomNo[0],
                        lastRoomType[0],
                        lastNights[0],
                        roomRate,
                        lastBreakfast[0],
                        lastExtraBed[0],
                        lastParking[0],
                        lastBill[0]);

                showAlert(">> INVOICE SAVED", "Saved to:\n" + path);

            } catch (Exception ex) {
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! ERROR", "Could not save invoice.");
            }
        });

        // ── CHECKOUT LOGIC ──────────────────────────────────────────
        checkOutBtn.setOnAction(e -> {
            try {
                int rNo = Integer.parseInt(exitRoomField.getText().trim());
                int days = Integer.parseInt(daysField.getText().trim());

                if (days < 1) {
                    SoundManager.play(SoundManager.ERROR);
                    showAlert("!! ERROR", "Nights must be >= 1.");
                    return;
                }

                for (Room r : roomList) {
                    if (r.getRoomNo() == rNo) {

                        if (r.isAvailable()) {
                            SoundManager.play(SoundManager.ERROR);
                            showAlert("!! NOTICE", "Room is already empty.");
                            return;
                        }

                        r.setAvailable(true);

                        double bill = r.getPrice() * days;
                        if (r.isBreakfast())
                            bill += 200 * days;
                        if (r.isExtraBed())
                            bill += 500 * days;
                        if (r.isParking())
                            bill += 100 * days;

                        // Update guest
                        String guestName = "—";
                        for (int i = guestList.size() - 1; i >= 0; i--) {
                            Guest g = guestList.get(i);
                            if (g.getRoomNo() == rNo && g.getCheckOutDate().equals("—")) {
                                g.checkOut(bill);
                                guestName = g.getName();
                                break;
                            }
                        }

                        GuestStorage.save(guestList);

                        // Store for invoice
                        lastRoomNo[0] = rNo;
                        lastNights[0] = days;
                        lastBill[0] = bill;
                        lastRoomType[0] = r.getType();
                        lastGuest[0] = guestName;
                        lastBreakfast[0] = r.isBreakfast();
                        lastExtraBed[0] = r.isExtraBed();
                        lastParking[0] = r.isParking();

                        billAmount.setText(String.format("¥ %.2f", bill));
                        billCard.setVisible(true);
                        billCard.setManaged(true);

                        invoiceBtn.setVisible(true);
                        invoiceBtn.setManaged(true);

                        roomTable.refresh();
                        RoomStorage.save(roomList);

                        SoundManager.play(SoundManager.CHECKOUT);

                        exitRoomField.clear();
                        daysField.clear();

                        showAlert(">> OK", "Checked out. Invoice ready.");

                        return;
                    }
                }

                SoundManager.play(SoundManager.ERROR);
                showAlert("!! NOTICE", "Room not found.");

            } catch (Exception ex) {
                SoundManager.play(SoundManager.ERROR);
                showAlert("!! ERROR", "Check your input values.");
            }
        });

        // ── FINAL ADD ───────────────────────────────────────────────
        exitPanel.getChildren().addAll(exitRow, checkOutBtn, billCard, invoiceBtn);
        // ══ GUESTS PANEL ══════════════════════════════════════════════ NEW
        VBox guestsPanel = buildGuestsPanel();

        // ══ TAB SWITCHING ═════════════════════════════════════════════
        content.getChildren().add(spacesPanel);
        setTabActive(spacesTab);

        spacesTab.setOnAction(e -> {
            SoundManager.play(SoundManager.TAB);
            contentTitle.setText("Spaces Registry");
            content.getChildren().setAll(spacesPanel);
            setTabActive(spacesTab);
            clearTabActive(entryTab, exitTab, guestsTab);
        });
        entryTab.setOnAction(e -> {
            SoundManager.play(SoundManager.TAB);
            contentTitle.setText("Entry — Check-In Desk");
            content.getChildren().setAll(entryPanel);
            setTabActive(entryTab);
            clearTabActive(spacesTab, exitTab, guestsTab);
        });
        exitTab.setOnAction(e -> {
            SoundManager.play(SoundManager.TAB);
            contentTitle.setText("Exit — Departure Log");
            content.getChildren().setAll(exitPanel);
            setTabActive(exitTab);
            clearTabActive(spacesTab, entryTab, guestsTab);
        });
        guestsTab.setOnAction(e -> {
            SoundManager.play(SoundManager.TAB);
            contentTitle.setText("Guests — Dream Registry");
            content.getChildren().setAll(guestsPanel);
            setTabActive(guestsTab);
            clearTabActive(spacesTab, entryTab, exitTab);
        });

        // ══ SCENE ═════════════════════════════════════════════════════
        Scene scene = new Scene(root, 960, 600); // slightly wider for 4 tabs
        stage.setTitle("夢日記 Hotel");
        stage.setScene(scene);
        stage.show();
    }

    // ══ GUESTS PANEL BUILDER ══════════════════════════════════════════
    /**
     * Builds the entire Guests tab panel.
     * Contains:
     * • A search bar that filters by name OR room number in real time
     * • A full-width table showing all guest fields
     * • A small stats row (total guests / currently in-house)
     */
    private VBox buildGuestsPanel() {
        VBox panel = new VBox(14);

        // ── SEARCH BAR ───────────────────────────────────────────────
        HBox searchRow = new HBox(10);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        Label searchLbl = new Label("SEARCH");
        searchLbl.setStyle(LABEL_STYLE);

        TextField searchField = new TextField();
        searchField.setStyle(INPUT_STYLE);
        searchField.setPromptText("name or room no...");
        searchField.setMaxWidth(320);
        addFocusSound(searchField);

        // Clear button
        Button clearBtn = new Button("✕");
        clearBtn.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                        "-fx-border-color: " + PINK_DIM + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-font-family: " + BODY_FONT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-cursor: hand;");
        clearBtn.setOnMouseEntered(e -> clearBtn.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                        "-fx-border-color: " + PINK + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-text-fill: " + PINK + ";" +
                        "-fx-font-family: " + BODY_FONT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-cursor: hand;"));
        clearBtn.setOnMouseExited(e -> clearBtn.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                        "-fx-border-color: " + PINK_DIM + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";" +
                        "-fx-font-family: " + BODY_FONT + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-cursor: hand;"));
        clearBtn.setOnAction(e -> {
            SoundManager.play(SoundManager.CLICK);
            searchField.clear();
        });

        // Live filter logic — matches name (partial, case-insensitive) or exact room no
        searchField.textProperty().addListener((obs, oldVal, query) -> {
            String q = query.trim().toLowerCase();
            if (q.isEmpty()) {
                filteredGuests.setPredicate(g -> true);
            } else {
                filteredGuests.setPredicate(g -> g.getName().toLowerCase().contains(q) ||
                        String.valueOf(g.getRoomNo()).contains(q));
            }
        });

        searchRow.getChildren().addAll(searchLbl, searchField, clearBtn);

        // ── STATS ROW ────────────────────────────────────────────────
        Label statsLabel = new Label();
        statsLabel.setStyle(
                "-fx-font-family: " + BODY_FONT + ";" +
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";");

        // Update stats whenever guestList changes
        Runnable updateStats = () -> {
            long total = guestList.size();
            long inHouse = guestList.stream().filter(g -> g.getCheckOutDate().equals("—")).count();
            long checkedOut = total - inHouse;
            statsLabel.setText(String.format(
                    "▌ total: %d  ▌ in-house: %d  ▌ departed: %d", total, inHouse, checkedOut));
        };
        guestList.addListener((javafx.collections.ListChangeListener<Guest>) c -> updateStats.run());
        updateStats.run();

        // ── GUEST TABLE ───────────────────────────────────────────────
        SortedList<Guest> sortedGuests = new SortedList<>(filteredGuests);

        TableView<Guest> guestTable = new TableView<>(sortedGuests);
        sortedGuests.comparatorProperty().bind(guestTable.comparatorProperty());

        styleTable(guestTable);
        VBox.setVgrow(guestTable, Priority.ALWAYS);
        guestTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, ov, nv) -> {
                    if (nv != null)
                        SoundManager.play(SoundManager.CLICK);
                });

        // Columns
        TableColumn<Guest, String> nameCol = guestStyledColumn("NAME", "name", PINK);
        TableColumn<Guest, Integer> roomCol = guestStyledColumn("ROOM", "roomNo", BLUE);
        TableColumn<Guest, String> checkInCol = guestStyledColumn("CHECK-IN", "checkInDate", PURPLE);
        TableColumn<Guest, String> checkOutCol = guestStyledColumn("CHECK-OUT", "checkOutDate", PURPLE);
        TableColumn<Guest, String> extrasCol = guestStyledColumn("ADD-ONS", "extras", TEXT_MUTED);
        TableColumn<Guest, String> billCol = guestStyledColumn("BILL", "billDisplay", YELLOW);

        // Colour the bill cell green if paid, pink-dim if pending
        billCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                String colour = item.equals("pending") ? PINK_DIM : GREEN;
                setStyle(
                        "-fx-text-fill: " + colour + ";" +
                                "-fx-font-family: " + BODY_FONT + ";" +
                                "-fx-font-size: 16px;" +
                                "-fx-alignment: CENTER-LEFT;");
            }
        });

        // Status column — "in-house" vs "departed"
        TableColumn<Guest, String> statusCol = new TableColumn<>("STATUS");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCheckOutDate().equals("—") ? "in-house" : "departed"));
        statusCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                String colour = item.equals("in-house") ? GREEN : TEXT_MUTED;
                setStyle(
                        "-fx-text-fill: " + colour + ";" +
                                "-fx-font-family: " + BODY_FONT + ";" +
                                "-fx-font-size: 16px;" +
                                "-fx-alignment: CENTER-LEFT;");
            }
        });
        statusCol.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";" +
                        "-fx-font-size: 8px;" +
                        "-fx-text-fill: " + PINK + ";");

        guestTable.getColumns().addAll(
                nameCol, roomCol, checkInCol, checkOutCol, extrasCol, billCol, statusCol);
        guestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        guestTable.skinProperty().addListener((obs, o, n) -> styleColumnHeaders(guestTable));
        guestTable.setRowFactory(tv -> styledRow());

        VBox tableWrap = new VBox(0);
        tableWrap.setStyle("-fx-border-color: " + BORDER + ";-fx-border-width: 2;");
        tableWrap.getChildren().add(guestTable);
        VBox.setVgrow(tableWrap, Priority.ALWAYS);

        panel.getChildren().addAll(searchRow, statsLabel, tableWrap);
        return panel;
    }

    // ── ABOUT DIALOG ──────────────────────────────────────────────────
    private void openAboutDialog(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/fxml/AboutDialog.fxml"));
            Parent dialogRoot = loader.load();
            Stage dialog = new Stage();
            dialog.initOwner(owner);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("About — 夢日記 Hotel");
            dialog.setResizable(false);
            dialog.setScene(new Scene(dialogRoot, 460, 340));
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("!! ERROR", "Could not load about dialog.");
        }
    }

    // ── TABLE HELPERS ─────────────────────────────────────────────────

    private <T> void styleTable(TableView<T> t) {
        t.setStyle(
                "-fx-background-color: " + BG_CARD + ";" +
                        "-fx-control-inner-background: " + BG_CARD + ";" +
                        "-fx-table-cell-border-color: " + BORDER + ";" +
                        "-fx-text-fill: " + TEXT_MAIN + ";" +
                        "-fx-selection-bar: " + BLUE + ";" +
                        "-fx-selection-bar-non-focused: " + PINK_DIM + ";");
    }

    private <T> void styleColumnHeaders(TableView<T> t) {
        t.lookupAll(".column-header").forEach(node -> node.setStyle(
                "-fx-background-color: " + BG_TOPBAR + ";" +
                        "-fx-border-color: " + PINK + ";" +
                        "-fx-text-fill: " + PINK + ";" +
                        "-fx-font-family: " + PIXEL_FONT + ";"));
    }

    private <T> TableRow<T> styledRow() {
        return new TableRow<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setStyle(empty || item == null
                        ? "-fx-background-color: transparent;"
                        : "-fx-background-color: " + BG_CARD + ";" +
                                "-fx-border-color: " + BORDER + ";" +
                                "-fx-text-fill: " + TEXT_MAIN + ";");
            }
        };
    }

    /** Generic styled column for Room table (BLUE text). */
    private <T> TableColumn<Room, T> styledColumn(String title, String prop) {
        return styledColImpl(title, prop, BLUE);
    }

    /** Generic styled column for Guest table with custom colour. */
    @SuppressWarnings("unchecked")
    private <T, M> TableColumn<T, M> guestStyledColumn(String title, String prop, String colour) {
        TableColumn<T, M> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(M item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item.toString());
                setStyle(
                        "-fx-text-fill: " + colour + ";" +
                                "-fx-font-family: " + BODY_FONT + ";" +
                                "-fx-font-size: 16px;" +
                                "-fx-alignment: CENTER-LEFT;");
            }
        });
        col.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";" +
                        "-fx-font-size: 8px;" +
                        "-fx-text-fill: " + PINK + ";");
        return col;
    }

    @SuppressWarnings("unchecked")
    private <T> TableColumn<Room, T> styledColImpl(String title, String prop, String colour) {
        TableColumn<Room, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(prop));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item.toString());
                setStyle(
                        "-fx-text-fill: " + colour + ";" +
                                "-fx-font-family: " + BODY_FONT + ";" +
                                "-fx-font-size: 16px;" +
                                "-fx-alignment: CENTER-LEFT;");
            }
        });
        col.setStyle(
                "-fx-font-family: " + PIXEL_FONT + ";" +
                        "-fx-font-size: 8px;" +
                        "-fx-text-fill: " + PINK + ";");
        return col;
    }

    // ── UI HELPERS ────────────────────────────────────────────────────

    private CheckBox styledCheckbox(String text, String style) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle(style);
        cb.setGraphic(null);
        cb.setOnMouseEntered(e -> {
            if (!cb.isSelected())
                cb.setStyle(style + "-fx-border-color:" + PINK + ";");
        });
        cb.setOnMouseExited(e -> {
            if (!cb.isSelected())
                cb.setStyle(style);
        });
        cb.selectedProperty().addListener((obs, o, n) -> {
            cb.setStyle(style + (n ? "-fx-border-color:" + BLUE + ";" : ""));
            SoundManager.play(SoundManager.CLICK);
        });
        return cb;
    }

    private Button makeIconBtn(String text) {
        Button btn = new Button(text);
        String off = "-fx-background-color: transparent;-fx-border-color: " + PINK_DIM +
                ";-fx-border-width: 1;-fx-text-fill: " + TEXT_MUTED +
                ";-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 7px;" +
                "-fx-background-radius: 0;-fx-padding: 6 10 6 10;-fx-cursor: hand;";
        String on = "-fx-background-color: transparent;-fx-border-color: " + PINK +
                ";-fx-border-width: 1;-fx-text-fill: " + PINK +
                ";-fx-font-family: " + PIXEL_FONT + ";-fx-font-size: 7px;" +
                "-fx-background-radius: 0;-fx-padding: 6 10 6 10;-fx-cursor: hand;";
        btn.setStyle(off);
        btn.setOnMouseEntered(e -> btn.setStyle(on));
        btn.setOnMouseExited(e -> btn.setStyle(off));
        return btn;
    }

    private void addFocusSound(TextField f) {
        f.focusedProperty().addListener((o, ov, nv) -> {
            if (nv)
                SoundManager.play(SoundManager.CLICK);
        });
    }

    private VBox labeledField(String labelText, TextField field) {
        Label lbl = new Label(labelText);
        lbl.setStyle(LABEL_STYLE);
        field.setStyle(INPUT_STYLE);
        field.setMaxWidth(Double.MAX_VALUE);
        return new VBox(6, lbl, field);
    }

    private VBox labeledCombo(String labelText, ComboBox<String> combo) {
        Label lbl = new Label(labelText);
        lbl.setStyle(LABEL_STYLE);
        combo.setMaxWidth(Double.MAX_VALUE);
        return new VBox(6, lbl, combo);
    }

    private Button createTabButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(TAB_NORMAL);
        btn.setUserData("inactive");
        btn.setOnMouseEntered(e -> {
            if (!"active".equals(btn.getUserData()))
                btn.setStyle(TAB_HOVER);
        });
        btn.setOnMouseExited(e -> {
            if (!"active".equals(btn.getUserData()))
                btn.setStyle(TAB_NORMAL);
        });
        return btn;
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(BTN_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(BTN_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(BTN_NORMAL));
        return btn;
    }

    private void setTabActive(Button btn) {
        btn.setUserData("active");
        btn.setStyle(TAB_ACTIVE);
    }

    private void clearTabActive(Button... btns) {
        for (Button b : btns) {
            b.setUserData("inactive");
            b.setStyle(TAB_NORMAL);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        DialogPane dp = alert.getDialogPane();
        dp.setStyle(
                "-fx-background-color: " + BG_DARK + ";" +
                        "-fx-border-color: " + PINK + ";-fx-border-width: 2;");
        Label lbl = (Label) dp.lookup(".content.label");
        if (lbl != null)
            lbl.setStyle(
                    "-fx-font-family: " + BODY_FONT + ";-fx-font-size: 16px;-fx-text-fill: " + TEXT_MAIN + ";");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
