package com.example.chimchak;

import java.util.*;
import javafx.application.Application;

public class MainApplication {
    public static void main(String[] args) {

        KoreanTestGame testGame = new KoreanTestGame();
        testGame.start();
        String userLevel = testGame.getUserLevel();

        String excelFilePath = "C:\\Users\\jarid\\OneDrive\\바탕 화면\\penguin\\programming\\KoreanTextGameData.xlsx";

        //KoreanTextGame textGame = new KoreanTextGame(excelFilePath, "Medium"); for testing
        KoreanTextGame textGame = new KoreanTextGame(excelFilePath, userLevel);
        textGame.initializeGame();
        Application.launch(KoreanTextGameFX.class);

      }
    }
