package com.in_the_moment;

public class InTheMomentApp {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        MySQLAccess mySQLAccess = new MySQLAccess();
        new MainWindowController(mainWindow, mySQLAccess);
        mainWindow.showMainWindow();
        //MySQLAccess dao = new MySQLAccess();
        //dao.readDataBase();
    }
}
