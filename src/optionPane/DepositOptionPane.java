package optionPane;

import constants.Constants;
import entity.BankAccount;
import gui.BankApplication;

import javax.swing.*;

public class DepositOptionPane {
    private BankApplication parent;

    public DepositOptionPane(BankApplication parent) {
        this.parent = parent;
        deposit();
    }

    private void deposit() {
        String accNum = JOptionPane.showInputDialog(Constants.ACCOUNT_TO_DEPOSIT);
        if (accNum != null) {
            boolean found = false;
            for (BankAccount bankAccount : parent.table) {
                if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                    found = true;
                    depositMoney(bankAccount);
                }
            }
            if (!found)
                JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.NOT_FOUND);
        }
    }

    private void depositMoney(BankAccount bankAccount) {
        String toDeposit = JOptionPane.showInputDialog(Constants.AMOUNT_TO_DEPOSIT);
        if (toDeposit != null) {
            bankAccount.setBalance(bankAccount.getBalance() + Double.parseDouble(toDeposit));
            parent.currentItem = parent.table.indexOf(bankAccount);
            parent.displayDetails();
        }
    }
}
