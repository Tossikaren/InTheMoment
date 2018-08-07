package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import com.in_the_moment.Model.GsrMeasurement;
import com.in_the_moment.Model.Photo;

public class MainWindow implements ImageFilter, FileUtility, DateUtility{

    private JFrame frame;
    private JTextField main_window_header;
    private JPanel main_window_main_panel;
    private JPanel selected_image_panel;
    private JScrollPane thumbnail_scroll_vindow;
    private JLabel jlabel1;
    private JList<Photo> jlist1;
    private JComboBox<String> comboBoxDate;
    private JButton updateButton;
    private JTextPane photoDescriptionTextField;
    private JButton photoDescriptionSaveButton;
    private JPanel JPanelLeft;
    private JPanel JPanelRight;
    private JScrollPane textDescriptionScroll;

    private String originalPhotoDirectoryBaseFolderPath = "E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0";
    private String topTenFolderPath = "C:\\In the Moment\\Moments\\topten.txt";

    public MainWindow(){
        frame = new JFrame("MainWindow");
        frame.setContentPane(main_window_main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat newDateTimeFormat = new SimpleDateFormat("EEE dd. MMMM yyyy");
        DefaultComboBoxModel<String> comboDate = new DefaultComboBoxModel<>();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<PhotoDirectory> originalPhotoDirectoryLocations = scanPhotoFolderStructure();

                // Check each date to see if there already is processed a top 10 photo set for that date. If a date has no top 10 photo set check if there is a eda.csv file present for the date. If true process a top 10 photo set for the date using data from the csv file

                // Check if there is a top 10 photo set present for each date
                ArrayList<String> hasTopTen = textFileReader(topTenFolderPath);
                systemOutArrayListLoop("Does have top ten: ", hasTopTen);

                for (int i = 0; i < originalPhotoDirectoryLocations.size(); i++) {
                    if (hasTopTen.contains(originalPhotoDirectoryLocations.get(i).photoDirectoryToString())){
                        originalPhotoDirectoryLocations.remove(i);
                    }
                }

                // Get list of GSR files
                File gsrFileDirectory = new File("C:\\In the Moment\\GSR files");
                ArrayList<String> gsrFiles = directoryFileReader(gsrFileDirectory);


                // Compare list of GSR files with list with photo directories and crete list with photo directories to be deleted
                ArrayList<PhotoDirectory> photoDirectoriesToBeDeleted = new ArrayList<>();
                for (int i = 0; i < originalPhotoDirectoryLocations.size(); i++) {
                    if (!gsrFiles.contains(originalPhotoDirectoryLocations.get(i).photoDirectoryToString() + ".csv")){
                        photoDirectoriesToBeDeleted.add(originalPhotoDirectoryLocations.get(i));
                    }
                }

                // Remove the Photo directories which did not have a corresponding GSR file
                for(PhotoDirectory photoDirectory : photoDirectoriesToBeDeleted){
                    originalPhotoDirectoryLocations.remove(photoDirectory);
                }

                // Process the top 10
                for (int i = 0; i < originalPhotoDirectoryLocations.size(); i++) {
                    PhotoDirectory photoDirectory = originalPhotoDirectoryLocations.get(i);

                    // read csv file with GSR measurements
                    ArrayList<GsrMeasurement> gsrMeasurements = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new FileReader("C:\\In the Moment\\GSR files\\" + photoDirectory.photoDirectoryToString() + ".csv"))) {
                        String line;

                        // get unix timestamp from csv file
                        String timeStampPartOfStringUnixFormat = br.readLine();

                        // convert unix timestamp to java.util.date object
                        java.util.Date timeStampDateObjectFormat = new java.util.Date((long) Double.parseDouble(timeStampPartOfStringUnixFormat) * 1000);

                        // get measurement frequency pr second from csv file
                        double frequency = Double.parseDouble(br.readLine());

                        // loop through measurements in csv file
                        while ((line = br.readLine()) != null) {

                            DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
                            String stringWithTimeStampDateObjectFormat = dateTimeFormat.format(timeStampDateObjectFormat);

                            //create measurement object and add it to list for GSR measurement objects
                            gsrMeasurements.add(new GsrMeasurement(Double.parseDouble(line), stringWithTimeStampDateObjectFormat));

                            // increase timestamp with measurement frequency pr second (represented in milliseconds)
                            timeStampDateObjectFormat.setTime(timeStampDateObjectFormat.getTime() + 1000 / Math.round(frequency));
                        }
                    } catch (IOException io) {
                    }

                    // Create list with top 10 highest GSR measurements. Create list with timestamps from all photos from that day. Foreach GSR measurement on 10 highest GSR measurement list compare timestamp with timestamps from list of photos to find the photo which was taken closest to the measurement

                    // Compare each measurement on the list of total gsr measurements with its adjacent measurements and create list of objects with all gsr measurement jumps including the size of the jump
                    ArrayList<GsrMeasurement> listWithAllIncreasesInGsrMeasurements = new ArrayList<>();
                    GsrMeasurement increasedGsrMeasurements;
                    for (int j = 0; j < gsrMeasurements.size() - 1; j++) {
                        int diff = gsrMeasurements.get(j + 1).getMeasurement() - gsrMeasurements.get(j).getMeasurement();

                        if (diff > 0) {
                            increasedGsrMeasurements = gsrMeasurements.get(j + 1);
                            increasedGsrMeasurements.setIncreaseDiff(diff);
                            listWithAllIncreasesInGsrMeasurements.add(increasedGsrMeasurements);
                        }
                    }

                    // Sort list with gsr measurement jumps to find the 10 highest jumps
                    listWithAllIncreasesInGsrMeasurements.sort(Comparator.comparing(GsrMeasurement::getIncreaseDiff).reversed());

                    DefaultListModel<Photo> photosInFolderListModel = new DefaultListModel<>();
                    File dir = new File(originalPhotoDirectoryBaseFolderPath + "\\" + photoDirectory.getYear() + "\\" + photoDirectory.getMonth() + "\\" + photoDirectory.getDay());
                    if (dir.isDirectory()) { // make sure it's a directory
                        File[] files = dir.listFiles(IMAGE_FILTER);
                        if (files != null) {
                            for (final File g : files) {
                                Photo newPhoto = new Photo(g.getName(), g.getName());
                                photosInFolderListModel.addElement(newPhoto);
                            }
                        } else{
                            //Todo: håndter hvis files er null
                        }
                    }

                    Photo currentClosest = null;
                    DefaultListModel<Photo> finalPhotoList = new DefaultListModel<>();

                    for (int j = 0; j < 10; j++) {
                        long smallestDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.firstElement().getDateTime().getTime());
                        currentClosest = photosInFolderListModel.firstElement();

                        for (int k = 0; k < photosInFolderListModel.getSize() - 1; k++) {
                            long currentDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.getElementAt(k + 1).getDateTime().getTime());

                            if (currentDifference < smallestDifference) {
                                currentClosest = photosInFolderListModel.getElementAt(k + 1);
                                smallestDifference = currentDifference;
                            }
                        }
                        finalPhotoList.addElement(currentClosest);
                    }

                    // Create directory for top 10 photos
                    new File("C:\\In the Moment\\Moments\\Images\\" + photoDirectory.photoDirectoryToString()).mkdirs();

                    // Copy top 10 photos to new directory
                    for (int j = 0; j < finalPhotoList.getSize(); j++) {
                        Path finalPhoto = Paths.get(originalPhotoDirectoryBaseFolderPath + "\\" + photoDirectory.getYear() + "\\" + photoDirectory.getMonth() + "\\" + photoDirectory.getDay() + "\\" + finalPhotoList.getElementAt(j).getID());
                        Path finalPhotoMove = Paths.get("C:\\In the Moment\\Moments\\Images\\" + photoDirectory.photoDirectoryToString() + "\\" + finalPhotoList.getElementAt(j).getID());
                        copyFileToDirectory(finalPhoto, finalPhotoMove);
                    }

                    // Save name of topten list to textfile
                    try (FileWriter fileWriter = new FileWriter("C:\\In the Moment\\Moments\\topten.txt", true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        PrintWriter line = new PrintWriter(bufferedWriter)) {
                        line.println(photoDirectory.photoDirectoryToString());
                        // Update combobox
                        String newTopTendate = parseDate(photoDirectory.photoDirectoryToString());
                        comboDate.addElement(newTopTendate);
                    } catch (IOException io) {
                    }
                }
            }
        });

        ArrayList<String> topTenListContents = textFileReader(topTenFolderPath);
        for(String line : topTenListContents){
            String date = parseDate(line);
            comboDate.addElement(date);
        }
        comboBoxDate.setModel(comboDate);

        //thumbnail_scroll_vindow.getVerticalScrollBar().setUnitIncrement(50);

        comboBoxDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comboBoxString = null;
                String folderName;
                if (comboBoxDate.getSelectedItem() != null) {
                    try {
                        comboBoxString = dateTimeFormat.format(newDateTimeFormat.parse(comboBoxDate.getSelectedItem().toString()));
                    } catch (ParseException pa) {
                        System.out.println("Get selected date from combobox: " + pa);
                    }
                    folderName = comboBoxString;
                    System.out.println("Fra combobox: " + folderName);

                    // Create listmodel with photo objects for the jlist view
                    DefaultListModel<Photo> photosInNewFolderListModel = new DefaultListModel<>();
                    File jListPhotos = new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\");
                    if (jListPhotos.isDirectory()) { // make sure it's a directory
                        for (final File fNew : jListPhotos.listFiles(IMAGE_FILTER)) {
                            Photo newPhoto = new Photo(fNew.getName(), fNew.getName());
                            photosInNewFolderListModel.addElement(newPhoto);
                        }
                    }

                    jlist1.setModel(photosInNewFolderListModel);
                    jlist1.setCellRenderer(new CustomPhotoRenderer(folderName));

                    // Add first photo from jlist listmodel to jlabel view
                    BufferedImage myImage;
                    try {
                        myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + folderName + "\\" + photosInNewFolderListModel.firstElement().getID()));
                        ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                        RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                        jlabel1.setIcon(rotatedImageIcon);
                        String photoDescription;
                        File file = new File("C:\\In the Moment\\Moments\\Descriptions\\" + photosInNewFolderListModel.firstElement().getID() + ".txt");
                        if (file.exists()) {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                            if ((photoDescription = bufferedReader.readLine()) != null) {
                                photosInNewFolderListModel.firstElement().setPhotoDescription(photoDescription);
                                System.out.println("Photo description read: " + photoDescription);
                            }
                        }
                        if (photosInNewFolderListModel.firstElement().getPhotoDescription() == null) {
                            photoDescriptionTextField.setText("Add description...");
                        } else {
                            photoDescriptionTextField.setText(photosInNewFolderListModel.firstElement().getPhotoDescription());
                        }

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
                                    ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                                    RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                                    jlabel1.setIcon(rotatedImageIcon);
                                    String photoDescription;
                                    File file = new File("C:\\In the Moment\\Moments\\Descriptions\\" + selectedItem.getID() + ".txt");
                                    if (file.exists()) {
                                        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                                        if ((photoDescription = bufferedReader.readLine()) != null) {
                                            selectedItem.setPhotoDescription(photoDescription);
                                            System.out.println("Photo description read: " + photoDescription);
                                        }
                                    }
                                    if (selectedItem.getPhotoDescription() == null) {
                                        photoDescriptionTextField.setText("Add description...");
                                    } else {
                                        photoDescriptionTextField.setText(selectedItem.getPhotoDescription());
                                    }
                                } catch (IOException er) {
                                    System.out.println("JList Image Clicked: " + folderName + " " + er);
                                }
                            }
                        }
                    };
                    //jlist1.removeMouseListener(mouseAdapter);
                    jlist1.addMouseListener(mouseAdapter);
                }
            }
        });
        comboBoxDate.setSelectedItem(comboDate.getElementAt(comboDate.getSize()-1));

        Border customBorderRose = BorderFactory.createLineBorder(Color.decode("#D46A6A"));
        main_window_header.setBorder(customBorderRose);
        thumbnail_scroll_vindow.setBorder(customBorderRose);
        //photoDescriptionTextField.setBorder();
        photoDescriptionSaveButton.addMouseListener(new MouseAdapter() {
            String saveText;

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                saveText = photoDescriptionTextField.getText();
                Photo selectedItem;
                if (!(jlist1.getSelectedValue() == null)) {
                    selectedItem = (Photo) jlist1.getSelectedValue();
                }else {
                    jlist1.setSelectedIndex(0);
                    selectedItem = (Photo) jlist1.getSelectedValue();
                }
                System.out.println(selectedItem.getID());
                selectedItem.setPhotoDescription(saveText);

                // Save description to text file
                try (FileWriter photoDescriptionTextFieldfw = new FileWriter("C:\\In the Moment\\Moments\\Descriptions\\" + selectedItem.getID() + ".txt");
                     BufferedWriter photoDescriptionTextFieldbw = new BufferedWriter(photoDescriptionTextFieldfw);
                     PrintWriter photoDescriptionTextFieldline = new PrintWriter(photoDescriptionTextFieldbw)) {
                    photoDescriptionTextFieldline.println(saveText);
                } catch (IOException io) {

                }
            }
        });
    }

    private ArrayList<PhotoDirectory> scanPhotoFolderStructure() {
        ArrayList<PhotoDirectory> listOfDirectoriesToReturn = new ArrayList<>();
        try {
            File directoriesYear = new File(originalPhotoDirectoryBaseFolderPath);
            if (directoriesYear.isDirectory()) {
                for (final File directoryYear : directoriesYear.listFiles()) {
                    if (directoryYear.isDirectory()) {
                        File directoriesMonth = new File(originalPhotoDirectoryBaseFolderPath + "\\" + directoryYear.getName());
                        if (directoriesMonth.isDirectory()) {
                            for (final File directoryMonth : directoriesMonth.listFiles()) {
                                if (directoryMonth.isDirectory()) {
                                    File directoriesDay = new File(originalPhotoDirectoryBaseFolderPath + "\\" + directoryYear.getName() + "\\" + directoryMonth.getName());
                                    if (directoriesDay.isDirectory()) {
                                        for (final File directoryDay : directoriesDay.listFiles()) {
                                            listOfDirectoriesToReturn.add(new PhotoDirectory(directoryYear.getName(), directoryMonth.getName(), directoryDay.getName()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException n) {
        }
        return listOfDirectoriesToReturn;
    }

    private void systemOutArrayListLoop(String string, ArrayList arrayList){
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println( string + arrayList.get(i));
        }
    }

    public void showMainWindow(){
        frame.setVisible(true);
    }
}