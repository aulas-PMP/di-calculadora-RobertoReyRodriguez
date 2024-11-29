import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class CalculadoraGUI extends JFrame implements ActionListener, KeyListener, WindowListener {
    private Calculadora calculadora;
    private JLabel pantallaResultado;
    private JLabel pantallaAlmacenada; // Nueva pantalla para mostrar valor almacenado
    private StringBuilder entrada;
    private String modoEntrada;
    private JPanel panelModoEntrada;
    private JLabel labelModoEntrada;
    private Font fuentePersonalizada;

    public CalculadoraGUI() {
        calculadora = new Calculadora();
        entrada = new StringBuilder();
        modoEntrada = "Libre";

        setTitle("Calculadora Rober - Horda");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        setSize(width, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Cargar la fuente personalizada
        try {
            fuentePersonalizada = Font.createFont(Font.TRUETYPE_FONT, new File("src/fuente/lifecraftfont.ttf"))
                    .deriveFont(18f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(fuentePersonalizada);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            fuentePersonalizada = new Font("Arial", Font.BOLD, 18); // Fuente de respaldo
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                setLocationRelativeTo(null);
            }
        });

        // Configurar el panel de modo de entrada
        panelModoEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelModoEntrada = new JLabel("Modo Actual: Libre", SwingConstants.LEFT);
        labelModoEntrada.setForeground(Color.WHITE);
        labelModoEntrada.setFont(fuentePersonalizada);
        panelModoEntrada.add(labelModoEntrada);
        //panelModoEntrada.setPreferredSize(new Dimension(width, 40));
        panelModoEntrada.setBorder(BorderFactory.createEmptyBorder());
        add(panelModoEntrada, BorderLayout.NORTH);

        // Nueva pantalla para mostrar el valor almacenado y el modo de entrada
        pantallaAlmacenada = new JLabel("Valor Almacenado: 0   |   Modo Actual: Libre", SwingConstants.RIGHT);
        pantallaAlmacenada.setOpaque(true);
        pantallaAlmacenada.setBackground(Color.DARK_GRAY);
        pantallaAlmacenada.setForeground(Color.WHITE);
        pantallaAlmacenada.setFont(fuentePersonalizada.deriveFont(18f));
       // pantallaAlmacenada.setPreferredSize(new Dimension(width, 30));
        pantallaAlmacenada.setBorder(BorderFactory.createEmptyBorder());
        add(pantallaAlmacenada, BorderLayout.NORTH);

        // Configurar la pantalla de resultado
        pantallaResultado = new JLabel("0", SwingConstants.RIGHT) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = ImageIO.read(new File("src/imagen/logohorda.png"));
                    if (img != null) {
                        int imgWidth = getWidth();
                        int imgHeight = getHeight();
                        g.drawImage(img, 0, 0, imgWidth, imgHeight, this);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pantallaResultado.setOpaque(true);
        pantallaResultado.setBackground(Color.BLACK);
        pantallaResultado.setForeground(new Color(255, 0, 0));
        pantallaResultado.setFont(fuentePersonalizada.deriveFont(48f));
        //pantallaResultado.setPreferredSize(new Dimension(width, 50));
        pantallaResultado.setBorder(BorderFactory.createEmptyBorder());
        add(pantallaResultado, BorderLayout.CENTER);

        // Configurar los botones numéricos con la estética de la Horda
        JPanel panelNumeros = new JPanel(new GridLayout(4, 3));
        panelNumeros.setBorder(BorderFactory.createEmptyBorder());
        for (int i = 1; i <= 9; i++) {
            agregarBotonNumerico(panelNumeros, String.valueOf(i));
        }
        agregarBotonNumerico(panelNumeros, "0");
        agregarBotonNumerico(panelNumeros, ".");
        agregarBotonNumerico(panelNumeros, "C");

        // Configurar los botones de operaciones con la estética de la Horda
        JPanel panelOperaciones = new JPanel(new GridLayout(5, 1));
        panelOperaciones.setBorder(BorderFactory.createEmptyBorder());
        agregarBotonOperacion(panelOperaciones, "+");
        agregarBotonOperacion(panelOperaciones, "-");
        agregarBotonOperacion(panelOperaciones, "*");
        agregarBotonOperacion(panelOperaciones, "/");
        agregarBotonOperacion(panelOperaciones, "=");

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder());
        
        GridBagConstraints gbc = new GridBagConstraints();
        // Configuración para el panel de números
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCentral.add(panelNumeros, gbc);

        // Configuración para el panel de operaciones
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCentral.add(panelOperaciones, gbc);

        add(panelCentral, BorderLayout.SOUTH);

        addKeyListener(this);
        addWindowListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    private void agregarBotonNumerico(JPanel panel, String nombre) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(this);
        boton.setBackground(new Color(153, 0, 0));
        boton.setForeground(Color.WHITE);
        boton.setFont(fuentePersonalizada);
        boton.setBorder(new LineBorder(Color.BLACK, 1));
        panel.add(boton);
    }

    private void agregarBotonOperacion(JPanel panel, String nombre) {
        JButton boton = new JButton(nombre);
        boton.addActionListener(this);
        boton.setBackground(new Color(153, 0, 0));
        boton.setForeground(Color.WHITE);
        boton.setFont(fuentePersonalizada);
        boton.setBorder(new LineBorder(Color.BLACK, 1));
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
                pantallaAlmacenada.setText("Valor Almacenado: " + operando1Str);
                pantallaResultado.setText("0");
                entrada.setLength(0);
                break;
            case "=":
                String operando2Str = pantallaResultado.getText().replace(",", ".");
                calculadora.setOperando2(Double.parseDouble(operando2Str));
                try {
                    Double resultado = calculadora.calcular();
                    pantallaResultado.setText(String.valueOf(resultado).replace(".", ","));
                    pantallaAlmacenada.setText("Resultado: " + String.valueOf(resultado).replace(".", ","));
                    actualizarColorResultado(resultado);
                } catch (ArithmeticException ex) {
                    pantallaResultado.setText("Error");
                    pantallaResultado.setForeground(Color.RED);
                }
                entrada.setLength(0);
                break;
            case "C":
                pantallaResultado.setText("0");
                pantallaAlmacenada.setText("Valor Almacenado: 0");
                pantallaResultado.setForeground(Color.WHITE);
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

    private void actualizarColorResultado(double valor) {
        if (valor < 0) {
            pantallaResultado.setForeground(Color.RED);
        } else {
            pantallaResultado.setForeground(Color.WHITE);
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
        labelModoEntrada.setText("Modo Actual: " + modo);
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
