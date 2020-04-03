import javax.swing.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NewEntry extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JComboBox<Entry.Type> typeComboBox;
    private JComboBox<Object> categoryComboBox;
    private JTextField amountTextField;
    private JTextField descriptionTextField;
    private JCheckBox recurringCheckBox;

    private boolean made;

    public NewEntry(List<Category> categories) {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
        setTitle("New Entry");
        pack();

        DefaultComboBoxModel<Object> categoryComboBoxModel = new DefaultComboBoxModel<>(categories.toArray());
        categoryComboBox.setModel(categoryComboBoxModel);

        DefaultComboBoxModel<Entry.Type> typeComboBoxModel = new DefaultComboBoxModel<>(Entry.Type.values());
        typeComboBox.setModel(typeComboBoxModel);

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    public Entry getEntry(){
        Entry newEntry = new Entry();
        newEntry.setAmount(new BigDecimal(amountTextField.getText()));
        newEntry.setDescription(descriptionTextField.getText());
        newEntry.setRecurring(recurringCheckBox.isSelected());
        newEntry.setTimeStamp(new Date());
        newEntry.setTransactionCategory((Category) categoryComboBox.getSelectedItem());
        newEntry.setType((Entry.Type) typeComboBox.getSelectedItem());
        return newEntry;
    }

    public boolean isMade(){
        return made;
    }

    public boolean isBigDecimal(JTextField amountTextField){
        try {
            new BigDecimal(amountTextField.getText());
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private void onOK() {
        if (categoryComboBox.getSelectedIndex() == -1){
            JOptionPane.showMessageDialog(null, "Please select a Category.");
            made = false;
        } else if (typeComboBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a Type.");
            made = false;
        } else if (!isBigDecimal(amountTextField)) {
            JOptionPane.showMessageDialog(null, "Please enter a valid Amount.");
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
