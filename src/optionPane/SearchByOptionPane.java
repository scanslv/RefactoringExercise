package optionPane;

import constants.Constants;
import entity.BankAccount;
import gui.BankApplication;

import javax.swing.*;

public class SearchByOptionPane {
    private BankApplication parent;

    public SearchByOptionPane(BankApplication parent, String type) {
        this.parent = parent;
        if (type.equalsIgnoreCase(Constants.BY_SURNAME))
            searchBySurname();
        else
            searchByAccNr();
    }

    private void searchBySurname() {
        String sName = JOptionPane.showInputDialog(Constants.SEARCH_FOR_SURNAME);
        if (sName != null) {
            boolean found = false;

            for (BankAccount bankAccount : parent.table) {
                if (sName.equalsIgnoreCase((bankAccount.getSurname().trim()))) {
                    found = true;
                    parent.currentItem = parent.table.indexOf(bankAccount);
                    parent.displayDetails();
                }
            }
            if (found)
                JOptionPane.showMessageDialog(null, Constants.SURNAME + sName + Constants.FOUND);
            else
                JOptionPane.showMessageDialog(null, Constants.SURNAME + sName + Constants.NOT_FOUND);
        }
    }

    private void searchByAccNr() {
        String accNum = JOptionPane.showInputDialog(Constants.SEARCH_FOR_ACC_NR);
        if (accNum != null) {
            boolean found = false;

            for (BankAccount bankAccount : parent.table) {
                if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                    found = true;
                    parent.currentItem = parent.table.indexOf(bankAccount);
                    parent.displayDetails();

                }
            }
            if (found)
                JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.FOUND);
            else
                JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.NOT_FOUND);
        }
    }
}
