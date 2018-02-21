package optionPane;

import constants.Constants;
import entity.BankAccount;
import gui.BankApplication;

import javax.swing.*;

public class DepositOptionPane {
    public DepositOptionPane(BankApplication parent){
        String accNum = JOptionPane.showInputDialog(Constants.ACCOUNT_TO_DEPOSIT);
        boolean found = false;
        for (BankAccount bankAccount : parent.table) {
            if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                found = true;
                String toDeposit = JOptionPane.showInputDialog(Constants.AMOUNT_TO_DEPOSIT);
                bankAccount.setBalance(bankAccount.getBalance() + Double.parseDouble(toDeposit));
                parent.currentItem = parent.table.indexOf(bankAccount);
                parent.displayDetails();
            }
        }
        if (!found)
            JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.NOT_FOUND);}
}
