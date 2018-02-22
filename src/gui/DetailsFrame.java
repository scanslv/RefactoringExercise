package gui;

import constants.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DetailsFrame extends JFrame {

    private JPanel dataPanel;
    private JLabel accountIdLabel, accountNumberLabel, lastNameLabel, firstNameLabel, accountTypeLabel, balanceLabel, overdraftLabel;
    private JTextField accountIdTextField, accountNumberTextField, firstNameTextField, surnameTextField, balanceTextField, overdraftTextField, accountTypeTextField;
    private JComboBox comboBox;
    private boolean displayAccountId, displayCombo;

    DetailsFrame(boolean displayAccountId, boolean displayCombo) {
        dataPanel = new JPanel(new MigLayout());
        this.displayAccountId = displayAccountId;
        this.displayCombo = displayCombo;
    }

    JPanel buildPanel() {
        setUpLabels();
        setUpTextFields();
        if (displayAccountId) {
            addLabelAndTextField(dataPanel, accountIdLabel, accountIdTextField);
        }
        addLabelAndTextField(dataPanel, accountNumberLabel, accountNumberTextField);
        addLabelAndTextField(dataPanel, lastNameLabel, surnameTextField);
        addLabelAndTextField(dataPanel, firstNameLabel, firstNameTextField);
        if (displayCombo)
            addLabelAndTextField(dataPanel, accountTypeLabel, comboBox);
        else
            addLabelAndTextField(dataPanel, accountTypeLabel, accountTypeTextField);
        addLabelAndTextField(dataPanel, balanceLabel, balanceTextField);
        addLabelAndTextField(dataPanel, overdraftLabel, overdraftTextField);

        return dataPanel;
    }

    private void setUpLabels() {
        accountIdLabel = new JLabel(Constants.ACCOUNT_ID);
        accountNumberLabel = new JLabel(Constants.ACCOUNT_NUMBER);
        lastNameLabel = new JLabel(Constants.LAST_NAME);
        firstNameLabel = new JLabel(Constants.FIRST_NAME);
        accountTypeLabel = new JLabel(Constants.ACCOUNT_TYPE);
        balanceLabel = new JLabel(Constants.BALANCE);
        overdraftLabel = new JLabel(Constants.OVERDRAFT);
    }

    private void setUpTextFields() {
        final int NR_OF_COLUMNS = 15;
        final int NR_OF_CURRENCY_COLUMNS = 10;

        accountIdTextField = new JTextField(NR_OF_COLUMNS);
        accountIdTextField.setEditable(false);

        accountNumberTextField = new JTextField(NR_OF_COLUMNS);

        surnameTextField = new JTextField(NR_OF_COLUMNS);

        firstNameTextField = new JTextField(NR_OF_COLUMNS);

        accountTypeTextField = new JTextField(NR_OF_COLUMNS);

        comboBox = new JComboBox<>(new String[]{Constants.CURRENT, Constants.DEPOSIT});

        balanceTextField = new JTextField(NR_OF_CURRENCY_COLUMNS);
        balanceTextField.setText("0.0");
        balanceTextField.setEditable(false);

        overdraftTextField = new JTextField(NR_OF_CURRENCY_COLUMNS);
        overdraftTextField.setText("0.0");
        overdraftTextField.setEditable(false);
    }

    private void addLabelAndTextField(JPanel jPanel, JLabel label, Component component) {
        jPanel.add(label, "growx, pushx");
        jPanel.add(component, "growx, pushx, wrap");
    }

    JLabel getAccountIdLabel() {
        return accountIdLabel;
    }

    JTextField getAccountIdTextField() {
        return accountIdTextField;
    }

    JLabel getAccountNumberLabel() {
        return accountNumberLabel;
    }

    JLabel getLastNameLabel() {
        return lastNameLabel;
    }

    JLabel getFirstNameLabel() {
        return firstNameLabel;
    }

    JLabel getAccountTypeLabel() {
        return accountTypeLabel;
    }

    JLabel getBalanceLabel() {
        return balanceLabel;
    }

    JLabel getOverdraftLabel() {
        return overdraftLabel;
    }

    JTextField getAccountNumberTextField() {
        return accountNumberTextField;
    }

    JTextField getFirstNameTextField() {
        return firstNameTextField;
    }

    JTextField getSurnameTextField() {
        return surnameTextField;
    }

    JTextField getBalanceTextField() {
        return balanceTextField;
    }

    JTextField getOverdraftTextField() {
        return overdraftTextField;
    }

    JTextField getAccountTypeTextField() {
        return accountTypeTextField;
    }

    JComboBox getComboBox() {
        return comboBox;
    }
}
