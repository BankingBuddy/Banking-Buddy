import javax.swing.*;
import java.math.BigDecimal;

public class NewGoal extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField amountTextField;

    private boolean made;

    public NewGoal() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
        setTitle("New Goal");
        pack();

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    public Goal getGoal(){
        Goal newGoal = new Goal();
        newGoal.setGoalName(nameTextField.getText());
        newGoal.setGoalAmount(new BigDecimal(amountTextField.getText()));
        return newGoal;
    }

    public boolean isBigDecimal(JTextField amountTextField){
        try {
            new BigDecimal(amountTextField.getText());
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean isMade() {
        return made;
    }

    private void onOK() {
        if (!isBigDecimal(amountTextField)){
            JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
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
