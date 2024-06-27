package com.example.chimchak;

/**
 * This class represents a KoreanKeyboard, a specialized version of the abstract Keyboard class. It is designed
 * specifically for inputting Hangeul, the Korean alphabet(or characters). The key feature of this class is its method for handling
 * key presses, translating them into Hangul characters.
 * Key Features:
 * - Inheritance: KoreanKeyboard extends the Keyboard class to provide specific functionality for Korean character input,
 *   overriding the method that processes key presses.
 * - StringBuilder: Utilizes StringBuilder for dynamic Hangul character composition. Unlike immutable Strings,
 *   StringBuilder allows for efficient modifications (e.g., adding, removing, or changing characters), making it
 *   ideal for the interactive nature of typing.
 * - Static : Contains a static variable, currentWord, shared across all instances of KoreanKeyboard. This design
 *   supports scenarios where the typing context needs to be preserved across the application.
 */

public class KoreanKeyboard extends Keyboard {
    private static StringBuilder currentWord = new StringBuilder();
    private char lastChar = '\0'; // for tracking the last Hangeul character input

    /**
     * Handle a key press by mapping the pressed key to its corresponding Hangeul character and appending it to the
     * current word being typed.
     * Parameter : the 'key' that was pressed.
     */
    @Override
    public void handleKeyPress(char key) {
        char hangulCharacter = mapKeyToHangul(key);
        if (hangulCharacter == '\0') {
            return;
        }

        currentWord.append(hangulCharacter);
    }

    /**
     * Clears the current word. This method allows for resetting the input buffer,
     * effectively starting a new word. I found this is needed, every chance after the user inputted.
     */
    public void resetCurrentWord() {
        currentWord.setLength(0); // Clears the current word
    }

    /**
     * Returns the current word being typed as a string.
     * Return : A string representation of the current word.
     */
    public static String getCurrentWord() {
        return currentWord.toString();
    }

    /**
     * Sets the current word to a new value. This method allows for changing the input buffer.
     * Parameter : currentWord - The new value for the current word.
     * actually it is just because of getter and setters.. the force of habit.
     */
    public static void setCurrentWord(StringBuilder currentWord) {
        KoreanKeyboard.currentWord = currentWord;
    }

    /**
     * Maps a pressed key to its corresponding Hangeul character. This method defines the keyboard layout for converting
     * English alphabet key input into Hangeul characters.
     * Parameter : key - The character representing the key that was pressed.
     * Return : The corresponding Hangul character, or '\0`
     */
    public char mapKeyToHangul(char key) {
        switch (key) {
            case 'Q': return 'ㅃ';
            case 'W': return 'ㅉ';
            case 'E': return 'ㄸ';
            case 'R': return 'ㄲ';
            case 'T': return 'ㅆ';
            case 'O': return 'ㅒ';
            case 'P': return 'ㅖ';
            case 'q': return 'ㅂ';
            case 'w': return 'ㅈ';
            case 'e': return 'ㄷ';
            case 'r': return 'ㄱ';
            case 't': return 'ㅅ';
            case 'y': return 'ㅛ';
            case 'u': return 'ㅕ';
            case 'i': return 'ㅑ';
            case 'o': return 'ㅐ';
            case 'p': return 'ㅔ';
            case 'a': return 'ㅁ';
            case 's': return 'ㄴ';
            case 'd': return 'ㅇ';
            case 'f': return 'ㄹ';
            case 'g': return 'ㅎ';
            case 'h': return 'ㅗ';
            case 'j': return 'ㅓ';
            case 'k': return 'ㅏ';
            case 'l': return 'ㅣ';
            case 'z': return 'ㅋ';
            case 'x': return 'ㅌ';
            case 'c': return 'ㅊ';
            case 'v': return 'ㅍ';
            case 'b': return 'ㅠ';
            case 'n': return 'ㅜ';
            case 'm': return 'ㅡ';
            case '/': return '?';
            default: return '\0'; // Use '\0' for unmapped keys (unexpected inputs) so this is quite strict
        }
    }


    /**
     * Replaces the last character in the current word. This method can be used for handling special input cases.
     * The idea behind this method is very related to the nature of Korean words.
     * But, I think I do not use this. so no specific descriptions can be fine.
     */
    public void replaceLastCharacter(char newChar) {
        if (currentWord.length() > 0) {
            currentWord.setCharAt(currentWord.length() - 1, newChar);
        }
    }

}
