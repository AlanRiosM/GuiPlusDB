import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class acceso extends JFrame {
    private JPanel panelAcceso;
    private JButton verificarButton;
    private JTextField usuarioText;
    private JPasswordField contrasenaText;

    public acceso() {
        super("Acceso");
        setContentPane(panelAcceso);

        verificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conexion();
            }
        });
    }

    private void conexion() {
        String url = "jdbc:mysql://localhost:3306/capacitacion";
        String usuarioDB = "root";
        String contreasenaDB = "";

        try (Connection conexion = DriverManager.getConnection(url, usuarioDB, contreasenaDB)) {
            String usuario = usuarioText.getText();
            String contrasena = new String(contrasenaText.getPassword());
            iniciarSesion(conexion, usuario, contrasena);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(acceso.this, "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    private void iniciarSesion(Connection conexion, String usuario, String contrasena) {
        String query = "SELECT COUNT(*) FROM usuarios WHERE nombre = ? AND contraseña = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasena);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        JOptionPane.showMessageDialog(acceso.this, "Inicio de sesión exitoso");
                        crud crud = new crud();
                        crud.abrirCrud();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(acceso.this, "Nombre de usuario o contraseña incorrectos");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(acceso.this, "Error al iniciar sesión: " + e.getMessage());
        }
    }

    void abrirAcceso(){
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
