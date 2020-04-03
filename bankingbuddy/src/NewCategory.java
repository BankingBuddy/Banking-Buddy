import javax.swing.*;

public class NewCategory extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;

    public NewCategory() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submitButton);
        pack();

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    public Category getCategory(){
        return new Category(nameTextField.getText());
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
