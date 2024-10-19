import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MetodoMinimizacion extends JFrame {
    private JTable tablaCostos;
    private JButton botonCalcular;
    private JTextArea areaResultado;
    private JTextField campoTareas;
    private JTextField campoTrabajadores;

    public MetodoMinimizacion() {
        setTitle("Método de Asignación por Minimización");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel para ingresar número de tareas y trabajadores
        JPanel panelIngreso = new JPanel();
        panelIngreso.setLayout(new GridLayout(3, 2, 10, 10));
        panelIngreso.setBorder(BorderFactory.createTitledBorder("Ingrese Datos"));

        panelIngreso.add(new JLabel("Número de Tareas:"));
        campoTareas = new JTextField(5);
        panelIngreso.add(campoTareas);

        panelIngreso.add(new JLabel("Número de Trabajadores:"));
        campoTrabajadores = new JTextField(5);
        panelIngreso.add(campoTrabajadores);

        JButton botonCrearTabla = new JButton("Crear Tabla");
        botonCrearTabla.addActionListener(e -> crearTabla());
        panelIngreso.add(botonCrearTabla);

        add(panelIngreso, BorderLayout.NORTH);

        // Tabla para los costos
        tablaCostos = new JTable();
        add(new JScrollPane(tablaCostos), BorderLayout.CENTER);

        // Panel para el botón de calcular
        JPanel panelBoton = new JPanel();
        botonCalcular = new JButton("Calcular Asignación");
        botonCalcular.addActionListener(e -> calcularAsignacion());
        panelBoton.add(botonCalcular);
        add(panelBoton, BorderLayout.SOUTH);

        // Área de texto para mostrar resultados
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setBorder(BorderFactory.createTitledBorder("Resultados"));
        add(new JScrollPane(areaResultado), BorderLayout.EAST);
    }

    private void crearTabla() {
        int tareas = Integer.parseInt(campoTareas.getText());
        int trabajadores = Integer.parseInt(campoTrabajadores.getText());

        DefaultTableModel modelo = new DefaultTableModel(tareas, trabajadores);
        tablaCostos.setModel(modelo);

        for (int i = 0; i < trabajadores; i++) {
            tablaCostos.getColumnModel().getColumn(i).setHeaderValue("Trabajador " + (i + 1));
        }

        for (int i = 0; i < tareas; i++) {
            modelo.setValueAt("0", i, 0); // Inicializa con 0
        }

        areaResultado.setText("");
    }

    private void calcularAsignacion() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCostos.getModel();
        int filas = modelo.getRowCount();
        int columnas = modelo.getColumnCount();
        int[][] costos = new int[filas][columnas];

        // Leer datos de la tabla
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Object valor = modelo.getValueAt(i, j);
                costos[i][j] = (valor != null && !valor.toString().isEmpty()) ? Integer.parseInt(valor.toString()) : 0;
            }
        }

        // Resolver el problema de asignación
        int[] asignacion = algoritmoHungaro(costos);
        mostrarResultado(asignacion);
    }

    private void mostrarResultado(int[] asignacion) {
        StringBuilder resultado = new StringBuilder("Asignación óptima:\n");
        for (int i = 0; i < asignacion.length; i++) {
            resultado.append("Tarea ").append(i + 1).append(" asignada a trabajador ").append(asignacion[i] + 1).append("\n");
        }
        areaResultado.setText(resultado.toString());
    }

    private int[] algoritmoHungaro(int[][] costos) {
        int n = costos.length;
        boolean[] asignado = new boolean[n];
        int[] resultado = new int[n];

        for (int i = 0; i < n; i++) {
            int minCost = Integer.MAX_VALUE;
            int minIndex = -1;

            for (int j = 0; j < n; j++) {
                if (!asignado[j] && costos[i][j] < minCost) {
                    minCost = costos[i][j];
                    minIndex = j;
                }
            }
            resultado[i] = minIndex;
            asignado[minIndex] = true;
        }
        return resultado;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MetodoMinimizacion ventana = new MetodoMinimizacion();
            ventana.setVisible(true);
        });
    }
}