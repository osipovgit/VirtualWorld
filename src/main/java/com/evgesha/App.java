package com.evgesha;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        People people = new People();
        people.startGame();
        System.out.println("\n\t  ═════════════════════════════════════\n\t  ╠  Virtual world created. Welcome!  ╣\n\t  ═════════════════════════════════════\n");
        int y, year = -1;
        do {
            people.oneYearLater();
//            people.printAll();
            people.printGodMode();
            System.out.println("\n╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶  ╶\n" + ++year + " year is over.\nNow we have " + people.food + " units of food.\nNext year...");
            Scanner s = new Scanner(System.in);
            y = s.nextInt();
        } while (y != -46486565);
    }
}