package matrixapp;

public class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final ComplexNumber[][] data;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = ComplexNumber.ZERO;
            }
        }
    }

    public ComplexMatrix(ComplexNumber[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new ComplexNumber[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    
    public ComplexNumber getValue(int r, int c) { return data[r][c]; }
    public void setValue(int r, int c, ComplexNumber val) { data[r][c] = val; }

    // Сложение
    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new MatrixException("Матрицы должны иметь одинаковый размер для сложения.");
        }
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setValue(i, j, this.data[i][j].add(other.data[i][j]));
            }
        }
        return result;
    }

    // Умножение
    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            throw new MatrixException("Количество столбцов первой матрицы должно быть равно количеству строк второй.");
        }
        ComplexMatrix result = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                ComplexNumber sum = ComplexNumber.ZERO;
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.data[i][k].multiply(other.data[k][j]));
                }
                result.setValue(i, j, sum);
            }
        }
        return result;
    }

    // Транспонирование
    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setValue(j, i, this.data[i][j]);
            }
        }
        return result;
    }

    // определитель
    public ComplexNumber determinant() {
        if (rows != cols) {
            throw new MatrixException("Определитель можно вычислить только для квадратной матрицы.");
        }
        return calcDeterminant(this.data);
    }

    private ComplexNumber calcDeterminant(ComplexNumber[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) {
            // (a*d) - (b*c)
            return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        }

        ComplexNumber det = ComplexNumber.ZERO;
        for (int j = 0; j < n; j++) {
            ComplexNumber[][] subMatrix = getSubMatrix(matrix, 0, j);
            ComplexNumber term = matrix[0][j].multiply(calcDeterminant(subMatrix));
            if (j % 2 == 1) {
                det = det.subtract(term);
            } else {
                det = det.add(term);
            }
        }
        return det;
    }

    private ComplexNumber[][] getSubMatrix(ComplexNumber[][] matrix, int excludingRow, int javaExcludingCol) {
        int n = matrix.length;
        ComplexNumber[][] sub = new ComplexNumber[n - 1][n - 1];
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == excludingRow) continue;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == javaExcludingCol) continue;
                sub[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }
        return sub;
    }

    // обратная матрица
    public ComplexMatrix inverse() {
        ComplexNumber det = determinant();
        if (det.real() == 0 && det.imaginary() == 0) {
            throw new MatrixException("Матрица вырождена (определитель равен 0), обратной матрицы не существует.");
        }

        ComplexMatrix adjugate = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ComplexNumber[][] sub = getSubMatrix(this.data, i, j);
                ComplexNumber subDet = calcDeterminant(sub);
                if ((i + j) % 2 == 1) {
                    subDet = ComplexNumber.ZERO.subtract(subDet);
                }
                adjugate.setValue(i, j, subDet);
            }
        }
        
        ComplexMatrix transposedAdj = adjugate.transpose();
        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setValue(i, j, transposedAdj.getValue(i, j).divide(det));
            }
        }
        return result;
    }

    // Деление
    public ComplexMatrix divide(ComplexMatrix other) {
        return this.multiply(other.inverse());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j].toString()).append("\t");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
