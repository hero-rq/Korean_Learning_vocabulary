package com.example.chimchak;

/**
 * An abstract class representing a generic keyboard. It provides a framework for handling key
 * presses and displaying the keyboard's status.
 * Special Characteristics:
 * - Abstract Class: This class is abstract and cannot be initiated on its own. It requires
 *   subclasses to provide implementations for the abstract method handleKeyPress, allowing
 *   for various keyboard layouts.
 * - Inheritance: Subclasses can inherit the display method, promoting code reuse and
 *   polymorphism. This enables subclasses possibly share or override functionality.
 *   In this case, the purpose of using abstract and inheritance is to prove that I can use these.
 */
public abstract class Keyboard {
    /**
     * Abstract method to be implemented by subclasses to handle key press events.
     * Parameter : key - the character representing the key that was pressed.
     */
    public abstract void handleKeyPress(char key);

    /** Displays a message indicating that the keyboard is ready for use.
     * This method can be inherited and used by subclasses as is or overridden for custom behavior.
     */
    public void display() {
        System.out.println("The Keyboard is ready.");
    }
}
