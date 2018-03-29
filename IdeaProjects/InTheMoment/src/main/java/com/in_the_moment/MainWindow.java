package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainWindow {
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList jlist1;

    static final File dir = new File("C:\\javaimagetest");

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    public MainWindow(){

        DefaultListModel<Photo> listModel = new DefaultListModel<>();

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {
                BufferedImage img;

                try {

                    Photo newPhoto = new Photo(f.getName());
                    listModel.addElement(newPhoto);
                    System.out.println(listModel.toString());
                    img = ImageIO.read(f);
                    //ImageIcon bufferedImg = new ImageIcon(img);

                    //System.out.println("image: " + f.getName());
                    System.out.println(" width : " + img.getWidth());
                    System.out.println(" height: " + img.getData());
                    System.out.println(" size  : " + f.length());

                } catch (final IOException e) {
                    // handle errors here
                }
            }
        }
        jlist1.setModel(listModel);
        jlist1.setCellRenderer(new CustomPhotoRenderer());

        BufferedImage myImage;
        try {
            myImage = ImageIO.read(new File("C:\\javaimagetest\\" + listModel.firstElement().getID()));
            ImageIcon myImageAsIcon = new ImageIcon(myImage);
            RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
            jlabel1.setIcon(rotatedImageIcon);

        } catch (IOException e) {
        }

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {

                    Photo selectedItem = (Photo) jlist1.getSelectedValue();
                    BufferedImage myImage;

                    try {
                        myImage = ImageIO.read(new File("C:\\javaimagetest\\" + selectedItem.getID()));
                        ImageIcon myImageAsIcon = new ImageIcon(myImage);
                        RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                        jlabel1.setIcon(rotatedImageIcon);

                    } catch (IOException er) {
                    }

                }
            }
        };
        jlist1.addMouseListener(mouseListener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().main_window_main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //System.out.println("See this?");
    }

}
