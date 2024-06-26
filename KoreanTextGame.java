package com.example.chimchak;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * KoreanTextGame class is designed to be the core for the whole Korean learning vocabulary app, focusing on
 * given English sentence meaning and Korean context. It adjusts the game's difficulty based on the user's level("Low,
 * "Medium", "High") by selecting appropriate content from an Excel file. It leverages the Apache POI library to dynamically
 * load game content from an Excel file, trying to provide learning experience according to the user's level.
 * What this class mainly do :
 * 1) Reading and bringing educational content from an Excel spreadsheet, including English meaning, Korean sentences,
 * and keywords for users to guess.
 * 2) Adjusting the difficulty of the game by selecting sheets based on the user's level, trying to personalize the learning path
 * 3) Managing the game state; tracking guessed words and providing hints
 */

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

    /**
     * Initializes the game without setting the user level. This constructor is less commonly used
     * and is intended for testing at first.
     * Parameter : filePath - The path to the Excel file containing English meaning and Korean context
     */
    public KoreanTextGame(String filePath) {
        initializeGame();
    }

    /**
     * Constructor to initiate the game based on the user's level. It reads the appropriate Excel sheet according to
     * the user's level and sets up the game.
     * Parameters : filePath - The path to the Excel file containing English meaning and Korean context
     *            userLevel - The level of the user ("Low", "Medium", "High").
     */
    public KoreanTextGame(String filePath, String userLevel) {
        readExcelFile(filePath, userLevel); // Read the Excel file based on userLevel
        initializeGame();
    }

    /**
     * Read contents from an Excel file. The method selects a sheet based on the user's level and loads
     * English sentences, Korean sentences, Keyword and direct comparisons consonant vowel.
     * Parameters : filePath - The path to the Excel file.
     *              userLevel - The level of the user ("Low", "Medium", "High").
     * Results : The app's data structures are regulated with content from the Excel file.
     * Conditions : Requires a valid Excel file path and user level to select the sheet.
     * Special Characteristics :
     *  This method requires the Apache POI library to function, as it handles the reading and interpretation of the Excel
     *  file format. Ensure that the library is included in the project's dependencies.
     *  Exceptions:
     *  Throws RuntimeException or specific checked exceptions related to file access and data parsing, highlighting the
     *  need for error handling when invoking this method. Callers should be prepared to catch and respond to these
     *  exceptions, ensuring a good user experience even when issues arise with the file reading process.
     */
    public static void readExcelFile(String filePath, String userLevel) {
        try (FileInputStream excelFile = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(excelFile);
            int modifyLevel = 0;
            if (Objects.equals(userLevel, "Low")) {
                modifyLevel = 0;
            } else if (Objects.equals(userLevel, "Medium")) {
                modifyLevel = 1;
            } else if (Objects.equals(userLevel, "High")) {
                modifyLevel = 2;
            } else {
                System.out.println("Something is wrong. Sorry");
                System.out.println("The basic setting will be Low level.");
            }
            Sheet datatypeSheet = workbook.getSheetAt(modifyLevel); // Use userLevel to select the sheet

            for (Row currentRow : datatypeSheet) {
                Cell englishCell = currentRow.getCell(0);
                Cell koreanCell = currentRow.getCell(1);
                Cell keywordCell = currentRow.getCell(2);
                Cell theAnswerCell = currentRow.getCell(3);

                String english = getCellValueAsString(englishCell);
                String korean = getCellValueAsString(koreanCell);
                String keyword = getCellValueAsString(keywordCell);
                String theAnswer = getCellValueAsString(theAnswerCell);

                KoreanTextGame.arrayEnglishSentences.add(english);
                KoreanTextGame.arrayKoreanSentences.add(korean);
                KoreanTextGame.wordsToGuess.add(keyword);
                KoreanTextGame.arrayTheAnswer.add(theAnswer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts cell content to a String, handling various data types (String, Numeric, Boolean).
     * Parameter : cell - The cell to extract the value from.
     * Return : the cell's content as a String representation
     * Special characteristics : Handles different cell types.
     * referencing : https://stackoverflow.com/questions/45691482/dateutil-iscelldateformattedcell-cell-not-working-in-java-p-o-i
     */
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

    /**
     * Prepares the game for a new round by choosing the first word from the list to guess.
     * Result : The app is ready for the user to make guesses.
     * Conditions : Assumes that there is at least one sentence availabe for guessing. (if empty) then it just prints
     * `No words available for the game.`
     */
    void initializeGame() {
        if (!arrayKoreanSentences.isEmpty()) {
            //Collections.shuffle(wordsToGuess);
            chosenWord = wordsToGuess.get(0);
            prepareForGuessing(chosenWord);
        } else {
            System.out.println("No words available for the game.");
        }
    }

    /**
     * Prepares the game for guessing a new word by resetting the blank and providing a hint.
     * Parameter : word - the new word to be guessed.
     * Result : Displays a hint and a series of □ representing the word to be guessed.
     */
    public void prepareForGuessing(String word) {
        blank = new String[word.length()];
        Arrays.fill(blank, "□");
        System.out.println("Guess the word: " + String.join(" ", blank));
        provideHint();
    }


    /**
     * Provides a hint for the current word to guess, indicating the context in which it is used.
     * Return : an array of two strings - the English sentence hint and the modified Korean sentence with blanks.
     * the reason why this modification is needed is that to link them in `handleGuessAction()` in FX class.
     * Result : print the hint and the modified sentence to the console.
     * Condition : The chosen word must be part of the current game vocabulary.
     */
    public String[] provideHint() {
        int index = -1;
        String [] result;
        result = new String[2];
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


    /* just getters do not solve this. >> public static List<String> wordsToGuesss = new ArrayList<>();
    I understand that if I make them private and also static
    still I can call and use them in other classes using getters.
    But, in this case wordsToGuess is called with the object.
    So private -> object (in the FX class) => conflicts
    non static -> this class => problem - at least I found it did not work, when I tried it earlier
    so only option for this at least public and also static at the same time

    public static List<String> getWordsToGuess() {
        return wordsToGuess;
    }

    private static List<String> wordsToGuess = new ArrayList<>();

    static is not object-friendly cause static does belong to the class not the individual objects
    non-static is object-friendly cause it belongs to each object.
    in week 11 class as long as I understood it
     */

    public String getDisplayWord() {
        StringBuilder display = new StringBuilder();
        for (String s : blank) {
            display.append(s).append(" ");
        }
        return display.toString().trim();
    }

    /**
     * Processes the player's guess and updates the game state accordingly.
     * This method compares the player's guess with the correct answer and provides feedback.
     * Parameter : guess - The player's guess
     * Return : true if the guess is correct, false otherwise.
     * Results:
     * - If the guess is correct, the word is removed from the list of words to guess, and the method returns true.
     * - If the guess is not correct, the method returns false and prompts the user to try again.
     * Conditions :
     * - The game continues with more words to guess unless all words have been correctly guessed,
     *  in which case a congratulation message is displayed.
     * Special characteristics:
     * - This method is central to the game's interactivity, directly responding
     *   to player input and driving the game's progression.
     * - Upon a correct guess, it automatically moves on to the next word,
     *   maintaining the game flow and engagement.
     */
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
}

