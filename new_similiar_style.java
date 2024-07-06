// same function, different + similiar style 
// you don't need this to make it work but rewriting is fun for me 

package com.example.chimchak;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class KoreanTextGame {

    private static List<String> arrayKoreanSentences = new ArrayList<>();
    private static List<String> arrayEnglishSentences = new ArrayList<>();
    private static List<String> arrayTheAnswer = new ArrayList<>();
    private static String excelFilePath = "C:\\Users\\jarid\\OneDrive\\바탕 화면\\penguin\\programming\\KoreanTextGameData.xlsx";
    static List<String> wordsToGuess = new ArrayList<>();
    private List<String> correctlyGuessedWords = new ArrayList<>();
    private String chosenWord;
    private String checkingWord;
    private String[] blank;
    private int guessAttempts = 0;

    public KoreanTextGame(String filePath) {
        initializeGame();
    }

    public KoreanTextGame(String filePath, String userLevel) {
        readExcelFile(filePath, userLevel); // Read the Excel file based on userLevel
        initializeGame();
    }

    public static void readExcelFile(String filePath, String userLevel) {
        try (FileInputStream excelFile = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            int sheetIndex = getSheetIndex(userLevel);
            Sheet sheet = workbook.getSheetAt(sheetIndex); // Use userLevel to select the sheet

            for (Row row : sheet) {
                Cell englishCell = row.getCell(0);
                Cell koreanCell = row.getCell(1);
                Cell keywordCell = row.getCell(2);
                Cell answerCell = row.getCell(3);

                String english = getCellValueAsString(englishCell);
                String korean = getCellValueAsString(koreanCell);
                String keyword = getCellValueAsString(keywordCell);
                String answer = getCellValueAsString(answerCell);

                arrayEnglishSentences.add(english);
                arrayKoreanSentences.add(korean);
                wordsToGuess.add(keyword);
                arrayTheAnswer.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getSheetIndex(String userLevel) {
        switch (userLevel) {
            case "Low":
                return 0;
            case "Medium":
                return 1;
            case "High":
                return 2;
            default:
                return 0; // Default to Low level
        }
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    void initializeGame() {
        if (!arrayKoreanSentences.isEmpty()) {
            chosenWord = wordsToGuess.get(0);
            prepareForGuessing(chosenWord);
        } else {
            System.out.println("No words available for the game.");
        }
    }

    public void prepareForGuessing(String word) {
        blank = new String[word.length()];
        Arrays.fill(blank, "□");
        System.out.println("Guess the word: " + String.join(" ", blank));
        provideHint();
    }

    public String[] provideHint() {
        int index = -1;
        String[] result = new String[2];
        for (int i = 0; i < arrayKoreanSentences.size(); i++) {
            if (arrayKoreanSentences.get(i).contains(chosenWord)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            System.out.println("Hint: " + arrayEnglishSentences.get(index));

            String sentence = arrayKoreanSentences.get(index);
            String modifiedSentence = sentence.replace(chosenWord, "□".repeat(chosenWord.length()));
            result[0] = arrayEnglishSentences.get(index);
            result[1] = modifiedSentence;

            System.out.println(modifiedSentence);
            checkingWord = arrayTheAnswer.get(index);
        } else {
            System.out.println("No hint available.");
        }

        return result;
    }

    public boolean makeGuess(String guess) {
        guessAttempts++;
        if (guess.equals(checkingWord)) {
            correctlyGuessedWords.add(chosenWord);
            wordsToGuess.remove(chosenWord);
            System.out.println("Correct! The word was: " + chosenWord);
            if (!wordsToGuess.isEmpty()) {
                chosenWord = wordsToGuess.get(0);
                prepareForGuessing(chosenWord);
            } else {
                System.out.println("Congratulations! You've guessed all words.");
            }
            return true;
        } else {
            System.out.println("Incorrect. Try again.");
            return false;
        }
    }

    public String getDisplayWord() {
        StringBuilder display = new StringBuilder();
        for (String s : blank) {
            display.append(s).append(" ");
        }
        return display.toString().trim();
    }

    public int getCorrectGuesses() {
        return correctlyGuessedWords.size();
    }

    public String getUserLevel() {
        if (getCorrectGuesses() > 5) {
            return "High";
        } else if (getCorrectGuesses() > 3) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    public boolean isWordGuessed() {
        for (String s : blank) {
            if (s.equals("□")) {
                return false;
            }
        }
        return true;
    }
}

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
         Displaying an image to show the English keyboard linked to Korean Keyboard letters.
         Enhancing user understanding of the input method.
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
        // Setting background color
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label textLabel = new Label("(Korean keyboard naturally captures your inputs as Korean)");
        textLabel.setStyle("-fx-font-size: 17px; -fx-text-fill: Blue;");

        messageLabel = new Label();
        wordLabel = new Label("Word: " + game.getDisplayWord());

        String[] hints = game.provideHint();
        englishHintLabel.setText("English meaning : " + hints[0]);
        englishHintLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: Green;");
        modifiedKoreanHintLabel.setText("Korean syntax : " + hints[1]);
        modifiedKoreanHintLabel.setStyle("-fx-font-size: 20px; -fx-test-fill: Blue;");

        guessInput.setPromptText("Enter the full word");
        setupGuessInputEventHandler();

        guessButton.setOnAction(e -> handleGuessAction());

        // Adding all components to the root layout
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
     * Result: The text field shows Korean letters as the user types.
     */
    public void setupGuessInputEventHandler() {
        guessInput.setOnKeyTyped(event -> {
            koreanKeyboard.handleKeyPress(event.getCharacter().charAt(0));
            // Update the TextField with the current Hangul word
            guessInput.setText(koreanKeyboard.getCurrentWord().toString());
            // Always move the caret to the end of the text
            guessInput.positionCaret(koreanKeyboard.getCurrentWord().length());
        });
    }

    /**
     * Handles the guess action for the "Guess" button.
     * Provides feedback on the guess and updates the UI accordingly.
     * Resets the input field and the Korean keyboard for the next input.
     */
    public void handleGuessAction() {
        String guessedWord = guessInput.getText();
        boolean isCorrect = game.makeGuess(guessedWord);

        wordLabel.setText("Word: " + game.getDisplayWord());

        // Clear the TextField
        guessInput.clear();

        // Reset the KoreanKeyboard currentWord
        koreanKeyboard.resetCurrentWord();

        if (isCorrect) {
            messageLabel.setText("Great, Your answer is right!");
            String[] hints = game.provideHint();
            englishHintLabel.setText("English meaning : " + hints[0]);
            englishHintLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: Green;");
            modifiedKoreanHintLabel.setText("Korean syntax : " + hints[1]);
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

