package cr.ac.una.resourcemanager.view.login;

import cr.ac.una.resourcemanager.view.login.ControllerLogin;
import javax.swing.*;
import java.awt.event.*;

public class ViewLogin extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField id;
    private JTextField clave;

    private ControllerLogin controllerLogin;

    public ViewLogin() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setControllerLogin(ControllerLogin controller) {
        this.controllerLogin = controller;
    }

    public String getId(){
        return id.getText();
    }

    public String getClave(){
        return clave.getText();
    }

    private void onOK() {
        if(controllerLogin !=  null ){ //pedirle al controller que valide la opciónde cerrar
            controllerLogin.ingresar(getId(), getClave());

        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ViewLogin dialog = new ViewLogin();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error de Autenticación",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void setController(ControllerLogin controllerLogin) {
        this.controllerLogin = controllerLogin;
    }
}
