package gui;

import constants.Constants;
import entity.BankAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class DisplayAllRecords extends JFrame {

    public DisplayAllRecords(ArrayList<BankAccount> bankAccounts) {
        super(Constants.ALL_RECORDS_TITLE);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String col[] = {Constants.ACCOUNT_ID, Constants.ACCOUNT_NUMBER, Constants.FULL_NAME, Constants.ACCOUNT_TYPE, Constants.BALANCE, Constants.OVERDRAFT};

        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        JTable jTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setAutoCreateRowSorter(true);

        for (BankAccount bankAccount : bankAccounts) {
            Object[] objs = {bankAccount.getAccountID(), bankAccount.getAccountNumber(),
                    bankAccount.getFirstName().trim() + " " + bankAccount.getSurname().trim(),
                    bankAccount.getAccountType(), bankAccount.getBalance(),
                    bankAccount.getOverdraft()};

            tableModel.addRow(objs);
        }
        setSize(700, 500);
        add(scrollPane);
        setVisible(true);
    }
}
