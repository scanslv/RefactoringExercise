package optionPane;

import constants.Constants;
import entity.BankAccount;
import gui.BankApplication;

import javax.swing.*;

public class WithdrawOptionPane {
    private BankApplication parent;

    public WithdrawOptionPane(BankApplication parent) {
        this.parent = parent;
        withdraw();
    }

    private void withdraw() {
        String accNum = JOptionPane.showInputDialog(Constants.ACCOUNT_TO_WITHDRAW);
        boolean found = false;

        for (BankAccount bankAccount : parent.table) {
            if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                found = true;
                parent.currentItem = parent.table.indexOf(bankAccount);
                if (bankAccount.getAccountType().trim().equals(Constants.CURRENT)) {
                    withdrawCurrent(bankAccount);
                } else if (bankAccount.getAccountType().trim().equals(Constants.DEPOSIT)) {
                    withdrawDeposit(bankAccount);
                }
            }
        }
        if (!found)
            JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.NOT_FOUND);
    }

    private void withdrawCurrent(BankAccount bankAccount) {
        String toWithdraw = JOptionPane.showInputDialog(Constants.AMOUNT_TO_WITHDRAW);
        if (Double.parseDouble(toWithdraw) > bankAccount.getBalance() + bankAccount.getOverdraft())
            JOptionPane.showMessageDialog(null, Constants.TRANSACTION_EXCEEDS);
        else {
            bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
            parent.displayDetails();
        }
    }

    private void withdrawDeposit(BankAccount bankAccount) {
        String toWithdraw = JOptionPane.showInputDialog(Constants.AMOUNT_TO_WITHDRAW);
        if (Double.parseDouble(toWithdraw) <= bankAccount.getBalance()) {
            bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
            parent.displayDetails();
        } else
            JOptionPane.showMessageDialog(null, Constants.INSUFFICIENT_FUNDS);
    }
}
