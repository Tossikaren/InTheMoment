package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPhotoRenderer extends JLabel implements ListCellRenderer<Photo> {

        private Object folderName;

        public CustomPhotoRenderer(Object folderName) {
            this.folderName = folderName;
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Photo> list, Photo photo, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            this.setBorder(new EmptyBorder(10, 10, 10, 10));

            String id = photo.getID();
            photo.setDateTime(photo.getID());
            Date dateTime = photo.getDateTime();

            BufferedImage myImage;
            try {
                myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\" + id));
                ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon (myImage).getImage().getScaledInstance(228, 171, Image.SCALE_DEFAULT));
                //ImageIcon resourceImage = new ImageIcon(getClass().getResource("/images/20180320_182947_000.jpg"));

                RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);

                setIcon(rotatedImageIcon);
                setText(new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy").format(dateTime));

            } catch (IOException e) {
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }