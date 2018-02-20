import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CreateBankDialog extends JFrame {
    private DetailsFrame detailsFrame;
    private final static int TABLE_SIZE = 29;
    private Random rand = new Random();
    private HashMap<Integer, BankAccount> table = new HashMap<>();

    private String accountNumber, surname, firstName, accountType;

    CreateBankDialog(HashMap<Integer, BankAccount> accounts) {
        super("Add Bank Details");
        table = accounts;
        setLayout(new BorderLayout());

        detailsFrame = new DetailsFrame(false, true);
        add(detailsFrame.buildPanel(), BorderLayout.CENTER);
        add(setUpButtonPanel(), BorderLayout.SOUTH);

        setSize(400, 800);
        pack();
        setVisible(true);
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
                            if (entry.getValue().getAccountNumber().trim().equals(accountNumber)) {
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

    private void getValues() {
        accountNumber = detailsFrame.getAccountNumberTextField().getText();
        surname = detailsFrame.getSurnameTextField().getText();
        firstName = detailsFrame.getFirstNameTextField().getText();
        accountType = detailsFrame.getComboBox().getSelectedItem().toString();
    }
}