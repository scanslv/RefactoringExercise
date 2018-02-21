import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CreateBankDialog extends JFrame {
    private DetailsFrame detailsFrame;
    private BankApplication parent;

    private String accountNumber, surname, firstName, accountType;

    CreateBankDialog(BankApplication parent) {
        super(Constants.CREATE_TITLE);
        setLayout(new BorderLayout());

        this.parent = parent;
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

                    for (BankAccount bankAccount : parent.table) {
                        if (bankAccount.getAccountNumber().equalsIgnoreCase(accountNumber.trim()))
                            accNumTaken = true;
                    }
                    if (!accNumTaken) {
                        int id = 1;
                        if (!parent.table.isEmpty()) {
                            id = parent.table.get(parent.table.size() - 1).getAccountID() + 1;
                        }
                        parent.table.add(new BankAccount(id, accountNumber, surname, firstName, accountType, 0.0, 0.0));
                        parent.last();
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