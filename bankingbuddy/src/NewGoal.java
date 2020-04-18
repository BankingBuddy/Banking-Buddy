import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class NewGoal extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField amountTextField;
    private JDatePickerImpl datePicker;
    private JPanel dateSelectionPanel;

    private boolean made;

    public NewGoal() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(submitButton);
        setModal(true);
        setTitle("New Goal");
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        dateSelectionPanel.setLayout(new GridLayout(1,1));
        dateSelectionPanel.add(datePicker);
        pack();

        submitButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
    }

    private static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) {
            try {
                return dateFormatter.parseObject(text);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }

    public Goal getGoal(){
        Goal newGoal = new Goal();
        newGoal.setGoalName(nameTextField.getText());
        newGoal.setGoalAmount(new BigDecimal(amountTextField.getText()));
        newGoal.setCompleted(false);
        newGoal.setCompleteBy((Date) datePicker.getModel().getValue());
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

    public boolean isNewDate(){
        Date selectedDate = (Date) datePicker.getModel().getValue();
        Date currentDate = new Date();
        if (selectedDate.before(currentDate)){
            return false;
        }else{
            return true;
        }
    }

    public boolean isMade() {
        return made;
    }

    private void onOK() {
        if (!isBigDecimal(amountTextField)){
            JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
            made = false;
        } else if (nameTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a name.");
            made = false;
        }else if (datePicker.getJFormattedTextField().getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please select a date.");
            made = false;
        }else if (!isNewDate()){
            JOptionPane.showMessageDialog(null, "Please select a date in the future.");
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

    public JButton getSubmitButton(){
        return submitButton;
    }

    public void setNameTextField(String name){
        nameTextField.setText(name);
    }

    public void setAmountTextField(String amount){
        amountTextField.setText(amount);
    }

    public void setDatePicker(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        datePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getModel().setSelected(true);
    }
}
