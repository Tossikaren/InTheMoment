package com.in_the_moment;

import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
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
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainWindow {
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList jlist1;

    static final File dir = new File("C:\\java_image_test");
    static final File dir2 = new File("C:\\java_image_test\\new\\");

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

    private MainWindow(){

        // create list for GSR measurement objects
        DefaultListModel<GsrMeasurement> gsrMeasurementListModel = new DefaultListModel<>();

        // read csv file with GSR measurements
        try(BufferedReader br = new BufferedReader(new FileReader("C:\\java_gsr_test\\EDA.csv"))){
            String line;

            // get unix timestamp from csv file
            String timeStampPartOfStringUnixFormat = br.readLine();
            System.out.println("Unix timestamp from csv file: " + timeStampPartOfStringUnixFormat);

            // convert unix timestamp to java.util.date object
            java.util.Date timeStampDateObjectFormat = new java.util.Date((long)Double.parseDouble(timeStampPartOfStringUnixFormat)*1000);
            System.out.println("Unix timestamp converted to java.util.Date object: " + timeStampDateObjectFormat);

            // get measurement frequency pr second from csv file
            double frequency = Double.parseDouble(br.readLine());

            int i = 0;

            // loop through measurements in csv file
            while((line = br.readLine()) !=null){

                DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
                String stringWithTimeStampDateObjectFormat = dateTimeFormat.format(timeStampDateObjectFormat);

                //create measurement object and add it to list for GSR measurement objects
                gsrMeasurementListModel.addElement(new GsrMeasurement(Double.parseDouble(line), stringWithTimeStampDateObjectFormat, i));

                // increase timestamp with measurement frequency pr second (represented in milliseconds)
                timeStampDateObjectFormat.setTime(timeStampDateObjectFormat.getTime() + 1000 / Math.round(frequency));
                //System.out.println("Measurement timestamp In the Moment format: " + gsrMeasurementListModel.getElementAt(i).getMeasurement() + " " + new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(gsrMeasurementListModel.getElementAt(i).getDateTime()) + " " + gsrMeasurementListModel.getElementAt(i).getI());

                i++;
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

        /*for(int j = 0; j < gsrMeasurementListModel.getSize();j++) {
            System.out.println("Measurement timestamp In the Moment format: " + gsrMeasurementListModel.getElementAt(j).getMeasurement() + " " + gsrMeasurementListModel.getElementAt(j).getDateTime());
        }*/

        DefaultListModel<Photo> photosInFolderListModel = new DefaultListModel<>();

        if (dir.isDirectory()) { // make sure it's a directory
            for (final File f : dir.listFiles(IMAGE_FILTER)) {

                    Photo newPhoto = new Photo(f.getName(), f.getName());
                    photosInFolderListModel.addElement(newPhoto);
                    //System.out.println(photosInFolderListModel.toString());
            }
        }

        // Create list with top 10 highest GSR measurements. Create list with timestamps from all photos from that day. Foreach GSR measurement on 10 highest GSR measurement list compare timestamp with timestamps from list of photos to find the photo which was taken closest to the measurement

        // create list for highest GSR measurement objects
        DefaultListModel<GsrMeasurement> highestGsrMeasurementListModel = new DefaultListModel<>();

        highestGsrMeasurementListModel.addElement(gsrMeasurementListModel.getElementAt(4));
        System.out.println("First element from list with highest GSR measurements: " + highestGsrMeasurementListModel.firstElement().getMeasurement() + " " + highestGsrMeasurementListModel.firstElement().getDateTime() + " " + highestGsrMeasurementListModel.firstElement().getI());

        Photo currentClosest = null;
        long smallestDifference = Math.abs(highestGsrMeasurementListModel.firstElement().getDateTime().getTime() - photosInFolderListModel.firstElement().getDateTime().getTime());
        for (int j = 0;  j < highestGsrMeasurementListModel.getSize(); j++) {

            for (int i = 0; i < photosInFolderListModel.getSize(); i++) {
                //System.out.println("Highest: " + highestGsrMeasurementListModel.firstElement().getDateTime().getTime());
                //System.out.println("The rest: " + photosInFolderListModel.getElementAt(i).getDateTime().getTime());
                long currentDifference = Math.abs(highestGsrMeasurementListModel.getElementAt(j).getDateTime().getTime() - photosInFolderListModel.getElementAt(i).getDateTime().getTime());
                System.out.println("Diff: " + currentDifference);

                if (currentDifference < smallestDifference) {
                    currentClosest = photosInFolderListModel.getElementAt(i);
                    smallestDifference = currentDifference;
                    System.out.println("Smallest difference: " + smallestDifference);
                    System.out.println(currentClosest.getID() + " " + currentClosest.getDateTime());
                }
            }
        }

        Path finalPhoto = Paths.get("c:\\java_image_test\\" + currentClosest.getID());
        Path finalPhotoMove = Paths.get("c:\\java_image_test\\new\\" + currentClosest.getID());
        System.out.println("Final photo: " + finalPhoto);
        System.out.println("Exists? " + Files.exists(finalPhoto));

        try {
            Files.copy(finalPhoto, finalPhotoMove, REPLACE_EXISTING);
        }catch (IOException e){
        }

        DefaultListModel<Photo> photosInNewFolderListModel = new DefaultListModel<>();

        if (dir2.isDirectory()) { // make sure it's a directory
            for (final File fNew : dir2.listFiles(IMAGE_FILTER)) {

                Photo newPhoto = new Photo(fNew.getName(), fNew.getName());
                photosInNewFolderListModel.addElement(newPhoto);
                System.out.println("New folder contents: " + photosInNewFolderListModel.toString());
            }
        }

        jlist1.setModel(photosInNewFolderListModel);
        jlist1.setCellRenderer(new CustomPhotoRenderer());

        BufferedImage myImage;
        try {
            myImage = ImageIO.read(new File("C:\\java_image_test\\new\\" + photosInNewFolderListModel.firstElement().getID()));
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
                        myImage = ImageIO.read(new File("C:\\java_image_test\\new\\" + selectedItem.getID()));
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