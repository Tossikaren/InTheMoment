package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomPhotoRenderer extends JLabel implements ListCellRenderer<Photo> {

        public CustomPhotoRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Photo> list, Photo photo, int index,
                                                      boolean isSelected, boolean cellHasFocus) {

            String id = photo.getID();
            photo.setDateTime(photo.getID());
            String dateTime = photo.getDateTime();

            BufferedImage myImage;
            try {
                myImage = ImageIO.read(new File("C:\\javaimagetest\\" + id));
                ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon (myImage).getImage().getScaledInstance(228, 171, Image.SCALE_DEFAULT));
                //ImageIcon resourceImage = new ImageIcon(getClass().getResource("/images/20180320_182947_000.jpg"));

                System.out.println("CustomPhotoRenderer: " + myImage.toString());

                RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);

                setIcon(rotatedImageIcon);
                setText(dateTime);

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