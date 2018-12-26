package de.toomuchcoffee.hitdice;

import de.toomuchcoffee.hitdice.domain.Hero;
import de.toomuchcoffee.hitdice.factories.HeroFactory;

import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        draw("Hello Hack and Stash player!");
        boolean createNewHero = confirm("Do you want to create a new hero?");
        if (createNewHero) {
            Hero hero = HeroFactory.create();
        }
    }

    public static void draw(String s, Object... args) {
        if (args.length == 0)
            System.out.println(s);
        else
            System.out.printf(s + System.lineSeparator(), args);
    }

    public static boolean confirm(String question) {
        System.out.print(question + " (y/n): ");
        return "y".equalsIgnoreCase(scanner.nextLine());
    }

    public static String dialog(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }
}
