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

    public NewEntry(List<Category> categories) {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
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
        System.out.println(descriptionTextField.getText());
        newEntry.setAmount(new BigDecimal(amountTextField.getText()));
        newEntry.setDescription(descriptionTextField.getText());
        newEntry.setRecurring(recurringCheckBox.isSelected());
        newEntry.setTimeStamp(new Date());
        newEntry.setTransactionCategory((Category) categoryComboBox.getSelectedItem());
        newEntry.setType((Entry.Type) typeComboBox.getSelectedItem());
        return newEntry;
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
