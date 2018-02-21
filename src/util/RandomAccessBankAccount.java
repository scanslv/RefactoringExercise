package util;

import entity.BankAccount;

import java.io.RandomAccessFile;
import java.io.IOException;

class RandomAccessBankAccount extends BankAccount {

    RandomAccessBankAccount() {
        this(0, "", "", "", "", 0.0, 0.0);
    }

    private RandomAccessBankAccount(int accountID, String accountNumber, String firstName, String surname, String accountType,
                                    double balance, double overdraft) {
        super(accountID, accountNumber, firstName, surname, accountType, balance, overdraft);
    }

    void read(RandomAccessFile file) throws IOException {
        setAccountID(file.readInt());
        setAccountNumber(readName(file));
        setFirstName(readName(file));
        setSurname(readName(file));
        setAccountType(readName(file));
        setBalance(file.readDouble());
        setOverdraft(file.readDouble());
    }

    private String readName(RandomAccessFile file) throws IOException {
        char name[] = new char[15], temp;
        for (int count = 0; count < name.length; count++) {
            temp = file.readChar();
            name[count] = temp;
        }

        return new String(name).replace('\0', ' ');
    }

    void write(RandomAccessFile file) throws IOException {
        file.writeInt(getAccountID());
        writeName(file, getAccountNumber());
        writeName(file, getFirstName());
        writeName(file, getSurname());
        writeName(file, getAccountType());
        file.writeDouble(getBalance());
        file.writeDouble(getOverdraft());
    }

    private void writeName(RandomAccessFile file, String name) throws IOException {
        StringBuffer buffer = null;

        if (name != null)
            buffer = new StringBuffer(name);
        else
            buffer = new StringBuffer(15);

        buffer.setLength(15);
        file.writeChars(buffer.toString());
    }

}
