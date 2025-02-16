import java.util.*;

public class Lab_2 {
    private final Scanner in = new Scanner(System.in);

    public String stringInput() {
        System.out.print("Введите строку:\n");
        return in.nextLine();
    }

    public int intInput() {
        System.out.print("Введите число: ");
        while (!in.hasNextInt()) {
            System.out.println("Необходимо ввести числовое значение");
            in.next();
        }
        return in.nextInt();
    }

    public int[] intArrayInput() {
        System.out.print("Введите количество чисел массива: ");
        int n = intInput();
        int[] inputArray = new int[n];
        System.out.print("Введите элементы массива: ");
        for (int i = 0; i < n; i++) {
            inputArray[i] = intInput();
        }
        return inputArray;
    }

    public int[][] twoDimensionalIntArrayInput() {
        System.out.print("Введите количество строк: ");
        int rows = intInput();
        System.out.print("Введите количество столбцов: ");
        int columns = intInput();
        int[][] array = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            System.out.print((i + 1) + ": ");
            for (int j = 0; j < columns; j++) {
                array[i][j] = intInput();
            }
        }
        return array;
    }

    public String task1(String input) {
        Set<Character> seen = new HashSet<>();
        StringBuilder maxSubstring = new StringBuilder();
        StringBuilder currentSubstring = new StringBuilder();

        for (char c : input.toCharArray()) {
            while (seen.contains(c)) {
                seen.remove(currentSubstring.charAt(0));
                currentSubstring.deleteCharAt(0);
            }
            seen.add(c);
            currentSubstring.append(c);
            if (currentSubstring.length() > maxSubstring.length()) {
                maxSubstring = new StringBuilder(currentSubstring);
            }
        }
        return maxSubstring.toString();
    }

    public String task2(int[] firstArray, int[] secondArray) {
        int[] mergedArray = new int[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, mergedArray, 0, firstArray.length);
        System.arraycopy(secondArray, 0, mergedArray, firstArray.length, secondArray.length);
        Arrays.sort(mergedArray);
        return Arrays.toString(mergedArray);
    }

    public String task3(int[] numbers) {
        int maxSum = Integer.MIN_VALUE, currentSum = 0;
        for (int num : numbers) {
            currentSum = Math.max(num, currentSum + num);
            maxSum = Math.max(maxSum, currentSum);
        }
        return String.valueOf(maxSum);
    }

    public String task4(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = matrix[i][j];
            }
        }
        return Arrays.deepToString(rotated);
    }

    public String task5(int[] numbers, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int num : numbers) {
            if (seen.containsKey(target - num)) {
                return num + " " + (target - num);
            }
            seen.put(num, 1);
        }
        return null;
    }

    public String task6(int[][] matrix) {
        return String.valueOf(Arrays.stream(matrix).flatMapToInt(Arrays::stream).sum());
    }

    public String task7(int[][] matrix) {
        return Arrays.toString(Arrays.stream(matrix).mapToInt(row -> Arrays.stream(row).max().orElse(Integer.MIN_VALUE)).toArray());
    }

    public String task8(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[cols - 1 - j][i] = matrix[i][j];
            }
        }
        return Arrays.deepToString(rotated);
    }
}
