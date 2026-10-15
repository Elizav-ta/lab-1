package matrixapp;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ComplexMatrix current = null; // последний результат

    public static void main(String[] args) {
        
        while (true) {
            try {
                System.out.println("\nТекущая матрица в памяти: " + (current == null ? "пусто" : current.getRows() + "x" + current.getCols()));
                if (current != null) System.out.print(current);

                System.out.print("Меню: 1.Сложить 2.Умножить 3.Разделить 4.Транспонировать 5.Определитель 6.Сброс 7.Выход\nВыбор: ");
                int choice = Integer.parseInt(sc.nextLine().trim());
                
                if (choice == 7) break;

                switch (choice) {
                    case 1 -> {
                        ComplexMatrix b = readMatrix("B");
                        current = (current == null ? readMatrix("A") : current).add(b);
                    }
                    case 2 -> {
                        ComplexMatrix b = readMatrix("B");
                        current = (current == null ? readMatrix("A") : current).multiply(b);
                    }
                    case 3 -> {
                        ComplexMatrix b = readMatrix("B");
                        current = (current == null ? readMatrix("A") : current).divide(b);
                    }
                    case 4 -> {
                        current = (current == null ? readMatrix("A") : current).transpose();
                    }
                    case 5 -> {
                        ComplexMatrix a = (current == null ? readMatrix("A") : current);
                        System.out.println("det = " + a.determinant());
                    }
                    case 6 -> current = null;
                    default -> System.out.println("Неверный пункт.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите число.");
            } catch (MatrixException e) {
                System.out.println("Математическая ошибка: " + e.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String label) {
        System.out.printf("Ввод матрицы %s (строки столбцы): ", label);
        String[] size = sc.nextLine().trim().split("\\s+");
        int r = Integer.parseInt(size[0]);
        int c = Integer.parseInt(size[1]);

        ComplexMatrix matrix = new ComplexMatrix(r, c);
        System.out.println("Введите элементы (Real Imag) построчно:");
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                String[] parts = sc.nextLine().trim().split("\\s+");
                double real = Double.parseDouble(parts[0]);
                double imag = parts.length > 1 ? Double.parseDouble(parts[1]) : 0;
                matrix.setValue(i, j, new ComplexNumber(real, imag));
            }
        }
        return matrix;
    }
}
