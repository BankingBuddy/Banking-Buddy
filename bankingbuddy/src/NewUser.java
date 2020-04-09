import javax.swing.*;
import java.math.BigDecimal;

public class NewUser extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField balanceTextField;

    private boolean made;

    public NewUser() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
        setTitle("Register");
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

    public boolean isBigDecimal(JTextField amountTextField){
        try {
            new BigDecimal(amountTextField.getText());
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public User getUser(){
        User newUser = new User();
        Wallet newWallet = new Wallet();

        String name = nameTextField.getText();
        BigDecimal balance = new BigDecimal(balanceTextField.getText());
        newWallet.setBalance(balance);

        newUser.setName(name);
        newUser.setWallet(newWallet);
        return newUser;
    }

    public boolean isMade() {
        return made;
    }

    private void onOK() {
        if (!isBigDecimal(balanceTextField)){
            JOptionPane.showMessageDialog(null, "Please enter a valid balance.");
            made = false;
        } else if (nameTextField.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please enter a name.");
            made = false;
        } else {
            made = true;
            dispose();
        }
    }

    private void onCancel() {
        made = false;
        dispose();
    }
}
