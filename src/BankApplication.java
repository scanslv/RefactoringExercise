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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BankApplication extends JFrame {
    public static ArrayList<BankAccount> table = new ArrayList<>();
    private final int TABLE_SIZE = 29;
    private DetailsFrame detailsFrame;

    private JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    private JFileChooser fc;
    private JTable jTable;
    private double interestRate;
    private int currentItem = 0;
    private int previousItem = 0;
    private boolean openValues;

    public BankApplication() {
        super("Bank Application");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        detailsFrame = new DetailsFrame(true, false);

        add(detailsFrame.buildPanel(), BorderLayout.CENTER);
        setUpTextFields();

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
                if (!table.isEmpty()) {
                    currentItem = 0;
                    displayDetails();
                }
            }
        };

        ActionListener next = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!table.isEmpty()) {
                    if (currentItem < table.size() - 1)
                        currentItem++;
                    else
                        currentItem = 0;
                    displayDetails();
                }
            }
        };

        ActionListener prev = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!table.isEmpty()) {
                    if (currentItem != 0) {
                        currentItem--;
                    } else
                        currentItem = 0;
                    displayDetails();
                }
            }
        };

        ActionListener last = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!table.isEmpty()) {
                    currentItem = table.size() - 1;
                    displayDetails();
                }
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
                openValues = false;
                if (!table.isEmpty()) {
                    table.remove(currentItem);
                    JOptionPane.showMessageDialog(null, "Account Deleted");
                    if (!table.isEmpty()) {
                        if (table.size() == 1) {
                            currentItem = 0;
                            displayDetails();
                        } else if (currentItem == 0) {
                            displayDetails();
                        } else {
                            currentItem--;
                            displayDetails();
                        }
                    } else {
                        currentItem = 0;
//                        clearTextFields();
                    }
                }
            }
        });

        createItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CreateBankDialog();
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

                for (BankAccount bankAccount : table) {
                    Object[] objs = {bankAccount.getAccountID(), bankAccount.getAccountNumber(),
                            bankAccount.getFirstName().trim() + " " + bankAccount.getSurname().trim(),
                            bankAccount.getAccountType(), bankAccount.getBalance(),
                            bankAccount.getOverdraft()};

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
                displayDetails();
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
            }
        });

        findBySurname.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String sName = JOptionPane.showInputDialog("Search for surname: ");
                boolean found = false;

                for (BankAccount bankAccount : table) {

                    if (sName.equalsIgnoreCase((bankAccount.getSurname().trim()))) {
                        found = true;
                        accountIDTextField.setText(bankAccount.getAccountID() + "");
                        accountNumberTextField.setText(bankAccount.getAccountNumber());
                        surnameTextField.setText(bankAccount.getSurname());
                        firstNameTextField.setText(bankAccount.getFirstName());
                        accountTypeTextField.setText(bankAccount.getAccountType());
                        balanceTextField.setText(bankAccount.getBalance() + "");
                        overdraftTextField.setText(bankAccount.getOverdraft() + "");
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

                for (BankAccount bankAccount : table) {

                    if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                        found = true;
                        accountIDTextField.setText(bankAccount.getAccountID() + "");
                        accountNumberTextField.setText(bankAccount.getAccountNumber());
                        surnameTextField.setText(bankAccount.getSurname());
                        firstNameTextField.setText(bankAccount.getFirstName());
                        accountTypeTextField.setText(bankAccount.getAccountType());
                        balanceTextField.setText(bankAccount.getBalance() + "");
                        overdraftTextField.setText(bankAccount.getOverdraft() + "");

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

                for (BankAccount bankAccount : table) {
                    if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                        found = true;
                        String toDeposit = JOptionPane.showInputDialog("Account found, Enter Amount to Deposit: ");
                        bankAccount.setBalance(bankAccount.getBalance() + Double.parseDouble(toDeposit));
                        currentItem = table.indexOf(bankAccount);
                        displayDetails();
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

                for (BankAccount bankAccount : table) {
                    if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                        currentItem = table.indexOf(bankAccount);
                        if (bankAccount.getAccountType().trim().equals("Current")) {
                            if (Double.parseDouble(toWithdraw) > bankAccount.getBalance() + bankAccount.getOverdraft())
                                JOptionPane.showMessageDialog(null, "Transaction exceeds overdraft limit");
                            else {
                                bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
                                displayDetails();
                            }
                        } else if (bankAccount.getAccountType().trim().equals("Deposit")) {
                            if (Double.parseDouble(toWithdraw) <= bankAccount.getBalance()) {
                                bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
                                displayDetails();
                            } else
                                JOptionPane.showMessageDialog(null, "Insufficient funds.");
                        }
                    }
                }
            }
        });

        calcInterest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (BankAccount bankAccount : table) {
                    if (bankAccount.getAccountType().equals("Deposit")) {
                        double equation = 1 + ((interestRate) / 100);
                        bankAccount.setBalance(bankAccount.getBalance() * equation);
                    }
                }
                displayDetails();
                JOptionPane.showMessageDialog(null, "Balances Updated");
            }
        });
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

    private void displayDetails() {
        saveOpenValues();
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
            JOptionPane.showMessageDialog(null, "Error reading file.");
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
}