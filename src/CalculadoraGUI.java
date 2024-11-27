import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Clase que representa la interfaz gráfica de una calculadora.
 * Utiliza Java Swing para la representación visual de los componentes y 
 * gestiona las operaciones básicas de una calculadora.
 */
public class CalculadoraGUI extends JFrame implements ActionListener, KeyListener, WindowListener {
    private Calculadora calculadora;
    private JTextField pantallaEntrada;
    private JLabel pantallaResultado;
    private StringBuilder entrada;
    private String modoEntrada;
    private JPanel panelModoEntrada;

    /**
     * Constructor de la clase CalculadoraGUI.
     * Inicializa todos los componentes de la interfaz gráfica, define el tamaño de la ventana
     * y establece las acciones que se realizan en los diferentes eventos de usuario.
     */
    public CalculadoraGUI() {
        calculadora = new Calculadora();
        entrada = new StringBuilder();
        modoEntrada = "Libre";

        setTitle("Calculadora Rober");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        setSize(width, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                setLocationRelativeTo(null);
            }
        });

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

    /**
     * Agrega un botón al panel especificado.
     * 
     * @param panel El panel al que se agrega el botón.
     * @param nombre El texto que se mostrará en el botón.
     */
    private void agregarBoton(JPanel panel, String nombre) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(this);
        panel.add(boton);
    }

    /**
     * Maneja los eventos de acción de los botones.
     * 
     * @param e El evento de acción generado por el usuario.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!modoEntrada.equals("Ratón") && !modoEntrada.equals("Libre")) {
            return;
        }

        String input = e.getActionCommand();
        procesarEntrada(input);

        this.requestFocusInWindow();
    }

    /**
     * Procesa la entrada del usuario y actualiza el resultado según la operación ingresada.
     * 
     * @param input El valor ingresado por el usuario.
     */
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
                    actualizarColorResultado(resultado);
                } catch (ArithmeticException ex) {
                    pantallaResultado.setText("Error");
                    pantallaResultado.setForeground(Color.RED);  
                }
                entrada.setLength(0);
                break;
            case "C":
                pantallaResultado.setText("0");
                pantallaResultado.setForeground(Color.BLACK);  
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
                actualizarColorResultado(Double.parseDouble(pantallaResultado.getText().replace(",", ".")));
                break;
        }
    }

    /**
     * Actualiza el color del resultado en pantalla. Los números negativos se muestran en rojo.
     * 
     * @param valor El valor actual del resultado a mostrar.
     */
    private void actualizarColorResultado(double valor) {
        if (valor < 0) {
            pantallaResultado.setForeground(Color.RED);
        } else {
            pantallaResultado.setForeground(Color.BLACK);
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

    /**
     * Establece el modo de entrada (Ratón, Libre, etc.) y actualiza la interfaz para reflejar el modo actual.
     * 
     * @param modo El modo de entrada que se debe establecer.
     */
    public void setModoEntrada(String modo) {
        this.modoEntrada = modo;
        panelModoEntrada.repaint();
        this.requestFocusInWindow();
    }

    /**
     * Método principal para iniciar la calculadora.
     * 
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraGUI calculadoraGUI = new CalculadoraGUI();
            calculadoraGUI.setVisible(true);
            calculadoraGUI.setModoEntrada("Libre");
        });
    }
}
