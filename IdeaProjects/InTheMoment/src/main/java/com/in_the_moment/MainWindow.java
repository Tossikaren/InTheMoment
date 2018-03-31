package com.in_the_moment;

import com.opencsv.CSVReader;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;

public class MainWindow {
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList jlist1;

    static final File dir = new File("C:\\java_image_test");

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

        DefaultListModel<GsrMeasurement> gsrMeasurementlistModel = new DefaultListModel<>();

        try(FileInputStream inputStream = new FileInputStream("C:\\java_gsr_test\\EDA.csv")) {
            String everythingFromEdaFile = IOUtils.toString(inputStream);
            String timeStampPartOfStringUnixFormat = everythingFromEdaFile.substring(0, Math.min(10, everythingFromEdaFile.length()));
            java.util.Date timeStampDateObjectFormat =new java.util.Date((long)Integer.parseInt(timeStampPartOfStringUnixFormat)*1000);
            
            System.out.println(new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy").format(timeStampDateObjectFormat));

            BufferedReader br = new BufferedReader(new FileReader("C:\\java_gsr_test\\EDA.csv"));
            String line;
            //String csvSplitBy = ",";
            while((line = br.readLine()) !=null){
                //String[] testy = line.split(csvSplitBy);

                GsrMeasurement gsrMeasurement = new GsrMeasurement(Double.parseDouble(line), timeStampDateObjectFormat);
                gsrMeasurementlistModel.addElement(gsrMeasurement);
                timeStampDateObjectFormat.setTime(timeStampDateObjectFormat.getTime() + 250);
                System.out.println(new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(timeStampDateObjectFormat));
            }
        } catch (IOException e){

        }

        /*try (CSVReader reader = new CSVReader(new FileReader("C:\\java_gsr_test\\EDA.csv"))) {
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(nextLine);
            }
        }catch (IOException e){

        }*/

        DefaultListModel<Photo> listModel = new DefaultListModel<>();

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {

                    Photo newPhoto = new Photo(f.getName());
                    listModel.addElement(newPhoto);
                    System.out.println(listModel.toString());
            }
        }
        //jlist1.setBorder(new EmptyBorder(10,10, 10, 10));
        jlist1.setModel(listModel);
        jlist1.setCellRenderer(new CustomPhotoRenderer());

        BufferedImage myImage;
        try {
            myImage = ImageIO.read(new File("C:\\java_image_test\\" + listModel.firstElement().getID()));
            ImageIcon myImageAsIcon = new ImageIcon (new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
            RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
            jlabel1.setIcon(rotatedImageIcon);

        } catch (IOException e) {
        }

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) {

                    Photo selectedItem = (Photo) jlist1.getSelectedValue();
                    BufferedImage myImage;

                    try {
                        myImage = ImageIO.read(new File("C:\\java_image_test\\" + selectedItem.getID()));
                        ImageIcon myImageAsIcon = new ImageIcon (new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
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
    }
}