import java.util.Scanner;

interface TimetableOperations {
    void displayTimetable();
    void addSubject(int day, int period, String subject);
    void removeSubject(int day, int period);
    void modifySubject(int day, int period, String newSubject);
}

class Timetable implements TimetableOperations {
    private String[][] timetable;
    public Timetable(int days, int periods) {
        timetable = new String[days][periods];
    }

    @Override
    public void displayTimetable() {
        System.out.println("Timetable:");

        System.out.print("\t\t");
        for (int j = 0; j < timetable[0].length; j++) {
            System.out.print("Slot " + (j + 1) +"\t");
        }
        System.out.println();

        for (int i = 0; i < timetable.length; i++){
            System.out.print("Day " + (i + 1) + ":\t");
            for (int j = 0; j < timetable[i].length; j++) {
                System.out.print(timetable[i][j]+"\t\t");
            }
            System.out.println();
        }
    }
    private void validateInput(int day, int period) {
        if (day < 1 || day > timetable.length || period < 1 || period > timetable[0].length) {
            throw new IllegalArgumentException("Invalid day or period.");
        }
    }
    @Override
    public void addSubject(int day, int period, String subject) {
        try {
            validateInput(day, period);
            timetable[day - 1][period - 1] = subject;
            System.out.println("Subject added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void removeSubject(int day, int period) {
        try {
            validateInput(day, period);
            timetable[day - 1][period - 1] = null;
            System.out.println("Subject removed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void modifySubject(int day, int period, String newSubject) {
        try {
            validateInput(day, period);
            timetable[day - 1][period - 1] = newSubject;
            System.out.println("Subject modified successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class TimetableManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Timetable Management System!");

        System.out.print("Enter the number of days in the timetable: ");
        int days = scanner.nextInt();

        System.out.print("Enter the number of periods in a day: ");
        int periods = scanner.nextInt();

        TimetableOperations timetable = new Timetable(days, periods);

        int choice;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Display Timetable");
            System.out.println("2. Add Subject");
            System.out.println("3. Remove Subject");
            System.out.println("4. Modify Subject");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    timetable.displayTimetable();
                    break;
                case 2:
                    try {
                        System.out.print("Enter the day (1-" + days + "): ");
                        int day = scanner.nextInt();
                        System.out.print("Enter the period (1-" + periods + "): ");
                        int period = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter the subject in 3 letter only...");
                        System.out.print("Enter the subject: ");
                        String subject = scanner.nextLine();
                        timetable.addSubject(day, period, subject);
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                        scanner.nextLine();
                    }
                    break;
                case 3:
                    try {
                        System.out.print("Enter the day (1-" + days + "): ");
                        int day = scanner.nextInt();
                        System.out.print("Enter the period (1-" + periods + "): ");
                        int period = scanner.nextInt();
                        timetable.removeSubject(day, period);
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                        scanner.nextLine();
                    }
                    break;
                case 4:
                    try {
                        System.out.print("Enter the day (1-" + days + "): ");
                        int day = scanner.nextInt();
                        System.out.print("Enter the period (1-" + periods + "): ");
                        int period = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter the new subject in 3 letter only...");
                        System.out.print("Enter the new subject: ");
                        String newSubject = scanner.nextLine();
                        timetable.modifySubject(day, period, newSubject);
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                        scanner.nextLine();
                    }
                    break;
                case 5:
                    System.out.println("Exiting the Timetable Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
