import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CreateBankDialog extends JFrame {
    private DetailsFrame detailsFrame;
    private Random rand = new Random();

    private String accountNumber, surname, firstName, accountType;

    CreateBankDialog() {
        super(Constants.CREATE_TITLE);
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
        JButton addButton = new JButton(Constants.ADD);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInputValid()) {
                    boolean accNumTaken = false;

                    for (BankAccount bankAccount : BankApplication.table) {
                        if (bankAccount.getAccountNumber().equalsIgnoreCase(accountNumber.trim()))
                            accNumTaken = true;
                    }
                    if (!accNumTaken) {
                        int id = 1;
                        if (!BankApplication.table.isEmpty()) {
                            id = BankApplication.table.get(BankApplication.table.size() - 1).getAccountID() + 1;
                        }
                        BankApplication.table.add(new BankAccount(id, accountNumber, surname, firstName, accountType, 0.0, 0.0));
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, Constants.UNIQUE_NUMBER);
                    }
                } else
                    JOptionPane.showMessageDialog(null, Constants.VALIDATION_TEXT);
            }
        });
        return addButton;
    }

    private JButton setUpCancelButton() {
        JButton cancelButton = new JButton(Constants.CANCEL);
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