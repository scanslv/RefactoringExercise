package gui;

import constants.Constants;
import entity.BankAccount;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class CreateBankFrame extends JFrame implements ActionListener {
    private DetailsFrame detailsFrame;
    private BankApplication parent;
    private String accountNumber, surname, firstName, accountType;

    CreateBankFrame(BankApplication parent) {
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

        addButton.addActionListener(this);
        return addButton;
    }

    private JButton setUpCancelButton() {
        JButton cancelButton = new JButton(Constants.CANCEL);
        cancelButton.addActionListener(this);

        return cancelButton;
    }

    private void getValues() {
        accountNumber = detailsFrame.getAccountNumberTextField().getText();
        surname = detailsFrame.getSurnameTextField().getText();
        firstName = detailsFrame.getFirstNameTextField().getText();
        accountType = detailsFrame.getComboBox().getSelectedItem().toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Constants.ADD: {
                if (isInputValid() && !isNumTaken()) {
                    parent.table.add(createAccount());
                    parent.last();
                    dispose();
                }
                break;
            }
            case Constants.CANCEL: {
                dispose();
                break;
            }
        }
    }

    private BankAccount createAccount() {
        int id = 1;
        if (!parent.table.isEmpty()) {
            id = parent.table.get(parent.table.size() - 1).getAccountID() + 1;
        }
        return new BankAccount(id, accountNumber, surname, firstName, accountType, 0.0, 0.0);
    }

    private boolean isInputValid() {
        boolean valid;
        getValues();

        valid = accountNrValid();
        if (surname == null || surname.isEmpty() || surname.length() > 20)
            valid = false;
        if (firstName == null || firstName.isEmpty() || firstName.length() > 21)
            valid = false;
        if (accountType == null)
            valid = false;

        if (!valid)
            JOptionPane.showMessageDialog(null, Constants.VALIDATION_TEXT);

        return valid;
    }

    private boolean accountNrValid() {
        try {
            return accountNumber != null && accountNumber.length() == 8 && (Integer.parseInt(accountNumber) > 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isNumTaken() {
        boolean accNumTaken = false;

        for (BankAccount bankAccount : parent.table) {
            if (bankAccount.getAccountNumber().equalsIgnoreCase(accountNumber.trim()))
                accNumTaken = true;
        }

        if (accNumTaken)
            JOptionPane.showMessageDialog(null, Constants.UNIQUE_NUMBER);

        return accNumTaken;
    }
}