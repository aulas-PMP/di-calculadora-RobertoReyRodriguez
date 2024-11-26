import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CalculadoraGUI extends JFrame implements ActionListener, KeyListener, WindowListener {
    private Calculadora calculadora;
    private JTextField pantallaEntrada;
    private JLabel pantallaResultado;
    private StringBuilder entrada;
    private String modoEntrada;
    private JPanel panelModoEntrada;

    public CalculadoraGUI() {
        calculadora = new Calculadora();
        entrada = new StringBuilder();
        modoEntrada = "Libre";

        setTitle("Calculadora Rober");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelModoEntrada = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawString("Modo Actual: " + modoEntrada, 10, 20);
            }
        };
        panelModoEntrada.setPreferredSize(new Dimension(400, 40));
        add(panelModoEntrada, BorderLayout.NORTH);

        pantallaEntrada = new JTextField();
        pantallaEntrada.setEditable(false);
        pantallaEntrada.setHorizontalAlignment(JTextField.RIGHT);

        pantallaResultado = new JLabel("0", SwingConstants.RIGHT);

        JPanel panelPantalla = new JPanel(new GridLayout(2, 1));
        panelPantalla.add(pantallaEntrada);
        panelPantalla.add(pantallaResultado);

        JPanel panelNumeros = new JPanel(new GridLayout(4, 3));
        for (int i = 1; i <= 9; i++) {
            agregarBoton(panelNumeros, String.valueOf(i));
        }
        agregarBoton(panelNumeros, "0");
        agregarBoton(panelNumeros, ".");

        JPanel panelOperaciones = new JPanel(new GridLayout(5, 1));
        agregarBoton(panelOperaciones, "+");
        agregarBoton(panelOperaciones, "-");
        agregarBoton(panelOperaciones, "*");
        agregarBoton(panelOperaciones, "/");
        agregarBoton(panelOperaciones, "=");

        JPanel panelBotonLimpieza = new JPanel(new FlowLayout());
        agregarBoton(panelBotonLimpieza, "C");

        add(panelPantalla, BorderLayout.CENTER);
        add(panelNumeros, BorderLayout.WEST);
        add(panelOperaciones, BorderLayout.EAST);
        add(panelBotonLimpieza, BorderLayout.SOUTH);

        addKeyListener(this);
        addWindowListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void agregarBoton(JPanel panel, String nombre) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(this);
        panel.add(boton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!modoEntrada.equals("Ratón") && !modoEntrada.equals("Libre")) {
            return;
        }

        String input = e.getActionCommand();
        procesarEntrada(input);

        this.requestFocusInWindow();
    }

    private void procesarEntrada(String input) {
        switch (input) {
            case "+":
            case "-":
            case "*":
            case "/":
                String operando1Str = pantallaResultado.getText().replace(",", ".");
                calculadora.setOperando1(Double.parseDouble(operando1Str));
                calculadora.setOperacion(input);
                entrada.setLength(0);
                break;
            case "=":
                String operando2Str = pantallaResultado.getText().replace(",", ".");
                calculadora.setOperando2(Double.parseDouble(operando2Str));
                try {
                    Double resultado = calculadora.calcular(); 
                    pantallaResultado.setText(String.valueOf(resultado));
                } catch (ArithmeticException ex) {
                    pantallaResultado.setText("Error");
                }
                entrada.setLength(0);
                break;
            case "C":
                pantallaResultado.setText("0");
                entrada.setLength(0);
                break;
            case ".":
            case ",":
                if (!entrada.toString().contains(",")) {
                    entrada.append(",");
                    pantallaResultado.setText(entrada.toString());
                }
                break;
            default:
                entrada.append(input);
                pantallaResultado.setText(entrada.toString());
                break;
        }
    }
    

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_NUMPAD0:
            case KeyEvent.VK_NUMPAD1:
            case KeyEvent.VK_NUMPAD2:
            case KeyEvent.VK_NUMPAD3:
            case KeyEvent.VK_NUMPAD4:
            case KeyEvent.VK_NUMPAD5:
            case KeyEvent.VK_NUMPAD6:
            case KeyEvent.VK_NUMPAD7:
            case KeyEvent.VK_NUMPAD8:
            case KeyEvent.VK_NUMPAD9:
                procesarEntrada(String.valueOf(keyCode - KeyEvent.VK_NUMPAD0));
                break;
                case KeyEvent.VK_COMMA:
                case KeyEvent.VK_DECIMAL:
                procesarEntrada(",");
                break;
            case KeyEvent.VK_ADD:
                procesarEntrada("+");
                break;
            case KeyEvent.VK_SUBTRACT:
                procesarEntrada("-");
                break;
            case KeyEvent.VK_MULTIPLY:
                procesarEntrada("*");
                break;
            case KeyEvent.VK_DIVIDE:
                procesarEntrada("/");
                break;
            case KeyEvent.VK_ENTER:
                procesarEntrada("=");
                break;
            case KeyEvent.VK_BACK_SPACE:
                procesarEntrada("C");

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
        pantallaResultado.setText("0");
        entrada.setLength(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public void setModoEntrada(String modo) {
        this.modoEntrada = modo;
        panelModoEntrada.repaint();
        this.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI calculadoraGUI = new CalculadoraGUI();
            calculadoraGUI.setVisible(true);
            calculadoraGUI.setModoEntrada("Libre");
        });
    }
}
