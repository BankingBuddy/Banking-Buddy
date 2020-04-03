import javax.swing.*;

public class NewCategory extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;

    private boolean made;

    public NewCategory() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submitButton);
        setTitle("New Category");
        pack();

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    public Category getCategory(){
        return new Category(nameTextField.getText());
    }

    public boolean isMade() {
        return made;
    }

    private void onOK() {
        if (nameTextField.getText().isEmpty()){
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
