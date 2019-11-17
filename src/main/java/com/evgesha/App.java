package com.evgesha;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        People people = new People();
        people.startGame();
        people.createGeneration(5);
        System.out.println("\nVirtual world created. Welcome!\n");
        int y, year = -1;
        do {
            people.oneYearLater();
            people.printAll();
            System.out.println("\n" + ++year + " year is over.\nNext year...");
            Scanner s = new Scanner(System.in);
            y = s.nextInt();
        } while (y != -46486565);
    }
}