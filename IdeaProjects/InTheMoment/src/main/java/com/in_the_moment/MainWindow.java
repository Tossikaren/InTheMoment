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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private MainWindow(){

        // create list for GSR measurement objects
        DefaultListModel<GsrMeasurement> gsrMeasurementListModel = new DefaultListModel<>();

        /*try(FileInputStream inputStream = new FileInputStream("C:\\java_gsr_test\\EDA.csv")) {
            String everythingFromEdaFile = IOUtils.toString(inputStream);
            String timeStampPartOfStringUnixFormat = everythingFromEdaFile.substring(0, Math.min(10, everythingFromEdaFile.length()));
            java.util.Date timeStampDateObjectFormat =new java.util.Date((long)Integer.parseInt(timeStampPartOfStringUnixFormat)*1000);
            System.out.println(new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy").format(timeStampDateObjectFormat));*/

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

        try {
            DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
            Date date = dateTimeFormat.parse(gsrMeasurementListModel.firstElement().getDateTime());
            System.out.println("Test java util: " + date);
            System.out.println("Test dato format: " + new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(date));
        }catch (ParseException p) {
        }


        //gsrMeasurementListModel.remove(0);
        for(int j = 0; j < 430;j++) {
            //System.out.println("Measurement timestamp In the Moment format: " + gsrMeasurementListModel.getElementAt(j).getMeasurement() + " " + gsrMeasurementListModel.getElementAt(j).getDateTime());
        }
        // create list for highest GSR measurement objects
        DefaultListModel<GsrMeasurement> highestGsrMeasurementListModel = new DefaultListModel<>();

        //GsrMeasurement gsrMeasurement1 = new GsrMeasurement()
        highestGsrMeasurementListModel.addElement(gsrMeasurementListModel.getElementAt(4));
        System.out.println("First element from list with highest GSR measurements: " + highestGsrMeasurementListModel.firstElement().getMeasurement() + " " + highestGsrMeasurementListModel.firstElement().getDateTime() + " " + highestGsrMeasurementListModel.firstElement().getI());

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
                    //System.out.println(listModel.toString());
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