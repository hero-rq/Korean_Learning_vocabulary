package com.example.chimchak;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The main purpose of this class is to make a graphical user interface of the Main application (Korean Learning vocabulary)
 * I have done, so far.  The structure is quite clear.
 *   Stage -> Scene -> VBox and
 *   VBox is a root node and it contains `englishHintLabel`, `modifiedKoreanHintLabel`,`messageLabel`, `wordLabel`, `guessInput`,
 *  `guessButton`, `imageView`, `textLabel` These are children of the root node
 */

public class KoreanTextGameFX extends Application {

    private KoreanTextGame game;
    private KoreanKeyboard koreanKeyboard = new KoreanKeyboard(); // Instance of KoreanKeyboard
    private Label wordLabel;
    private Label englishHintLabel = new Label();
    private Label modifiedKoreanHintLabel = new Label();
    private Label messageLabel;
    private TextField guessInput = new TextField(); // Correctly declare here for global access
    private int currentPosition = 0;
    private Button guessButton = new Button("Guess"); // make this global access then solve minor bug 1 too, hooray !

    @Override
    public void start(Stage primaryStage) {
        /*
         This image is to show how English keyboard are linking to Korean Keyboard letters by letters.
         Making them all into only using JavaFX is very unrealistic, but I can just show it as an image.
         */
        Image image = new Image("file:/Users/jarid/IdeaProjects/chimchak/src/main/java/com/example/chimchak/keyboard.PNG");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400); // Set height of the ImageView
        imageView.setFitWidth(500); // Set width of the ImageView
        imageView.setPreserveRatio(true); // Preserve the ratio of the Keyboard

        game = new KoreanTextGame("low"); // this is for the test

        VBox root = new VBox(20);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.CENTER);
        //try to fill new color
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label textLabel = new Label("(Korean keyboard naturally captures your inputs as Korean)");
        textLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: Blue;");

        messageLabel = new Label();
        wordLabel = new Label("Word: " + game.getDisplayWord());

        String[] hints = game.provideHint();
        englishHintLabel.setText("English meaning : " + hints[0]);
        englishHintLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: Green;");
        modifiedKoreanHintLabel.setText("Korean syntax : "+hints[1]);
        modifiedKoreanHintLabel.setStyle("-fx-font-size: 20px; -fx-test-fill: Blue;");

        guessInput.setPromptText("Enter the full word");
        setupGuessInputEventHandler();

        //Button guessButton = new Button("Guess");
        guessButton.setOnAction(e -> handleGuessAction());

        root.getChildren().addAll(englishHintLabel, modifiedKoreanHintLabel, messageLabel, wordLabel, guessInput, guessButton, imageView, textLabel);

        Scene scene = new Scene(root, 1150, 750);
        primaryStage.setTitle("Korean Vocabulary Learning Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Prepares the `guessInput` text field to handle typing, so users can type in Korean using an English keyboard.
     * It works with a `KoreanKeyboard` object, which changes English letter inputs into Korean letters directly in
     * the text field. This process makes typing Korean easy and natural, even if the user is only familiar with an
     * English keyboard.
     * Return: void
     * Results:
     * - The text field shows Korean letters as the user types.
     * - Keeps the typing cursor at the end of the entered text, ready for more letters. positionCaret ...
     * Important:
     * - This method uses a `KoreanKeyboard` object, an instance in Java representing the keyboard logic.
     *   Having an instance means this method can access the keyboard logic specifically prepared for our game,
     *   ensuring that typing works correctly.
     */
    public void setupGuessInputEventHandler() {
        guessInput.setOnKeyTyped(event -> {
            koreanKeyboard.handleKeyPress(event.getCharacter().charAt(0));
            // update the TextField with the current Hangul word
            guessInput.setText(koreanKeyboard.getCurrentWord().toString());
            // always move the caret to the end of the text
            guessInput.positionCaret(koreanKeyboard.getCurrentWord().length());
        });
    }

/*
    public void resetCurrentWord() {
        KoreanKeyboard.setCurrentWord(new StringBuilder("\0"));
       // currentWord.setLength(0);
    }

 */

    /**
     * This method try to handle guess action and it worked with the event handler (Guess button)
     * Return is void
     * Result : it shows getDisplayWord of the app and guessInput Text field is clear and KoreanKeyboard is also clear
     * if the guess from the user is correct, then Congratulation message would come and also englishHintLabel and
     * modifiedKoreanHintLabel will be set again for the next problem, if the guess from the user is not correct
     * then just wrong message.
     * Condition : two cons are coming - for the first one is on the above
     *            and the second one is checking whether wordsToGuess is empty or not
     *            if that is empty then, "Congratulation!"
     */
    public void handleGuessAction() {
       // Button guessButton = new Button("Guess");
        String guessedWord = guessInput.getText();
        boolean isCorrect = game.makeGuess(guessedWord);

        wordLabel.setText("Word: " + game.getDisplayWord());

        // Clear the TextField
        guessInput.clear();

        // Reset the KoreanKeyboard currentWord
        koreanKeyboard.resetCurrentWord();

        if (isCorrect) {
            messageLabel.setText("Great, Your answer is right!");
            //game.provideHint();
            String[] hints = game.provideHint();
            englishHintLabel.setText("English meaning : " + hints[0]);
            englishHintLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: Green;");
            modifiedKoreanHintLabel.setText("Korean syntax : "+hints[1]);
            modifiedKoreanHintLabel.setStyle("-fx-font-size: 20px; -fx-test-fill: Blue;");
        } else {
            messageLabel.setText("Wrong. Try again.");
        }

        if (game.wordsToGuess.isEmpty()) {
            messageLabel.setText("Congratulations! You've guessed all words.");
            guessInput.setDisable(true);
            guessButton.setDisable(true);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
