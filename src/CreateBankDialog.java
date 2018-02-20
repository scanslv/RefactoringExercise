import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class CreateBankDialog extends JFrame {
    private final static int TABLE_SIZE = 29;
    private final String[] accounts = new String[]{"Current", "Deposit"};
    private Random rand = new Random();
    private HashMap<Integer, BankAccount> table = new HashMap<>();

    private JTextField accountNumberTextField;
    private JTextField firstNameTextField;
    private JTextField surnameTextField;
    private JTextField balanceTextField, overdraftTextField;
    private JComboBox comboBox;

    private String accountNumber;
    private String surname;
    private String firstName;
    private String accountType;

    CreateBankDialog(HashMap<Integer, BankAccount> accounts) {
        super("Add Bank Details");
        table = accounts;
        setLayout(new BorderLayout());

        setUpTextFields();
        add(setUpDataPanel(), BorderLayout.CENTER);
        add(setUpButtonPanel(), BorderLayout.SOUTH);

        setSize(400, 800);
        pack();
        setVisible(true);
    }

    private void setUpTextFields(){
        final int nrOfColumns = 15;
        final int nrOfCurrencyColumns = 10;

        accountNumberTextField = new JTextField(nrOfColumns);
        accountNumberTextField.setEditable(true);

        surnameTextField = new JTextField(nrOfColumns);
        surnameTextField.setEditable(true);

        firstNameTextField = new JTextField(nrOfColumns);
        firstNameTextField.setEditable(true);

        comboBox = new JComboBox<>(accounts);

        balanceTextField = new JTextField(nrOfCurrencyColumns);
        balanceTextField.setText("0.0");
        balanceTextField.setEditable(false);

        overdraftTextField = new JTextField(nrOfCurrencyColumns);
        overdraftTextField.setText("0.0");
        overdraftTextField.setEditable(false);
    }

    private JPanel setUpDataPanel() {
        JPanel dataPanel = new JPanel(new MigLayout());

        addLabelAndTextField(dataPanel, "Account Number: ", accountNumberTextField);
        addLabelAndTextField(dataPanel, "Last Name: ",surnameTextField);
        addLabelAndTextField(dataPanel, "First Name: ", firstNameTextField);
        addLabelAndTextField(dataPanel, "Account Type: ", comboBox);
        addLabelAndTextField(dataPanel, "Balance: ", balanceTextField);
        addLabelAndTextField(dataPanel, "Overdraft: ", overdraftTextField);

        return dataPanel;
    }

    private void addLabelAndTextField(JPanel jPanel, String label, Component component){
        final String labelConstraints = "growx, pushx";
        final String textFieldConstraints = "growx, pushx, wrap";
        jPanel.add(new JLabel(label), labelConstraints);
        jPanel.add(component, textFieldConstraints);
    }

    private JPanel setUpButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(setUpAddButton());
        buttonPanel.add(setUpCancelButton());

        return buttonPanel;
    }

    private JButton setUpAddButton() {
        JButton addButton = new JButton("Add");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    try {
                        boolean accNumTaken = false;
                        int randNumber = rand.nextInt(24) + 1;

                        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                            while (randNumber == entry.getValue().getAccountID()) {
                                randNumber = rand.nextInt(24) + 1;
                            }
                        }
                        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                            if (entry.getValue().getAccountNumber().trim().equals(accountNumberTextField.getText())) {
                                accNumTaken = true;
                            }
                        }
                        if (!accNumTaken) {
                            BankAccount account = new BankAccount(randNumber, accountNumber, surname, firstName, accountType, 0.0, 0.0);
                            int key = Integer.parseInt(account.getAccountNumber());
                            int hash = (key % TABLE_SIZE);

                            while (table.containsKey(hash)) {
                                hash = hash + 1;
                            }
                            table.put(hash, account);
                        } else {
                            JOptionPane.showMessageDialog(null, "Account Number must be unique");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Number format exception");
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Please make sure all fields have values, and Account Number is a unique 8 digit number");
                dispose();
            }
        });
        return addButton;
    }

    private JButton setUpCancelButton() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return cancelButton;
    }

    private void getValues() {
        accountNumber = accountNumberTextField.getText();
        surname = surnameTextField.getText();
        firstName = firstNameTextField.getText();
        accountType = comboBox.getSelectedItem().toString();
    }

    private boolean isInputValid() {
        boolean valid = true;
        getValues();

        if (accountNumber == null || accountNumber.length() != 8)
            valid = false;
        if (surname == null)
            valid = false;
        if (firstName == null)
            valid = false;
        if (accountType == null)
            valid = false;

        return valid;
    }
}