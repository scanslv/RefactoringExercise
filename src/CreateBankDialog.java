import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
    private Random rand = new Random();
    private HashMap<Integer, BankAccount> table = new HashMap<>();

    public void put(int key, BankAccount value) {
        int hash = (key % TABLE_SIZE);

        while (table.containsKey(key)) {
            hash = hash + 1;
        }
        table.put(hash, value);
    }

    private JTextField accountNumberTextField;
    private final JTextField firstNameTextField;
    private final JTextField surnameTextField;
    private final JTextField balanceTextField;
    private final JTextField overdraftTextField;

    CreateBankDialog(HashMap accounts) {
        super("Add Bank Details");
        table = accounts;
        setLayout(new BorderLayout());
        JPanel dataPanel = new JPanel(new MigLayout());
        String[] comboTypes = {"Current", "Deposit"};
        final JComboBox comboBox = new JComboBox(comboTypes);
        accountNumberTextField = new JTextField(15);
        JLabel accountNumberLabel = new JLabel("Account Number: ");
        accountNumberTextField = new JTextField(15);
        accountNumberTextField.setEditable(true);

        dataPanel.add(accountNumberLabel, "growx, pushx");
        dataPanel.add(accountNumberTextField, "growx, pushx, wrap");

        JLabel surnameLabel = new JLabel("Last Name: ");
        surnameTextField = new JTextField(15);
        surnameTextField.setEditable(true);

        dataPanel.add(surnameLabel, "growx, pushx");
        dataPanel.add(surnameTextField, "growx, pushx, wrap");

        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameTextField = new JTextField(15);
        firstNameTextField.setEditable(true);

        dataPanel.add(firstNameLabel, "growx, pushx");
        dataPanel.add(firstNameTextField, "growx, pushx, wrap");

        JLabel accountTypeLabel = new JLabel("Account Type: ");
        JTextField accountTypeTextField = new JTextField(5);
        accountTypeTextField.setEditable(true);

        dataPanel.add(accountTypeLabel, "growx, pushx");
        dataPanel.add(comboBox, "growx, pushx, wrap");

        JLabel balanceLabel = new JLabel("Balance: ");
        balanceTextField = new JTextField(10);
        balanceTextField.setText("0.0");
        balanceTextField.setEditable(false);

        dataPanel.add(balanceLabel, "growx, pushx");
        dataPanel.add(balanceTextField, "growx, pushx, wrap");

        JLabel overdraftLabel = new JLabel("Overdraft: ");
        overdraftTextField = new JTextField(10);
        overdraftTextField.setText("0.0");
        overdraftTextField.setEditable(false);

        dataPanel.add(overdraftLabel, "growx, pushx");
        dataPanel.add(overdraftTextField, "growx, pushx, wrap");

        add(dataPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accountNumber = accountNumberTextField.getText();
                String surname = surnameTextField.getText();
                String firstName = firstNameTextField.getText();
                String accountType = comboBox.getSelectedItem().toString();
                String balanceStr = balanceTextField.getText();
                String overdraftStr = overdraftTextField.getText();
                double balance;
                double overdraft;

                if (accountNumber != null && accountNumber.length() == 8 && surname != null && firstName != null && accountType != null) {
                    try {

                        boolean idTaken = false;
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

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setSize(400, 800);
        pack();
        setVisible(true);
    }
}