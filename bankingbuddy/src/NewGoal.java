import javax.swing.*;
import java.math.BigDecimal;

public class NewGoal extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField amountTextField;

    public NewGoal() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
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

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
