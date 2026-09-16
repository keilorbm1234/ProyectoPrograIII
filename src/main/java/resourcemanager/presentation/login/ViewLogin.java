package resourcemanager.presentation.login;

import resourcemanager.presentation.FormUiLoader;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

public class ViewLogin extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField Id;
    private JTextField clave;

    private ControllerLogin controllerLogin;

    public ViewLogin() {
        FormUiLoader.load(this);
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


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonOK.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/ok.png"))));
        buttonCancel.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/cancel.png"))));
    }

    public void setControllerLogin(ControllerLogin controller) {
        this.controllerLogin = controller;
    }

    public String getId(){
        return Id.getText();
    }

    public String getClave(){
        return clave.getText();
    }

    private void onOK() {
        if(controllerLogin !=  null ){ //pedirle al controller que valide la opciónde cerrar
            String id = getId().trim();
            String clave = getClave().trim();

            if(id.isEmpty() || clave.isEmpty()){
                mostrarError("Debe ingresar un ID y una clave.");
                return;
            }
            controllerLogin.ingresar(id, clave);
        }
    }

    private void onCancel() {
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
