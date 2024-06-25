package com.example.chimchak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The KoreanTestGame class is designed to evaluate a user's level about Korean words.
 * It is very simple answering English words game in Korean syntax.
 * The classification of the user's level is based on the number of correct guess
 * more than 5 -> "High", more than 3 -> "Medium", and else -> "Low".
 * Depending on the user's level, Excel file (part) will be provided.
 * And there is an interesting point. The input from users in this part is English not Korean.
 * two reasons
 * a) in KoreanTestGame the Keyboard and KoreanKeyboard can not be used. Because it requires very specific
 * comparison, but KoreanTestGame can't read Excel file, so that requirement couldn't be fulfilled.
 * b) even simple English input in Korean context still be enough to test user's level to determine the right level.
 */

public class KoreanTestGame {
    private String [] arrayEnglishwords = {"comprehensively", "decision", "lives", "policy", "hard", "tickets", "read", "watch", "eat", "weather"};
    private String chosenWord;

    //public List<String> wordsToGuess = new ArrayList<>();  // for the testing with the Tester
    private List<String> wordsToGuess = new ArrayList<>();
    private List<String> correctlyGuessedWords = new ArrayList<>();
    private String[] blank;
    private int incorrectGuesses;

    private int correctGuesses;
    private String result;

    Scanner userInput = new Scanner(System.in);

    /**
     * Constructor initializes the game by shuffling the word array,
     * preparing the first word for guessing, and setting the initial value for counts of guesses.
     * Special characteristics : Uses 'randomArray' to shuffle words, ensuring a various game experience each time.
     */
    public KoreanTestGame() {
        randomArray(arrayEnglishwords);
        wordsToGuess.addAll(Arrays.asList(arrayEnglishwords));
        chosenWord = wordsToGuess.get(0);
        prepareForGuessing(chosenWord);
        incorrectGuesses = 0;
        correctGuesses = 0;
    }

    /**
     * Prepare the game for guessing a new word by resetting the blank and providing a hint for the word.
     * Parameter : word - The new word to guess.
     * Result : The hint for the current word is printed to the console.
     */
    public void prepareForGuessing(String word) {
        blank = new String[word.length()];
        Arrays.fill(blank, "_");

        String hint = getClueForWord(word);
        System.out.println("Hint: " + hint);
    }

    /**
     * Generates and returns a hint based on the given word.
     * Parameter : word - The word for which to generate a hint.
     * Result : Korean sentence(meaning) and English sentence(context) (else then no hint is given)
     * Special characteristics : Each word has specific link to each Korean sentence and English sentence
     * so it is more definitive style to linking English sentence - Korean sentence - Keyword(for the user to guess)
     * compared to the KoreanTextGame.java
     */
    public String getClueForWord(String word) {
        if ("comprehensively".equals(word)) {
            return "복잡한 사회적 현상을 분석할 때, 다양한 이론적 관점을 종합적으로 고려하는 것이 중요하다. \nWhen analyzing complex social phenomena, it is important to consider various theoretical perspectives ㅁㅁㅁㅁㅁㅁㅁㅁㅁ.";
        } else if ("decision".equals(word)) {
            return "경제학의 기본 원리를 이해하는 것은 현대 사회에서 경제적 의사결정을 내릴 때 필수적이다. \nUnderstanding the fundamental principles of economics is essential in making economic ㅁㅁㅁㅁㅁㅁ in modern society";
        } else if ("lives".equals(word)) {
            return "최근 기술의 발전으로 우리의 일상생활이 크게 변화하고 있다.\nWith the recent advancement of technology, our daily ㅁㅁㅁ have changed significantly.";
        } else if ("policy".equals(word)) {
            return "정부는 환경 보호를 위해 새로운 정책을 시행해야 한다고 주장하는 사람들이 많다.\nMany argue that the government should implement new ㅁㅁㅁ to protect the environment.";
        } else if ("hard".equals(word)) {
            return "그는 오랜 시간 동안 열심히 공부하여 마침내 그의 목표를 이루었다. \nHe studied ㅁㅁㅁ for a long time and finally achieved his goal.";
        } else if ("tickets".equals(word)) {
            return "여행 가기 전에, 호텔 예약과 비행기 티켓을 확인하는 것이 좋다.\nBefore you go on a trip, it's a good idea to check your hotel reservations and flight ㅁㅁㅁㅁㅁㅁ.";
        } else if ("read".equals(word)) {
            return "이 책은 매우 재미있어서 밤새도록 읽었다.\nI ㅁㅁㅁ this book all night because it was very interesting.";
        } else if ("watch".equals(word)) {
            return "친구와 함께 영화를 보러 갈 거예요. \nI am going to ㅁㅁㅁ the movie with my friend.";
        } else if ("eat".equals(word)) {
            return "오늘 저녁에 무엇을 먹고 싶어요? \nWhat do you want to ㅁㅁㅁ today's dinner ?";
        } else if ("weather".equals(word)) {
            return "날씨가 참 좋다.\nThe ㅁㅁㅁ is so good." ;
        }
        else {
            return "No specific clue available for this word.";
        }
    }


    /**
     * Randomizes the order of elements in the provided array using a 'shuffle' operation.
     * Parameter : array - The array to shuffle.
     * Result : The array is shuffled in place, and go back to array.
     * Special characteristics : Utilizes 'Collections.shuffle' to randomize the array, trying to make the game
     * unpredictable
     */
    public static void randomArray(String[] array) {
        List<String> tempList = Arrays.asList(array);
        Collections.shuffle(tempList);
        tempList.toArray(array);
    }

    /**
     * Processes the user's guess, comparing it to the chosen word, and updates the game state after that.
     * Parameter : guess - The user's guess.
     * Result : Returns true if the guess is correct, false otherwise.
     * Conditions : If the guess is incorrect, 'incorrectGuesses' is incremented.
     * If correct, 'correctlyGuessedWords' is updates and the next word is on the plate.
     * Special characteristics : Correct and incorrect guesses conclude the result and flow of this method
     */
    public boolean makeGuess(String guess) {
        if (!guess.equals(chosenWord)) {
            System.out.println("Wrong. Try again.");
            incorrectGuesses++;
            return false;
        } else {
            // Correct guess
            correctlyGuessedWords.add(chosenWord);
            wordsToGuess.remove(chosenWord);
            correctGuesses++;
            if (!wordsToGuess.isEmpty()) {
                chosenWord = wordsToGuess.get(0);
                prepareForGuessing(chosenWord);
            }
            return true;
        }
    }

    /**
     * Starts the game loop, accepting guesses until the user runs out of attempts or guesses all words correctly.
     * Result : The user's level is printed to the console at the end of the game.
     * Conditions : The game continues as long as there are remaining guesses and words to guess.
     * Special characteristics : This method encapsulates the main game logic and flow, including displaying prompts,
     * processing guesses, and concluding the game.
     */
    public void start() {
        chosenWord = wordsToGuess.get(0);
        prepareForGuessing(chosenWord);
        incorrectGuesses = 0;
        correctGuesses = 0;

        while (incorrectGuesses < 6 && !wordsToGuess.isEmpty()) {
            System.out.println("Guess the word: " + getDisplayWord());
            String guessText = userInput.next();

            if (makeGuess(guessText)) {
                System.out.println("Great, Your answer is right!");
                if (wordsToGuess.isEmpty()) {
                    System.out.println("Congratulations! You've guessed all words.");
                }
            } else {
                if (getRemainingGuesses() <= 0) {
                    System.out.println("Game Over. You've run out of guesses.");
                    break;
                }
            }
        }

        if (getCorrectGuesses() > 5){
            System.out.println("Setting result. Correct guesses: " + getCorrectGuesses());
            setResult("High");
            System.out.println("Result is going to be accessed. Current value: " + getResult());
        } else if (getCorrectGuesses() > 3){
            System.out.println("Setting result. Correct guesses: " + getCorrectGuesses());
            setResult("Medium");
            System.out.println("Result is going to be accessed. Current value: " + getResult());
        } else {
            System.out.println("Setting result. Correct guesses: " + getCorrectGuesses());
            setResult("Low");
            System.out.println("Result is going to be accessed. Current value: " + getResult());
        }

        //getUserLevel();
        System.out.println("You did a good job");
        System.out.println("This is your result: " + getResult());
    }

    public int getRemainingGuesses() {
        return 6 - incorrectGuesses;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDisplayWord() {
        StringBuilder display = new StringBuilder();
        for (String s : blank) {
            display.append(s).append(" ");
        }
        return display.toString();
    }
    public String getChosenWord() {
        return chosenWord;
    }

    /**
     * This method is for using in MainApplication.java
     * Result and Conditions are pretty much same, but
     * now it can be called in the MainApplication.java
     */
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
            if (s.equals("_")) {
                return false;
            }
        }
        return true;
    }
    
}

