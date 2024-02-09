import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class crud extends JFrame{
    private JTextField usuarioText;
    private JTextField codigoText;
    private JTextField cedulaText;
    private JPasswordField contrasenaText;
    private JComboBox rolText;
    private JPanel panelCrud;
    private JButton registrarButton;
    private JButton visualizarButton;

    public crud() {
        super("Crud");
        setContentPane(panelCrud);
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conexion(1);
            }
        });
        visualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conexion(2);
            }
        });
    }

    void conexion(int opcion){
        String url = "jdbc:mysql://localhost:3306/capacitacion";
        String usuarioDB = "root";
        String contreasenaDB = "";

        try(Connection conexion = DriverManager.getConnection(url, usuarioDB, contreasenaDB)){
            String usuario = usuarioText.getText();
            String codigo = codigoText.getText();
            String cedula = cedulaText.getText();
            String contrasena = contrasenaText.getText();
            String rol = (String) rolText.getSelectedItem();
            if(opcion == 1){
                registrarDatos(conexion, usuario, codigo, cedula, contrasena, rol);
            } else {
                visualizarDatos(conexion, cedula);
            }

        } catch (SQLException e){
            JOptionPane.showMessageDialog(crud.this, "Conexión no exitosa \n" + e);
        }
    }

    void registrarDatos(Connection conexion, String nombre, String codigo, String cedula, String contrasena, String rol){
        String query = "INSERT INTO usuarios (nombre, codigo, cedula, contraseña, rol) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, codigo);
            preparedStatement.setString(3, cedula);
            preparedStatement.setString(4, contrasena);
            preparedStatement.setString(5, rol);
            int filasInsertadas = preparedStatement.executeUpdate();
            if (filasInsertadas>0){
                JOptionPane.showMessageDialog(crud.this, "Datos insertados correctamentes");
            } else {
                JOptionPane.showMessageDialog(crud.this, "No se han insertado datos");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(crud.this, "Error al ejecutar la consulta SQL \n "+ e);
        }
    }

    void visualizarDatos(Connection conexion, String cedula) {
        String query = "SELECT * FROM usuarios WHERE cedula = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, cedula);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    String codigo = resultSet.getString("codigo");
                    String rol = resultSet.getString("rol");

                    JOptionPane.showMessageDialog(null, "Nombre: " + nombre + "\nCódigo: " + codigo + "\nCedula: " + cedula + "\nRol: " + rol);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ejecutar la consulta SQL \n" + e);
        }
    }


    void abrirCrud(){
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
