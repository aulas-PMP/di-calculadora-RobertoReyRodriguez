public class Calculadora {
    private double operando1;
    private double operando2;
    private String operacion;

    public Calculadora() {
        operando1 = 0;
        operando2 = 0;
        operacion = "";
    }

    public void setOperando1(double operando1) {
        this.operando1 = operando1;
    }

    public void setOperando2(double operando2) {
        this.operando2 = operando2;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public double calcular() throws ArithmeticException {
        switch (operacion) {
            case "+":
                return operando1 + operando2;
            case "-":
                return operando1 - operando2;
            case "*":
                return operando1 * operando2;
            case "/":
                if (operando2 == 0) {
                    throw new ArithmeticException("División por cero");
                }
                return operando1 / operando2;
            default:
                throw new IllegalArgumentException("Operación no válida: " + operacion);
        }
    }
}
