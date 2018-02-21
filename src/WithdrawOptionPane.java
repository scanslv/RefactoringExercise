import javax.swing.*;

public class WithdrawOptionPane {
    public WithdrawOptionPane(BankApplication parent) {
        String accNum = JOptionPane.showInputDialog(Constants.ACCOUNT_TO_WITHDRAW);
        String toWithdraw = JOptionPane.showInputDialog(Constants.AMMOUNT_TO_WITHDRAW);
        boolean found = false;

        for (BankAccount bankAccount : parent.table) {
            if (accNum.equals(bankAccount.getAccountNumber().trim())) {
                found = true;
                parent.currentItem = parent.table.indexOf(bankAccount);
                if (bankAccount.getAccountType().trim().equals(Constants.CURRENT)) {
                    if (Double.parseDouble(toWithdraw) > bankAccount.getBalance() + bankAccount.getOverdraft())
                        JOptionPane.showMessageDialog(null, Constants.TRANSACTION_EXCEEDS);
                    else {
                        bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
                        parent.displayDetails();
                    }
                } else if (bankAccount.getAccountType().trim().equals(Constants.DEPOSIT)) {
                    if (Double.parseDouble(toWithdraw) <= bankAccount.getBalance()) {
                        bankAccount.setBalance(bankAccount.getBalance() - Double.parseDouble(toWithdraw));
                        parent.displayDetails();
                    } else
                        JOptionPane.showMessageDialog(null, Constants.INSUFFICIENT_FUNDS);
                }
            }
        }
        if (!found)
            JOptionPane.showMessageDialog(null, Constants.ACCOUNT_NR + accNum + Constants.NOT_FOUND);
    }
}
