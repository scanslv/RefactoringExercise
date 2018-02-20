import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class BankApplication extends JFrame {
    private HashMap<Integer, BankAccount> table = new HashMap<>();
    private final int TABLE_SIZE = 29;

    private JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    private JFileChooser fc;
    private JTable jTable;
    private double interestRate;
    private int currentItem = 0;
    private boolean openValues;

    public BankApplication() {
        super("Bank Application");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel displayPanel = new JPanel(new MigLayout());

        JLabel accountIDLabel = new JLabel("Account ID: ");
        accountIDTextField = new JTextField(15);
        accountIDTextField.setEditable(false);

        displayPanel.add(accountIDLabel, "growx, pushx");
        displayPanel.add(accountIDTextField, "growx, pushx, wrap");

        JLabel accountNumberLabel = new JLabel("Account Number: ");
        accountNumberTextField = new JTextField(15);
        accountNumberTextField.setEditable(false);

        displayPanel.add(accountNumberLabel, "growx, pushx");
        displayPanel.add(accountNumberTextField, "growx, pushx, wrap");

        JLabel surnameLabel = new JLabel("Last Name: ");
        surnameTextField = new JTextField(15);
        surnameTextField.setEditable(false);

        displayPanel.add(surnameLabel, "growx, pushx");
        displayPanel.add(surnameTextField, "growx, pushx, wrap");

        JLabel firstNameLabel = new JLabel("First Name: ");
        firstNameTextField = new JTextField(15);
        firstNameTextField.setEditable(false);

        displayPanel.add(firstNameLabel, "growx, pushx");
        displayPanel.add(firstNameTextField, "growx, pushx, wrap");

        JLabel accountTypeLabel = new JLabel("Account Type: ");
        accountTypeTextField = new JTextField(5);
        accountTypeTextField.setEditable(false);

        displayPanel.add(accountTypeLabel, "growx, pushx");
        displayPanel.add(accountTypeTextField, "growx, pushx, wrap");

        JLabel balanceLabel = new JLabel("Balance: ");
        balanceTextField = new JTextField(10);
        balanceTextField.setEditable(false);

        displayPanel.add(balanceLabel, "growx, pushx");
        displayPanel.add(balanceTextField, "growx, pushx, wrap");

        JLabel overdraftLabel = new JLabel("Overdraft: ");
        overdraftTextField = new JTextField(10);
        overdraftTextField.setEditable(false);

        displayPanel.add(overdraftLabel, "growx, pushx");
        displayPanel.add(overdraftTextField, "growx, pushx, wrap");

        add(displayPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

        JButton nextItemButton = new JButton(new ImageIcon("next.png"));
        JButton prevItemButton = new JButton(new ImageIcon("prev.png"));
        JButton firstItemButton = new JButton(new ImageIcon("first.png"));
        JButton lastItemButton = new JButton(new ImageIcon("last.png"));

        buttonPanel.add(firstItemButton);
        buttonPanel.add(prevItemButton);
        buttonPanel.add(nextItemButton);
        buttonPanel.add(lastItemButton);

        add(buttonPanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu navigateMenu = new JMenu("Navigate");

        JMenuItem nextItem = new JMenuItem("Next Item");
        JMenuItem prevItem = new JMenuItem("Previous Item");
        JMenuItem firstItem = new JMenuItem("First Item");
        JMenuItem lastItem = new JMenuItem("Last Item");
        JMenuItem findByAccount = new JMenuItem("Find by Account Number");
        JMenuItem findBySurname = new JMenuItem("Find by Surname");
        JMenuItem listAll = new JMenuItem("List All Records");

        navigateMenu.add(nextItem);
        navigateMenu.add(prevItem);
        navigateMenu.add(firstItem);
        navigateMenu.add(lastItem);
        navigateMenu.add(findByAccount);
        navigateMenu.add(findBySurname);
        navigateMenu.add(listAll);

        menuBar.add(navigateMenu);

        JMenu recordsMenu = new JMenu("Records");

        JMenuItem createItem = new JMenuItem("Create Item");
        JMenuItem modifyItem = new JMenuItem("Modify Item");
        JMenuItem deleteItem = new JMenuItem("Delete Item");
        JMenuItem setOverdraft = new JMenuItem("Set Overdraft");
        JMenuItem setInterest = new JMenuItem("Set Interest");

        recordsMenu.add(createItem);
        recordsMenu.add(modifyItem);
        recordsMenu.add(deleteItem);
        recordsMenu.add(setOverdraft);
        recordsMenu.add(setInterest);

        menuBar.add(recordsMenu);

        JMenu transactionsMenu = new JMenu("Transactions");

        JMenuItem deposit = new JMenuItem("Deposit");
        JMenuItem withdraw = new JMenuItem("Withdraw");
        JMenuItem calcInterest = new JMenuItem("Calculate Interest");

        transactionsMenu.add(deposit);
        transactionsMenu.add(withdraw);
        transactionsMenu.add(calcInterest);

        menuBar.add(transactionsMenu);

        JMenu fileMenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open File");
        JMenuItem save = new JMenuItem("Save File");
        JMenuItem saveAs = new JMenuItem("Save As");

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);

        menuBar.add(fileMenu);

        JMenu exitMenu = new JMenu("Exit");

        JMenuItem closeApp = new JMenuItem("Close Application");

        exitMenu.add(closeApp);

        menuBar.add(exitMenu);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setOverdraft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.get(currentItem).getAccountType().trim().equals("Current")) {
                    String newOverdraftStr = JOptionPane.showInputDialog(null, "Enter new Overdraft", JOptionPane.OK_CANCEL_OPTION);
                    overdraftTextField.setText(newOverdraftStr);
                    table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
                } else
                    JOptionPane.showMessageDialog(null, "Overdraft only applies to Current Accounts");
            }
        });

        ActionListener first = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveOpenValues();
                currentItem = 0;

                while (!table.containsKey(currentItem)) {
                    currentItem++;
                }
                displayDetails(currentItem);
            }
        };

        ActionListener next = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<Integer> keyList = new ArrayList<Integer>();
                int i = 0;

                while (i < TABLE_SIZE) {
                    i++;
                    if (table.containsKey(i))
                        keyList.add(i);
                }

                int maxKey = Collections.max(keyList);
                saveOpenValues();

                if (currentItem < maxKey) {
                    currentItem++;
                    while (!table.containsKey(currentItem)) {
                        currentItem++;
                    }
                }
                displayDetails(currentItem);
            }
        };

        ActionListener prev = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Integer> keyList = new ArrayList<Integer>();
                int i = 0;

                while (i < TABLE_SIZE) {
                    i++;
                    if (table.containsKey(i))
                        keyList.add(i);
                }

                int minKey = Collections.min(keyList);

                if (currentItem > minKey) {
                    currentItem--;
                    while (!table.containsKey(currentItem)) {
                        currentItem--;
                    }
                }
                displayDetails(currentItem);
            }
        };

        ActionListener last = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveOpenValues();
                currentItem = 29;

                while (!table.containsKey(currentItem)) {
                    currentItem--;
                }
                displayDetails(currentItem);
            }
        };

        nextItemButton.addActionListener(next);
        nextItem.addActionListener(next);

        prevItemButton.addActionListener(prev);
        prevItem.addActionListener(prev);

        firstItemButton.addActionListener(first);
        firstItem.addActionListener(first);

        lastItemButton.addActionListener(last);
        lastItem.addActionListener(last);

        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table.remove(currentItem);
                JOptionPane.showMessageDialog(null, "Account Deleted");

                currentItem = 0;
                while (!table.containsKey(currentItem)) {
                    currentItem++;
                }
                displayDetails(currentItem);

            }
        });

        createItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CreateBankDialog(table);
            }
        });

        modifyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                surnameTextField.setEditable(true);
                firstNameTextField.setEditable(true);

                openValues = true;
            }
        });

        setInterest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String interestRateStr = JOptionPane.showInputDialog("Enter Interest Rate: (do not type the % sign)");
                if (interestRateStr != null)
                    interestRate = Double.parseDouble(interestRateStr);

            }
        });

        listAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("TableDemo");
                JPanel pan = new JPanel();

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                String col[] = {"ID", "Number", "Name", "Account Type", "Balance", "Overdraft"};

                DefaultTableModel tableModel = new DefaultTableModel(col, 0);
                jTable = new JTable(tableModel);
                JScrollPane scrollPane = new JScrollPane(jTable);
                jTable.setAutoCreateRowSorter(true);

                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                    Object[] objs = {entry.getValue().getAccountID(), entry.getValue().getAccountNumber(),
                            entry.getValue().getFirstName().trim() + " " + entry.getValue().getSurname().trim(),
                            entry.getValue().getAccountType(), entry.getValue().getBalance(),
                            entry.getValue().getOverdraft()};

                    tableModel.addRow(objs);
                }
                frame.setSize(600, 500);
                frame.add(scrollPane);
                frame.setVisible(true);
            }
        });

        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readFile();
                currentItem = 0;
                while (!table.containsKey(currentItem)) {
                    currentItem++;
                }
                displayDetails(currentItem);
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writeFile();
            }
        });

        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        });

        closeApp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int answer = JOptionPane.showConfirmDialog(BankApplication.this, "Do you want to save before quitting?");
                if (answer == JOptionPane.YES_OPTION) {
                    saveFileAs();
                    dispose();
                } else if (answer == JOptionPane.NO_OPTION)
                    dispose();
                else if (answer == 0)
                    ;
            }
        });

        findBySurname.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String sName = JOptionPane.showInputDialog("Search for surname: ");
                boolean found = false;

                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                    if (sName.equalsIgnoreCase((entry.getValue().getSurname().trim()))) {
                        found = true;
                        accountIDTextField.setText(entry.getValue().getAccountID() + "");
                        accountNumberTextField.setText(entry.getValue().getAccountNumber());
                        surnameTextField.setText(entry.getValue().getSurname());
                        firstNameTextField.setText(entry.getValue().getFirstName());
                        accountTypeTextField.setText(entry.getValue().getAccountType());
                        balanceTextField.setText(entry.getValue().getBalance() + "");
                        overdraftTextField.setText(entry.getValue().getOverdraft() + "");
                    }
                }
                if (found)
                    JOptionPane.showMessageDialog(null, "Surname  " + sName + " found.");
                else
                    JOptionPane.showMessageDialog(null, "Surname " + sName + " not found.");
            }
        });

        findByAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String accNum = JOptionPane.showInputDialog("Search for account number: ");
                boolean found = false;

                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                        found = true;
                        accountIDTextField.setText(entry.getValue().getAccountID() + "");
                        accountNumberTextField.setText(entry.getValue().getAccountNumber());
                        surnameTextField.setText(entry.getValue().getSurname());
                        firstNameTextField.setText(entry.getValue().getFirstName());
                        accountTypeTextField.setText(entry.getValue().getAccountType());
                        balanceTextField.setText(entry.getValue().getBalance() + "");
                        overdraftTextField.setText(entry.getValue().getOverdraft() + "");

                    }
                }
                if (found)
                    JOptionPane.showMessageDialog(null, "Account number " + accNum + " found.");
                else
                    JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");

            }
        });

        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accNum = JOptionPane.showInputDialog("Account number to deposit into: ");
                boolean found = false;

                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                        found = true;
                        String toDeposit = JOptionPane.showInputDialog("Account found, Enter Amount to Deposit: ");
                        entry.getValue().setBalance(entry.getValue().getBalance() + Double.parseDouble(toDeposit));
                        displayDetails(entry.getKey());
                        //balanceTextField.setText(entry.getValue().getBalance()+"");
                    }
                }
                if (!found)
                    JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");
            }
        });

        withdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String accNum = JOptionPane.showInputDialog("Account number to withdraw from: ");
                String toWithdraw = JOptionPane.showInputDialog("Account found, Enter Amount to Withdraw: ");
                boolean found;

                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {

                        found = true;

                        if (entry.getValue().getAccountType().trim().equals("Current")) {
                            if (Double.parseDouble(toWithdraw) > entry.getValue().getBalance() + entry.getValue().getOverdraft())
                                JOptionPane.showMessageDialog(null, "Transaction exceeds overdraft limit");
                            else {
                                entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
                                displayDetails(entry.getKey());
                            }
                        } else if (entry.getValue().getAccountType().trim().equals("Deposit")) {
                            if (Double.parseDouble(toWithdraw) <= entry.getValue().getBalance()) {
                                entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
                                displayDetails(entry.getKey());
                            } else
                                JOptionPane.showMessageDialog(null, "Insufficient funds.");
                        }
                    }
                }
            }
        });

        calcInterest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                    if (entry.getValue().getAccountType().equals("Deposit")) {
                        double equation = 1 + ((interestRate) / 100);
                        entry.getValue().setBalance(entry.getValue().getBalance() * equation);
                        JOptionPane.showMessageDialog(null, "Balances Updated");
                        displayDetails(entry.getKey());
                    }
                }
            }
        });
    }

    private void saveOpenValues() {
        if (openValues) {
            surnameTextField.setEditable(false);
            firstNameTextField.setEditable(false);

            table.get(currentItem).setSurname(surnameTextField.getText());
            table.get(currentItem).setFirstName(firstNameTextField.getText());
        }
    }

    private void displayDetails(int currentItem) {
        accountIDTextField.setText(table.get(currentItem).getAccountID() + "");
        accountNumberTextField.setText(table.get(currentItem).getAccountNumber());
        surnameTextField.setText(table.get(currentItem).getSurname());
        firstNameTextField.setText(table.get(currentItem).getFirstName());
        accountTypeTextField.setText(table.get(currentItem).getAccountType());
        balanceTextField.setText(table.get(currentItem).getBalance() + "");
        if (accountTypeTextField.getText().trim().equals("Current"))
            overdraftTextField.setText(table.get(currentItem).getOverdraft() + "");
        else
            overdraftTextField.setText("Only applies to current accs");
    }

    private RandomAccessFile input;
    private RandomAccessFile output;

    private void openFileRead() {
        table.clear();
        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        }

        try // open file
        {
            if (fc.getSelectedFile() != null)
                input = new RandomAccessFile(fc.getSelectedFile(), "r");
        } // end try
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "File Does Not Exist.");
        } // end catch
    } // end method openFile

    private String fileToSaveAs = "";

    private void openFileWrite() {
        if (fileToSaveAs.equalsIgnoreCase("")) {
            // open file
            try {
                output = new RandomAccessFile(fileToSaveAs, "rw");
                JOptionPane.showMessageDialog(null, "Accounts saved to " + fileToSaveAs);
            } // end try
            catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "File does not exist.");
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
            JOptionPane.showMessageDialog(null, "Accounts saved to " + file.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Save cancelled by user");
        }

        try {
            if (fc.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "Cancelled");
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
            JOptionPane.showMessageDialog(null, "Error closing file.");//System.exit( 1 );
        } // end catch
    } // end method closeFile

    private void readRecords() {
        RandomAccessBankAccount record = new RandomAccessBankAccount();
        // read a record and display
        try {
            while (true) {
                do {
                    if (input != null)
                        record.read(input);
                } while (record.getAccountID() == 0);

                BankAccount ba = new BankAccount(record.getAccountID(), record.getAccountNumber(), record.getFirstName(),
                        record.getSurname(), record.getAccountType(), record.getBalance(), record.getOverdraft());

                Integer key = Integer.valueOf(ba.getAccountNumber().trim());
                int hash = (key % TABLE_SIZE);

                while (table.containsKey(hash)) {

                    hash = hash + 1;
                }

                table.put(hash, ba);
            } // end while
        } // end try
        catch (EOFException eofException) // close file
        {
            return; // end of file was reached
        } // end catch
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error reading file.");
            System.exit(1);
        } // end catch
    }

    private void saveToFile() {
        RandomAccessBankAccount record = new RandomAccessBankAccount();
        Scanner input = new Scanner(System.in);

        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
            record.setAccountID(entry.getValue().getAccountID());
            record.setAccountNumber(entry.getValue().getAccountNumber());
            record.setFirstName(entry.getValue().getFirstName());
            record.setSurname(entry.getValue().getSurname());
            record.setAccountType(entry.getValue().getAccountType());
            record.setBalance(entry.getValue().getBalance());
            record.setOverdraft(entry.getValue().getOverdraft());

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
}