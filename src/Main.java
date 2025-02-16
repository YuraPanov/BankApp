import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        runLab02();
    }

    static void runLab02() {
        Lab_2 tasks = new Lab_2();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Task 1: " + tasks.task1(tasks.stringInput()));

        System.out.println("Task 2: " + tasks.task2(tasks.intArrayInput(), tasks.intArrayInput()));

        System.out.println("Task 3: " + tasks.task3(tasks.intArrayInput()));

        System.out.println("Task 4: " + tasks.task4(tasks.twoDimensionalIntArrayInput()));

        System.out.println("Task 5: " + tasks.task5(tasks.intArrayInput(), tasks.intInput()));

        System.out.println("Task 6: " + tasks.task6(tasks.twoDimensionalIntArrayInput()));

        System.out.println("Task 7: " + tasks.task7(tasks.twoDimensionalIntArrayInput()));

        System.out.println("Task 8: " + tasks.task8(tasks.twoDimensionalIntArrayInput()));

        scanner.close();
    }
}
