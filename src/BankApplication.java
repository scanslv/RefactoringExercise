import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class BankApplication extends JFrame implements ActionListener {
    ArrayList<BankAccount> table = new ArrayList<>();
    int currentItem;
    private DataManipulation fileManipulation;
    private DetailsFrame detailsFrame;

    private JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    private double interestRate;
    private int previousItem = 0;
    private boolean openValues = false;

    BankApplication() {
        super(Constants.TITLE);
        detailsFrame = new DetailsFrame(true, false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        fileManipulation = new DataManipulation(this);
        currentItem = 0;
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
        accountIDTextField.setText(String.valueOf(table.get(currentItem).getAccountID()));
        accountNumberTextField.setText(table.get(currentItem).getAccountNumber());
        surnameTextField.setText(table.get(currentItem).getSurname());
        firstNameTextField.setText(table.get(currentItem).getFirstName());
        accountTypeTextField.setText(table.get(currentItem).getAccountType());
        balanceTextField.setText(String.valueOf(table.get(currentItem).getBalance()));
        if (accountTypeTextField.getText().trim().equals(Constants.CURRENT))
            overdraftTextField.setText(String.valueOf(table.get(currentItem).getOverdraft()));
        else
            overdraftTextField.setText(Constants.ONLY_APPLIES_TO);
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
                new SearchByOptionPane(this, Constants.BY_ACCOUNT_NR);
                break;
            }
            case Constants.BY_SURNAME: {
                new SearchByOptionPane(this, Constants.BY_SURNAME);
                break;
            }
            case Constants.LIST_ALL: {
                new DisplayAllRecords(table);
                break;
            }
            case Constants.SET_OVERDRAFT: {
                if (table.get(currentItem).getAccountType().trim().equals(Constants.CURRENT)) {
                    String newOverdraftStr = JOptionPane.showInputDialog(null, Constants.NEW_OVERDRAFT, JOptionPane.OK_CANCEL_OPTION);
                    overdraftTextField.setText(newOverdraftStr);
                    table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
                } else
                    JOptionPane.showMessageDialog(null, Constants.OVERDRAFT_MSG);
                break;
            }
            case Constants.DELETE_ITEM: {
                openValues = false;
                if (!table.isEmpty()) {
                    table.remove(currentItem);
                    JOptionPane.showMessageDialog(null, Constants.ACCOUNT_DELETED);
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
                fileManipulation.readFile();
                currentItem = 0;
                displayDetails();
                break;
            }
            case Constants.SAVE_FILE: {
                fileManipulation.writeFile();
                break;
            }
            case Constants.SAVE_AS: {
                fileManipulation.saveFileAs();
                break;
            }
            case Constants.CLOSE_APPLICATION: {
                int answer = JOptionPane.showConfirmDialog(BankApplication.this, Constants.CLOSE_MSG);
                if (answer == JOptionPane.YES_OPTION) {
                    fileManipulation.saveFileAs();
                    dispose();
                } else if (answer == JOptionPane.NO_OPTION)
                    dispose();
                break;
            }
        }
    }
}