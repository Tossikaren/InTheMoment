package com.in_the_moment;

import com.in_the_moment.Model.Photo;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomPhotoRenderer extends JLabel implements com.in_the_moment.BufferedImage,  ListCellRenderer<Photo>  {
    private final String folderName;

    public CustomPhotoRenderer(String folderName) {
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

            BufferedImage scaled = scale(myImage, 0.07);
            ImageIcon myImageAsIcon = new ImageIcon(scaled);
            //ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon (myImage).getImage().getScaledInstance(228, 171, Image.SCALE_DEFAULT));

            RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);

            setIcon(rotatedImageIcon);
            setText(new SimpleDateFormat("HH:mm:ss").format(dateTime));

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