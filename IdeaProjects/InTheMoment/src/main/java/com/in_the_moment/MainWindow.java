package com.in_the_moment;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;

import com.in_the_moment.Model.Moment;

public class MainWindow implements DateUtility{

    private JFrame frame;
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList<Moment> jlist1;
    private JComboBox<String> comboBoxDate;
    private JButton updateButton;
    private JTextPane momentDescriptionTextField;
    private JButton photoDescriptionSaveButton;
    private JPanel JPanelLeft;
    private JPanel JPanelRight;
    private JScrollPane textDescriptionScroll;

    public DefaultComboBoxModel<String> comboDate = new DefaultComboBoxModel<>();

    public MainWindow(){
        frame = new JFrame("MainWindow");
        frame.setContentPane(main_window_main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        Border customBorderRose = BorderFactory.createLineBorder(Color.decode("#D46A6A"));
        main_window_header.setBorder(customBorderRose);
        thumbnail_scroll_vindow.setBorder(customBorderRose);
        //momentDescriptionTextField.setBorder();
        //thumbnail_scroll_vindow.getVerticalScrollBar().setUnitIncrement(50);
    }

    public JTextPane getMomentDescriptionTextField() {
        return momentDescriptionTextField;
    }

    public JComboBox<String> getComboBoxDate() {
        return comboBoxDate;
    }

    public JLabel getJlabel1() {
        return jlabel1;
    }

    public JList<Moment> getJlist1() {
        return jlist1;
    }

    public void showMainWindow(){
        frame.setVisible(true);
    }

    public void fillComboBox(ArrayList<Date> momentDates){
        for(Date date : momentDates){
            String parsedDate = parseDateComboBox(date);
            comboDate.addElement(parsedDate);
        }
        comboBoxDate.setModel(comboDate);
    }

    void addUpdateListener(ActionListener listener){
        updateButton.addActionListener(listener);
    }

    void addComboBoxListener(ActionListener listener){
        comboBoxDate.addActionListener(listener);
    }

    void addPhotoDescriptionMouseAdapter(MouseAdapter adapter){
        photoDescriptionSaveButton.addMouseListener(adapter);
    }

    void setFirstElementOfComboBox(){
        comboBoxDate.setSelectedItem(comboDate.getElementAt(comboDate.getSize()-1));
    }
}