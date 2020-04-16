import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
    private JTextField recurringTextField;

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

        recurringTextField.setText("Days...");
        recurringTextField.setForeground(new Color(102, 102, 102));
        recurringTextField.addFocusListener(focusListener());
    }

    public Entry getEntry(){
        Entry newEntry = new Entry();
        newEntry.setAmount(new BigDecimal(amountTextField.getText()));
        newEntry.setDescription(descriptionTextField.getText());
        newEntry.setRecurring(recurringCheckBox.isSelected());
        if (recurringCheckBox.isSelected()){
            newEntry.setRecurringInterval(Integer.parseInt(recurringTextField.getText()));
        }
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

    public boolean isInt(JTextField recurringTextField){
        try{
            Integer.parseInt(recurringTextField.getText());
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
        }else if (recurringCheckBox.isSelected() && !isInt(recurringTextField)){
            JOptionPane.showMessageDialog(null, "Please enter a valid integer number of Days.");
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

    private FocusListener focusListener(){
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (recurringTextField.getText().equals("Days...")){
                    recurringTextField.setText("");
                    recurringTextField.setForeground(new Color(51, 51, 51));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (recurringTextField.getText().equals("")){
                    recurringTextField.setText("Days...");
                    recurringTextField.setForeground(new Color(102, 102, 102));
                }
            }
        };
    }

    public JButton getSubmitButton(){
        return submitButton;
    }

    public void setTypeComboBox(Entry.Type type){
        typeComboBox.setSelectedItem(type);
    }

    public void  setCategoryComboBox(Object category){
        categoryComboBox.setSelectedItem(category);
    }

    public void setAmountTextField(String amount){
        amountTextField.setText(amount);
    }

    public void setDescriptionTextField(String description) {
        descriptionTextField.setText(description);
    }

    public void setRecurringCheckBox(boolean set){
        recurringCheckBox.setSelected(set);
    }

    public void setRecurringTextField(String days){
        recurringTextField.setText(days);
    }

    public JTextField getRecurringTextField() {
        return recurringTextField;
    }
}
