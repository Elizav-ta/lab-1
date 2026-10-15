package matrixapp;
public record ComplexNumber(double real, double imaginary) {

    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double r = this.real * other.real - this.imaginary * other.imaginary;
        double i = this.real * other.imaginary + this.imaginary * other.real;
        return new ComplexNumber(r, i);
    }

    public ComplexNumber divide(ComplexNumber other) {
        double denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator == 0) {
            throw new MatrixException("Деление на комплексный ноль невозможно.");
        }
        double r = (this.real * other.real + this.imaginary * other.imaginary) / denominator;
        double i = (this.imaginary * other.real - this.real * other.imaginary) / denominator;
        return new ComplexNumber(r, i);
    }

    @Override
    public String toString() {
        if (imaginary == 0) return String.format("%.2f", real);
        if (real == 0) return String.format("%.2fi", imaginary);
        return String.format("%.2f %s %.2fi", real, (imaginary < 0 ? "-" : "+"), Math.abs(imaginary));
    }
}
