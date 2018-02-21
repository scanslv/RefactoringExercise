import javax.swing.*;
import java.io.*;

class DataManipulation {
    private BankApplication parent;
    private JFileChooser fc;
    private RandomAccessFile input;
    private RandomAccessFile output;
    private String fileToSaveAs = "";

    DataManipulation(BankApplication parent) {
        this.parent = parent;
    }

    void writeFile() {
        openFileWrite();
        saveToFile();
        closeFile();
    }

    void saveFileAs() {
        saveToFileAs();
        saveToFile();
        closeFile();
    }

    void readFile() {
        openFileRead();
        readRecords();
        closeFile();
    }

    private void openFileRead() {
        parent.table.clear();
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

                parent.table.add(bankAccount);
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

        for (BankAccount bankAccount : parent.table) {
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
}
