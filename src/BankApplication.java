import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.*;

public class BankApplication extends JFrame implements ActionListener {
    ArrayList<BankAccount> table = new ArrayList<>();
    private DetailsFrame detailsFrame;

    private JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    private JFileChooser fc;
    private double interestRate;
    int currentItem = 0;
    private int previousItem = 0;
    private boolean openValues = false;
    private RandomAccessFile input;
    private RandomAccessFile output;
    private String fileToSaveAs = "";

    BankApplication() {
        super(Constants.TITLE);
        detailsFrame = new DetailsFrame(true, false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        add(detailsFrame.buildPanel(), BorderLayout.CENTER);
        setUpTextFields();
        add(setUpNavigationButtons(), BorderLayout.SOUTH);
        setJMenuBar(setUpMenuBar());
        setUpActionListeners(getComponents());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JMenuBar setUpMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(setUpNavigateMenu());
        menuBar.add(setUpRecordsMenu());
        menuBar.add(setUpTransactionsMenu());
        menuBar.add(setUpFileMenu());
        menuBar.add(setUpExitMenu());

        return menuBar;
    }

    private JMenu setUpNavigateMenu() {
        JMenu navigateMenu = new JMenu(Constants.NAVIGATE);

        JMenuItem nextItem = new JMenuItem(Constants.NEXT_ITEM);
        JMenuItem prevItem = new JMenuItem(Constants.PREV_ITEM);
        JMenuItem firstItem = new JMenuItem(Constants.FIRST_ITEM);
        JMenuItem lastItem = new JMenuItem(Constants.LAST_ITEM);
        JMenuItem findByAccount = new JMenuItem(Constants.BY_ACCOUNT_NR);
        JMenuItem findBySurname = new JMenuItem(Constants.BY_SURNAME);
        JMenuItem listAll = new JMenuItem(Constants.LIST_ALL);

        navigateMenu.add(nextItem);
        navigateMenu.add(prevItem);
        navigateMenu.add(firstItem);
        navigateMenu.add(lastItem);
        navigateMenu.add(findByAccount);
        navigateMenu.add(findBySurname);
        navigateMenu.add(listAll);

        return navigateMenu;
    }

    private JMenu setUpRecordsMenu() {
        JMenu recordsMenu = new JMenu(Constants.RECORDS);

        JMenuItem createItem = new JMenuItem(Constants.CREATE_ITEM);
        JMenuItem modifyItem = new JMenuItem(Constants.MODIFY_ITEM);
        JMenuItem deleteItem = new JMenuItem(Constants.DELETE_ITEM);
        JMenuItem setOverdraft = new JMenuItem(Constants.SET_OVERDRAFT);
        JMenuItem setInterest = new JMenuItem(Constants.SET_INTEREST);

        recordsMenu.add(createItem);
        recordsMenu.add(modifyItem);
        recordsMenu.add(deleteItem);
        recordsMenu.add(setOverdraft);
        recordsMenu.add(setInterest);

        return recordsMenu;
    }

    private JMenu setUpTransactionsMenu() {
        JMenu transactionsMenu = new JMenu(Constants.TRANSACTIONS);

        JMenuItem deposit = new JMenuItem(Constants.DEPOSIT);
        JMenuItem withdraw = new JMenuItem(Constants.WITHDRAW);
        JMenuItem calcInterest = new JMenuItem(Constants.CALCULATE_INTEREST);

        transactionsMenu.add(deposit);
        transactionsMenu.add(withdraw);
        transactionsMenu.add(calcInterest);

        return transactionsMenu;
    }

    private JMenu setUpFileMenu() {
        JMenu fileMenu = new JMenu(Constants.FILE);

        JMenuItem open = new JMenuItem(Constants.OPEN_FILE);
        JMenuItem save = new JMenuItem(Constants.SAVE_FILE);
        JMenuItem saveAs = new JMenuItem(Constants.SAVE_AS);

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);

        return fileMenu;
    }

    private JMenu setUpExitMenu() {
        JMenu exitMenu = new JMenu(Constants.EXIT);

        JMenuItem closeApp = new JMenuItem(Constants.CLOSE_APPLICATION);

        exitMenu.add(closeApp);

        return exitMenu;
    }

    private void setUpActionListeners(Component[] components) {
        for (Component component : components) {
            if (component instanceof JButton)
                ((JButton) component).addActionListener(this);
            else if (component instanceof JMenu)
                setUpActionListeners(((JMenu) component).getMenuComponents());
            else if (component instanceof JMenuItem)
                ((JMenuItem) component).addActionListener(this);
            else if (component instanceof Container)
                setUpActionListeners(((Container) component).getComponents());

        }
    }

    private JPanel setUpNavigationButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

        JButton nextItemButton = new JButton(new ImageIcon("next.png"));
        nextItemButton.setActionCommand(Constants.NEXT_ITEM);
        JButton prevItemButton = new JButton(new ImageIcon("prev.png"));
        prevItemButton.setActionCommand(Constants.PREV_ITEM);
        JButton firstItemButton = new JButton(new ImageIcon("first.png"));
        firstItemButton.setActionCommand(Constants.FIRST_ITEM);
        JButton lastItemButton = new JButton(new ImageIcon("last.png"));
        lastItemButton.setActionCommand(Constants.LAST_ITEM);

        buttonPanel.add(firstItemButton);
        buttonPanel.add(prevItemButton);
        buttonPanel.add(nextItemButton);
        buttonPanel.add(lastItemButton);

        return buttonPanel;
    }

    private void setUpTextFields() {
        accountIDTextField = detailsFrame.getAccountIdTextField();
        accountNumberTextField = detailsFrame.getAccountNumberTextField();
        firstNameTextField = detailsFrame.getFirstNameTextField();
        surnameTextField = detailsFrame.getSurnameTextField();
        accountTypeTextField = detailsFrame.getAccountTypeTextField();
        balanceTextField = detailsFrame.getBalanceTextField();
        overdraftTextField = detailsFrame.getOverdraftTextField();

        accountNumberTextField.setEditable(false);
        surnameTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        accountTypeTextField.setEditable(false);
        balanceTextField.setText("");
        overdraftTextField.setText("");
    }

    private void saveOpenValues() {
        if (openValues) {
            surnameTextField.setEditable(false);
            firstNameTextField.setEditable(false);

            table.get(previousItem).setSurname(surnameTextField.getText());
            table.get(previousItem).setFirstName(firstNameTextField.getText());
        }
        previousItem = currentItem;
    }

    void displayDetails() {
        saveOpenValues();
        accountIDTextField.setText(table.get(currentItem).getAccountID() + "");
        accountNumberTextField.setText(table.get(currentItem).getAccountNumber());
        surnameTextField.setText(table.get(currentItem).getSurname());
        firstNameTextField.setText(table.get(currentItem).getFirstName());
        accountTypeTextField.setText(table.get(currentItem).getAccountType());
        balanceTextField.setText(table.get(currentItem).getBalance() + "");
        if (accountTypeTextField.getText().trim().equals(Constants.CURRENT))
            overdraftTextField.setText(table.get(currentItem).getOverdraft() + "");
        else
            overdraftTextField.setText(Constants.ONLY_APPLIES_TO);
    }

    private void openFileRead() {
        table.clear();
        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            fileToSaveAs = file.getAbsolutePath();
        }

        try // open file
        {
            if (fc.getSelectedFile() != null)
                input = new RandomAccessFile(fc.getSelectedFile(), "r");
        } // end try
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, Constants.FILE_NOT_EXIST);
        } // end catch
    } // end method openFile

    private void openFileWrite() {
        if (!fileToSaveAs.equalsIgnoreCase("")) {
            // open file
            try {
                output = new RandomAccessFile(fileToSaveAs, "rw");
                JOptionPane.showMessageDialog(null, Constants.ACCOUNT_SAVED + fileToSaveAs);
            } // end try
            catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, Constants.FILE_NOT_EXIST);
            } // end catch
        } else
            saveToFileAs();
    }

    private void saveToFileAs() {
        fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            fileToSaveAs = file.getName();
            JOptionPane.showMessageDialog(null, Constants.ACCOUNT_SAVED + file.getName());
        } else {
            JOptionPane.showMessageDialog(null, Constants.SAVE_CANCELLED);
        }

        try {
            if (fc.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, Constants.CANCELLED);
            } else
                output = new RandomAccessFile(fc.getSelectedFile(), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeFile() {
        try // close file and exit
        {
            if (input != null)
                input.close();
        } // end try
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, Constants.ERROR_CLOSING);//System.exit( 1 );
        } // end catch
    } // end method closeFile

    private void readRecords() {
        RandomAccessBankAccount record = new RandomAccessBankAccount();
        // read a record and display
        try {
            while (true) {
                record.read(input);

                BankAccount bankAccount = new BankAccount(record.getAccountID(), record.getAccountNumber(), record.getFirstName(),
                        record.getSurname(), record.getAccountType(), record.getBalance(), record.getOverdraft());

                table.add(bankAccount);
            } // end while
        } // end try
        catch (EOFException eofException) // close file
        {
            return; // end of file was reached
        } // end catch
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, Constants.ERROR_READING);
            System.exit(1);
        } // end catch
    }

    private void saveToFile() {
        RandomAccessBankAccount record = new RandomAccessBankAccount();

        for (BankAccount bankAccount : table) {
            record.setAccountID(bankAccount.getAccountID());
            record.setAccountNumber(bankAccount.getAccountNumber());
            record.setFirstName(bankAccount.getFirstName());
            record.setSurname(bankAccount.getSurname());
            record.setAccountType(bankAccount.getAccountType());
            record.setBalance(bankAccount.getBalance());
            record.setOverdraft(bankAccount.getOverdraft());

            if (output != null) {
                try {
                    record.write(output);
                } catch (IOException u) {
                    u.printStackTrace();
                }
            }
        }
    }

    private void writeFile() {
        openFileWrite();
        saveToFile();
        closeFile();
    }

    private void saveFileAs() {
        saveToFileAs();
        saveToFile();
        closeFile();
    }

    private void readFile() {
        openFileRead();
        readRecords();
        closeFile();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Constants.NEXT_ITEM: {
                if (!table.isEmpty()) {
                    if (currentItem < table.size() - 1)
                        currentItem++;
                    else
                        currentItem = 0;
                    displayDetails();
                }
                break;
            }
            case Constants.PREV_ITEM: {
                if (!table.isEmpty()) {
                    if (currentItem != 0) {
                        currentItem--;
                    } else
                        currentItem = table.size() - 1;
                    displayDetails();
                }
                break;
            }
            case Constants.LAST_ITEM: {
                last();
                break;
            }
            case Constants.FIRST_ITEM: {
                if (!table.isEmpty()) {
                    currentItem = 0;
                    displayDetails();
                }
                break;
            }
            case Constants.BY_ACCOUNT_NR: {
                String accNum = JOptionPane.showInputDialog("Search for account number: ");
                boolean found = false;

                for (BankAccount bankAccount : table) {
                    if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                        found = true;
                        currentItem = table.indexOf(bankAccount);
                        displayDetails();

                    }
                }
                if (found)
                    JOptionPane.showMessageDialog(null, "Account number " + accNum + " found.");
                else
                    JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");

                break;
            }
            case Constants.BY_SURNAME: {
                String sName = JOptionPane.showInputDialog("Search for surname: ");
                boolean found = false;

                for (BankAccount bankAccount : table) {

                    if (sName.equalsIgnoreCase((bankAccount.getSurname().trim()))) {
                        found = true;
                        currentItem = table.indexOf(bankAccount);
                        displayDetails();
                    }
                }
                if (found)
                    JOptionPane.showMessageDialog(null, "Surname  " + sName + " found.");
                else
                    JOptionPane.showMessageDialog(null, "Surname " + sName + " not found.");
                break;
            }
            case Constants.LIST_ALL: {
                new DisplayAllRecords(table);
                break;
            }
            case Constants.SET_OVERDRAFT: {
                if (table.get(currentItem).getAccountType().trim().equals("Current")) {
                    String newOverdraftStr = JOptionPane.showInputDialog(null, "Enter new Overdraft", JOptionPane.OK_CANCEL_OPTION);
                    overdraftTextField.setText(newOverdraftStr);
                    table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
                } else
                    JOptionPane.showMessageDialog(null, "Overdraft only applies to Current Accounts");
                break;
            }
            case Constants.DELETE_ITEM: {
                openValues = false;
                if (!table.isEmpty()) {
                    table.remove(currentItem);
                    JOptionPane.showMessageDialog(null, "Account Deleted");
                    if (!table.isEmpty()) {
                        if (table.size() == 1)
                            currentItem = 0;
                        else
                            currentItem--;
                        displayDetails();

                    } else {
                        currentItem = 0;
                        clearTextFields();
                    }
                }
                break;
            }
            case Constants.CREATE_ITEM: {
                new CreateBankDialog(this);
                break;
            }
            case Constants.MODIFY_ITEM: {
                surnameTextField.setEditable(true);
                firstNameTextField.setEditable(true);

                openValues = true;
                break;
            }
            case Constants.SET_INTEREST: {
                String interestRateStr = JOptionPane.showInputDialog(Constants.ENTER_INTEREST);
                if (interestRateStr != null)
                    interestRate = Double.parseDouble(interestRateStr);
                break;
            }
            case Constants.DEPOSIT: {
                new DepositOptionPane(this);
                break;
            }
            case Constants.WITHDRAW: {
                new WithdrawOptionPane(this);
                break;
            }
            case Constants.CALCULATE_INTEREST: {
                for (BankAccount bankAccount : table) {
                    if (bankAccount.getAccountType().equals(Constants.DEPOSIT)) {
                        double equation = 1 + ((interestRate) / 100);
                        bankAccount.setBalance(bankAccount.getBalance() * equation);
                    }
                }
                displayDetails();
                JOptionPane.showMessageDialog(null, Constants.BALLANCE_UPDATED);
                break;
            }
            case Constants.OPEN_FILE: {
                readFile();
                currentItem = 0;
                displayDetails();
                break;
            }
            case Constants.SAVE_FILE: {
                writeFile();
                break;
            }
            case Constants.SAVE_AS: {
                saveFileAs();
                break;
            }
            case Constants.CLOSE_APPLICATION: {
                int answer = JOptionPane.showConfirmDialog(BankApplication.this, "Do you want to save before quitting?");
                if (answer == JOptionPane.YES_OPTION) {
                    saveFileAs();
                    dispose();
                } else if (answer == JOptionPane.NO_OPTION)
                    dispose();
                break;
            }
        }
    }

    void last() {
        if (!table.isEmpty()) {
            currentItem = table.size() - 1;
            displayDetails();
        }
    }

    private void clearTextFields() {
        accountIDTextField.setText("");
        accountNumberTextField.setText("");
        surnameTextField.setText("");
        firstNameTextField.setText("");
        accountTypeTextField.setText("");
        balanceTextField.setText("");
        overdraftTextField.setText("");
    }
}