package main;

import gui.BankApplication;

public class BankMain {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
                BankApplication::createAndShowGUI);
    }
}
