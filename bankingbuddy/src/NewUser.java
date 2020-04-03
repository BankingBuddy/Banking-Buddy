import javax.swing.*;
import java.math.BigDecimal;

public class NewUser extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField balanceTextField;

    public NewUser() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
        pack();

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    public String getName(){
        return nameTextField.getText();
    }

    public BigDecimal getBalance(){
        return new BigDecimal(balanceTextField.getText());
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
