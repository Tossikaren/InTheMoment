package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainWindow {
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
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList jlist1;
    private JComboBox comboBoxDate;
    private JButton updateButton;

    private MainWindow(){

        ArrayList<ArrayList<String>> originalPhotoDataArray = new ArrayList<ArrayList<String>>();
        File originalPhotosDirectoryYear = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0");
        try {
            int i = 0;
            if (originalPhotosDirectoryYear.isDirectory()) {
                for (final File f : originalPhotosDirectoryYear.listFiles()) {
                    if (f.isDirectory()) {
                        File directoryMonth = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + f.getName());
                        if (directoryMonth.isDirectory()) {
                            for (final File g : directoryMonth.listFiles()) {
                                if (g.isDirectory()) {
                                    File directoryDay = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + f.getName() + "\\" + g.getName());
                                    if (directoryDay.isDirectory()) {
                                        for (final File h : directoryDay.listFiles()) {
                                            /*System.out.println("Original photo data: " + f.getName() + " " + g.getName() + " " + h.getName());*/
                                            originalPhotoDataArray.add(new ArrayList<String>());
                                            originalPhotoDataArray.get(i).add(f.getName());
                                            originalPhotoDataArray.get(i).add(g.getName());
                                            originalPhotoDataArray.get(i).add(h.getName());
                                            i++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException n){
        }

        // Check each date to see if there already is processed a top 10 photo set for that date. If a date has no top 10 photo set check if there is a eda.csv file present for the date. If true process a top 10 photo set for the date using data from the csv file

        // Check if there is a top 10 photo set present for each date
        ArrayList<String> hasTopTen = new ArrayList<>();
        ArrayList<String> doesNotHaveTopTen = new ArrayList<>();
        try {
            File file = new File("C:\\In the Moment\\topten.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                hasTopTen.add(st);
            }
        }catch (IOException io){
        }

        for (int i = 0; i < hasTopTen.size(); i++){
            System.out.println("Does have top ten: " + hasTopTen.get(i));
        }

        for(int i = 0; i < originalPhotoDataArray.size(); i++) {
            /*System.out.println("Her: " + originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2));*/
            if (hasTopTen.size() == 0) {
                doesNotHaveTopTen.add(originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2));
            } else {
                boolean match = false;
                for (int j = 0; j < hasTopTen.size(); j++) {
                    if ((originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2)).equals(hasTopTen.get(j))) {
                        match = true;
                    }

                }
                if(!match){
                    doesNotHaveTopTen.add(originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2));
                    System.out.println("Does not have top ten: " + originalPhotoDataArray.get(i));
                }
            }
        }

        for(int i = 0; i < doesNotHaveTopTen.size(); i++){
            System.out.println("Does not have topten list: " + doesNotHaveTopTen.get(i));
        }

        // Check if the csv file is present
        File checkGsrFileDirectory = new File("C:\\In the Moment\\GSR files");
        for(int i = 0; i < doesNotHaveTopTen.size(); i++) {
            String date = doesNotHaveTopTen.get(i);
            //System.out.println("What is the date of missing top ten: " + date);
            for (final File f : checkGsrFileDirectory.listFiles()) {
                //System.out.println("CSV filename: " + f.getName());
                System.out.println("Equals? : " + date + ".csv == " + f.getName());
                if ((date + ".csv").equals(f.getName())) {
                    System.out.println("It's true woo hoo: " + date);

                    // Process the top 10

                    // create list for GSR measurement objects
                    DefaultListModel<GsrMeasurement> gsrMeasurementListModel = new DefaultListModel<>();

                    // read csv file with GSR measurements
                    try(BufferedReader br = new BufferedReader(new FileReader("C:\\In the Moment\\GSR files\\" + f.getName()))){
                        String line;

                        // get unix timestamp from csv file
                        String timeStampPartOfStringUnixFormat = br.readLine();
                        //System.out.println("Unix timestamp from csv file: " + timeStampPartOfStringUnixFormat);

                        // convert unix timestamp to java.util.date object
                        java.util.Date timeStampDateObjectFormat = new java.util.Date((long)Double.parseDouble(timeStampPartOfStringUnixFormat) * 1000 );
                        //System.out.println("Unix timestamp converted to java.util.Date object: " + timeStampDateObjectFormat);

                        // get measurement frequency pr second from csv file
                        double frequency = Double.parseDouble(br.readLine());

                        // loop through measurements in csv file
                        while((line = br.readLine()) !=null){

                            DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
                            String stringWithTimeStampDateObjectFormat = dateTimeFormat.format(timeStampDateObjectFormat);

                            //create measurement object and add it to list for GSR measurement objects
                            gsrMeasurementListModel.addElement(new GsrMeasurement(Double.parseDouble(line), stringWithTimeStampDateObjectFormat));

                            // increase timestamp with measurement frequency pr second (represented in milliseconds)
                            timeStampDateObjectFormat.setTime(timeStampDateObjectFormat.getTime() + 1000 / Math.round(frequency));
                            //System.out.println("Measurement timestamp In the Moment format: " + gsrMeasurementListModel.getElementAt(i).getMeasurement() + " " + new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(gsrMeasurementListModel.getElementAt(i).getDateTime()) + " " + gsrMeasurementListModel.getElementAt(i).getI());
                        }
                    } catch (IOException e){

                    }

                    /*for(int j = 0; j < gsrMeasurementListModel.getSize();j++) {
                        System.out.println("Measurement timestamp In the Moment format: " + gsrMeasurementListModel.getElementAt(j).getMeasurement() + " " + gsrMeasurementListModel.getElementAt(j).getDateTime());
                    }*/

                    // Create list with top 10 highest GSR measurements. Create list with timestamps from all photos from that day. Foreach GSR measurement on 10 highest GSR measurement list compare timestamp with timestamps from list of photos to find the photo which was taken closest to the measurement

                    // Compare each measurement on the list of total gsr measurements with its adjacent measurements and create list of objects with all gsr measurement jumps including the size of the jump
                    ArrayList<GsrMeasurement> listWithAllIncreasesInGsrMeasurements = new ArrayList<GsrMeasurement>();
                    GsrMeasurement increasedGsrMeasurements;
                    for (int j = 0; j < gsrMeasurementListModel.size() -1; j++) {
                        int diff = gsrMeasurementListModel.getElementAt(j + 1).getMeasurement() - gsrMeasurementListModel.getElementAt(j).getMeasurement();

                        //System.out.println("All diffs: " + diff);
                        if(diff > 0){
                        /*System.out.println(gsrMeasurementListModel.getElementAt(i).getMeasurement());
                        System.out.println(gsrMeasurementListModel.getElementAt(i+1).getMeasurement());
                        System.out.println("All diffs: " + diff);*/
                            increasedGsrMeasurements = gsrMeasurementListModel.getElementAt(j + 1);
                            increasedGsrMeasurements.setIncreaseDiff(diff);
                            listWithAllIncreasesInGsrMeasurements.add(increasedGsrMeasurements);
                        }
                    }

                    // Compare all gsr measurement jumps on the list of objects with gsr measurement jumps to find the 10 highest jumps

                    // Reverse sort list with the increased gsr measurement objects
                    listWithAllIncreasesInGsrMeasurements.sort(Comparator.comparing(GsrMeasurement::getIncreaseDiff).reversed());

                    // Write list to console to check contents
                    for(int j = 0; j < 10; j++){
                        System.out.println("Increased GSR measurement: " + listWithAllIncreasesInGsrMeasurements.get(j).getMeasurement() + " " + listWithAllIncreasesInGsrMeasurements.get(j).getIncreaseDiff() + " " + listWithAllIncreasesInGsrMeasurements.get(j).getDateTime() + " " + j);
                    }

                    DefaultListModel<Photo> photosInFolderListModel = new DefaultListModel<>();
                    File dir = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + originalPhotoDataArray.get(i).get(0) + "\\" + originalPhotoDataArray.get(i).get(1) + "\\" + originalPhotoDataArray.get(i).get(2));
                    /*File dir = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\2018\\03\\30");*/
                    if (dir.isDirectory()) { // make sure it's a directory
                        for (final File g : dir.listFiles(IMAGE_FILTER)) {

                            Photo newPhoto = new Photo(g.getName(), g.getName());
                            photosInFolderListModel.addElement(newPhoto);
                            //System.out.println(photosInFolderListModel.toString());
                        }
                    }

                    Photo currentClosest = null;
                    DefaultListModel<Photo> finalPhotoList = new DefaultListModel<>();

                    for (int j = 0;  j < 10; j++) {

                        long smallestDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.firstElement().getDateTime().getTime());
                        currentClosest = photosInFolderListModel.firstElement();

                        for (int k = 0; k < photosInFolderListModel.getSize() - 1; k++) {
                            long currentDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.getElementAt(k + 1).getDateTime().getTime());
                            //System.out.println("Diff: " + currentDifference);

                            if (currentDifference < smallestDifference){
                                currentClosest = photosInFolderListModel.getElementAt(k + 1);
                                smallestDifference = currentDifference;
                                //System.out.println("Smallest difference: " + smallestDifference);
                                //System.out.println(currentClosest.getID() + " " + currentClosest.getDateTime());
                            }
                        }
                        finalPhotoList.addElement(currentClosest);
                    }

                    // Create directory for top 10 photos
                    new File("C:\\In the Moment\\Moments\\Images\\" + originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2)).mkdirs();

                    // Copy top 10 photos to new directory
                    for(int j = 0; j < finalPhotoList.getSize(); j++) {
                        Path finalPhoto = Paths.get("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + originalPhotoDataArray.get(i).get(0) + "\\" + originalPhotoDataArray.get(i).get(1) + "\\" + originalPhotoDataArray.get(i).get(2) + "\\" + finalPhotoList.getElementAt(j).getID());
                        Path finalPhotoMove = Paths.get("C:\\In the Moment\\Moments\\Images\\" + originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2) + "\\" + finalPhotoList.getElementAt(j).getID());
                        System.out.println("Final photo: " + finalPhoto);
                        System.out.println("Exists? " + Files.exists(finalPhoto));
                        try {
                            Files.copy(finalPhoto, finalPhotoMove, REPLACE_EXISTING);
                        } catch (IOException e) {
                        }
                    }

                    // Save name of topten list to textfile
                    try(FileWriter fw = new FileWriter("C:\\In the Moment\\topten.txt", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter line = new PrintWriter(bw))
                    {
                        line.println(originalPhotoDataArray.get(i).get(0) + originalPhotoDataArray.get(i).get(1) + originalPhotoDataArray.get(i).get(2));
                    } catch (IOException e) {

                    }
                }
            }
        }

        /*DefaultComboBoxModel<String> comboYear = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> comboMonth = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> comboDay = new DefaultComboBoxModel<>();*/
        DefaultComboBoxModel<String> comboDate = new DefaultComboBoxModel<>();

        ArrayList<String> topTens = new ArrayList<>();
        try {
            File file = new File("C:\\In the Moment\\topten.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                comboDate.addElement(st);
            }
        }catch (IOException io){
        }

        /*for (int i = 0; i < topTens.size(); i++){

        }*/

        comboBoxDate.setModel(comboDate);

        comboBoxDate.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object folderName = e.getItem();

                // Create listmodel with photo objects for the jlist view
                DefaultListModel<Photo> photosInNewFolderListModel = new DefaultListModel<>();
                File jListPhotos = new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\");
                if (jListPhotos.isDirectory()) { // make sure it's a directory
                    for (final File fNew : jListPhotos.listFiles(IMAGE_FILTER)) {


                        Photo newPhoto = new Photo(fNew.getName(), fNew.getName());
                        photosInNewFolderListModel.addElement(newPhoto);
                        //System.out.println("New folder contents: " + photosInNewFolderListModel.toString());
                    }
                }

                jlist1.setModel(photosInNewFolderListModel);
                jlist1.setCellRenderer(new CustomPhotoRenderer(folderName));

                // Add first photo from jlist listmodel to jlabel view
                BufferedImage myImage;
                try {
                    myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\" + photosInNewFolderListModel.firstElement().getID()));
                    ImageIcon myImageAsIcon = new ImageIcon (new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                    RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                    jlabel1.setIcon(rotatedImageIcon);

                } catch (IOException i) {
                }
                MouseAdapter mouseAdapter = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount() >= 1) {

                            Photo selectedItem = (Photo) jlist1.getSelectedValue();
                            BufferedImage myImage;

                            try {
                                myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\" + selectedItem.getID()));
                                ImageIcon myImageAsIcon = new ImageIcon (new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                                RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                                jlabel1.setIcon(rotatedImageIcon);

                            } catch (IOException er) {
                                System.out.println("JList Image Clicked: " + folderName + " " + er);
                            }
                        }
                    }
                };
                //jlist1.removeMouseListener(mouseAdapter);
                jlist1.addMouseListener(mouseAdapter);

                
            }
        });

        /*File directoryYear = new File("C:\\In the Moment\\Moments\\Images");
        try {
            if (directoryYear.isDirectory()) {
                for (final File f : directoryYear.listFiles()) {
                    if (f.isDirectory()) {
                        //comboYear.addElement(f.getName());
                        File directoryMonth = new File("C:\\In the Moment\\Moments\\Images\\" + f.getName());
                        if (directoryMonth.isDirectory()) {
                            for (final File g : directoryMonth.listFiles()) {
                                if (g.isDirectory()) {
                                    //comboMonth.addElement(g.getName());
                                    File directoryDay = new File("C:\\In the Moment\\Moments\\Images\\" + f.getName() + "\\" + g.getName());
                                    if (directoryDay.isDirectory()) {
                                        for (final File h : directoryDay.listFiles()) {
                                            //comboDay.addElement(h.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (NullPointerException n){

        }*/

        /*comboBoxYear.setModel(comboYear);
        comboBoxMonth.setModel(comboMonth);
        comboBoxDay.setModel(comboDay);*/

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().main_window_main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}