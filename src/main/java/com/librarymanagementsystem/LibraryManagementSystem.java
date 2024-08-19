package com.librarymanagementsystem;

import com.librarymanagementsystem.persistence.SessionManager;
import com.librarymanagementsystem.response.BookDetailsResponse;
import com.librarymanagementsystem.response.IssuedBookResponse;
import com.librarymanagementsystem.response.Response;
import com.librarymanagementsystem.response.ReturnBookResponse;
import org.hibernate.Session;

import java.util.Scanner;

import static com.librarymanagementsystem.BookAvailability.AVAILABLE;

public final class LibraryManagementSystem {

    private static final SessionManager SESSION_MANAGER = new SessionManager();
    private static UserUtil userUtil;
    private static BookUtil bookUtil;
    private static IssueBookUtil issueBookUtil;
    private static ReturnBookUtil returnBookUtil;
    private static Session session;

    static {
        SESSION_MANAGER.startSession();
        session = SESSION_MANAGER.getSession();
        userUtil = new UserUtil(session);
        bookUtil = new BookUtil(session);
        issueBookUtil = new IssueBookUtil(session);
        returnBookUtil = new ReturnBookUtil(session);
    }

    public static void main(String... args) {
        Scanner in = new Scanner(System.in);
        int choice = 0;

        while ((choice > -1) && (choice < 6)) {
            displayMenu();
            choice = in.nextInt();
            switch(choice) {
                case 1 -> addUser();
                case 2 -> addBook();
                case 3 -> searchBooks();
                case 4 -> issueBook();
                case 5 -> returnBook();
            }
        }
        session.close();
    }

    private static void displayMenu() {
        System.out.println("          *** Main Menu ***\n");
        System.out.println("          Options:\n");
        System.out.println("          1. Add User");
        System.out.println("          2. Add Book");
        System.out.println("          3. Search for a Book");
        System.out.println("          4. Issue Book");
        System.out.println("          5. Return Book");
        System.out.println("          6. Exit\n");
        System.out.print(" Enter option:");
    }

    private static void addUser() {
        Scanner in = new Scanner(System.in);
        String name = "";
        String phoneNumber = "";
        String emailAddress = "";
        String libraryCard = "";
        System.out.println("          *** Add User ***\n");
        System.out.print("    Enter name (required):");
        name = in.nextLine();
        System.out.print("\n    Enter phone number (required):");
        phoneNumber = in.nextLine();
        System.out.print("\n    Enter email address (optional - enter to skip):");
        String input = in.nextLine();
        if (!input.equals("\n")) {
            emailAddress = input;
        }
        System.out.print("\n    Enter library card (optional - enter to skip):");
        input = in.nextLine();
        if (!input.equals("\n")) {
            libraryCard = input;
        }
        Response response = (emailAddress.isEmpty() && libraryCard.isEmpty()) ? userUtil.addMinimalUser(name, phoneNumber)
                                                                              : userUtil.addCompleteUserDetails(name, phoneNumber, emailAddress, libraryCard);

        if (response.getCode() == 201) {
            System.out.println("\nUser " + name + " successfully created!\n");
        } else {
            System.out.println("\nUser " + name + " not created!  The following errors were returned:\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        }
    }

    private static void addBook() {
        Scanner in = new Scanner(System.in);
        String title = "";
        String author = "";
        String isbn = "";
        int publicationYear = 0;
        int copies = 1;
        System.out.println("          *** Add Book ***\n");
        System.out.print("    Enter title (required):");
        title = in.nextLine();
        System.out.print("\n    Enter author (required):");
        author = in.nextLine();
        System.out.print("\n    Enter isbn (required):");
        isbn = in.nextLine();
        System.out.print("\n    Enter publication year:");
        publicationYear = in.nextInt();
        System.out.print("\n    Enter number of copies (defaults to 1):");
        int input = in.nextInt();
        if (input > 1) {
            copies = input;
        }
        Response response = (copies > 1) ? bookUtil.addMultipleBooks(title, author, isbn, publicationYear, copies)
                                         : bookUtil.addSingleBook(title, author, isbn, publicationYear);

        if (response.getCode() == 201) {
            System.out.println("\nBook " + title + " successfully created!\n");
        } else {
            System.out.println("\nBook " + title + " not created!  The following errors were returned:\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        }
    }

    private static void searchBooks() {
        Scanner in = new Scanner(System.in);
        String title = "";
        String availability = "";
        System.out.println("          *** Search for a Book ***\n");
        System.out.print("    Enter title (required):");
        title = in.nextLine();
        BookDetailsResponse response = bookUtil.getBookDetails(title);

        if (response.getCode() == 200) {
            System.out.println("\nTitle: " + title);
            System.out.println("Author: " + response.getAuthor());
            if (response.getAvailability() == AVAILABLE) {
                availability = "Copies are available at this time;";
            } else {
                availability = "All copies are issued at this time;";
            }
            System.out.println("Availability: " + availability);
        } else if (response.getCode() == 404) {
            System.out.println("\n No book with title: " + title + " exists!");
        } else {
            System.out.println("\nThe following errors were returned:\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        }
    }

    private static void issueBook() {
        Scanner in = new Scanner(System.in);
        String title = "";
        String user = "";
        System.out.println("          *** Issue Book ***\n");
        System.out.print("    Enter title (required):");
        title = in.nextLine();
        System.out.print("\n    Enter user name (required):");
        user = in.nextLine();

        IssuedBookResponse response = issueBookUtil.issueBook(title, user);

        if (response.getCode() == 201) {
            System.out.println("\nBook " + title + " issued to " + user);
        } else if (response.getCode() == 410) {
            System.out.println("\nBook " + title + " not available at this time.");
        } else if (response.getCode() == 404) {
            System.out.println("\nBook " + title + " or user " + user + "not in system.\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println("\nAn error occurred:\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        }
    }

    private static void returnBook() {
        Scanner in = new Scanner(System.in);
        String title = "";
        String user = "";
        System.out.println("          *** Return Book ***\n");
        System.out.print("    Enter title (required):");
        title = in.nextLine();
        System.out.print("\n    Enter user name (required):");
        user = in.nextLine();

        ReturnBookResponse response = returnBookUtil.returnBook(title, user);

        if (response.getCode() == 200) {
            System.out.println("\nBook " + title + " issued to " + user + " has been returned.");
        } else if (response.getCode() == 404) {
            System.out.println("\nBook " + title + " or user " + user + "not in system, or book not issued.\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println("\nAn error occurred:\n");
            for (String errorMessage : response.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        }
    }

}
