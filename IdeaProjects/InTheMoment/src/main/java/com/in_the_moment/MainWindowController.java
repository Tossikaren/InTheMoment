package com.in_the_moment;

import com.in_the_moment.Model.GsrMeasurement;
import com.in_the_moment.Model.Moment;
import com.in_the_moment.Model.Photo;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

public  class MainWindowController implements FileUtility{

    private MainWindow theView;
    private MySQLAccess theModel;

    public MainWindowController(MainWindow theView, MySQLAccess theModel){
        this.theView = theView;
        this.theModel = theModel;
        this.theView.addUpdateListener(new UpdateListener());
        setupComboBox();
        this.theView.addComboBoxListener(new ComboBoxListener());
        this.theView.setFirstElementOfComboBox();
        this.theView.addPhotoDescriptionMouseAdapter(new photoDescriptionMouseAdapter());

    }

    private void setupComboBox(){
        ArrayList<Date> arrayList = theModel.queryMomentDatesFromDatabase();
        theView.fillComboBox(arrayList);
    }

    public class UpdateListener implements ActionListener, ImageFilter, FileUtility, DateUtility {

        private String originalPhotoDirectoryBaseFolderPath = "E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0";
        //private String originalPhotoDirectoryBaseFolderPath = "C:\\Users\\Marti\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0";

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<PhotoDirectory> originalPhotoDirectoryLocations = scanPhotoFolderStructure();

            // Check each date to see if there already is processed a top 10 photo set for that date. If a date has no top 10 photo set check if there is a eda.csv file present for the date. If true process a top 10 photo set for the date using data from the csv file

            // Check if there is a top 10 photo set present for each date
            ArrayList<String> hasTopTen = new ArrayList<>();
            for (Date date: theModel.queryMomentDatesFromDatabase()) {
                String parsedDate = parseTopTenDate(date);
                hasTopTen.add(parsedDate);
            }

            for (String date : hasTopTen) {
                System.out.println("Does have top ten: " + date);
            }

            for (int i = 0; i < originalPhotoDirectoryLocations.size(); i++) {
                if (hasTopTen.contains(originalPhotoDirectoryLocations.get(i).photoDirectoryToString())) {
                    originalPhotoDirectoryLocations.remove(i);
                }
            }

            // Get list of GSR files
            File gsrFileDirectory = new File("C:\\In the Moment\\GSR files");
            Set<String> gsrFiles = directoryFileReader(gsrFileDirectory);

            // Compare list of GSR files with list with photo directories and crete list with photo directories to be deleted
            ArrayList<PhotoDirectory> photoDirectoriesToBeDeleted = new ArrayList<>();
            for (int i = 0; i < originalPhotoDirectoryLocations.size(); i++) {
                if (!gsrFiles.contains(originalPhotoDirectoryLocations.get(i).photoDirectoryToString() + ".csv")) {
                    photoDirectoriesToBeDeleted.add(originalPhotoDirectoryLocations.get(i));
                }
            }

            // Remove the Photo directories which did not have a corresponding GSR file
            for (PhotoDirectory photoDirectory : photoDirectoriesToBeDeleted) {
                originalPhotoDirectoryLocations.remove(photoDirectory);
                System.out.println("Delete: " + photoDirectory.photoDirectoryToString());
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
                    Date timeStampDateObjectFormat = new Date((long) Double.parseDouble(timeStampPartOfStringUnixFormat) * 1000);

                    // get measurement frequency from csv file
                    double frequency = Double.parseDouble(br.readLine());

                    // loop through measurements in csv file
                    while ((line = br.readLine()) != null) {

                        //DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
                        //String stringWithTimeStampDateObjectFormat = dateTimeFormat.format(timeStampDateObjectFormat);

                        //create measurement object and add it to list for GSR measurement objects
                        gsrMeasurements.add(new GsrMeasurement(Double.parseDouble(line), timeStampDateObjectFormat));

                        // increase timestamp with measurement frequency pr second (represented in milliseconds)
                        timeStampDateObjectFormat = new Date(timeStampDateObjectFormat.getTime() + 1000 / Math.round(frequency));
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

                // Save objects on list to Mysql database
                theModel.saveGsrMeasurementsToDatabase(listWithAllIncreasesInGsrMeasurements);

                ArrayList<Photo> photosInFolderArrayList = new ArrayList<>();
                File dir = new File(originalPhotoDirectoryBaseFolderPath + "\\" + photoDirectory.getYear() + "\\" + photoDirectory.getMonth() + "\\" + photoDirectory.getDay());
                if (dir.isDirectory()) { // make sure it's a directory
                    File[] files = dir.listFiles(IMAGE_FILTER);
                    if (files != null) {
                        for (final File file : files) {
                            Photo photo = new Photo(file.getName(), parsePhotoDate(file.getName()));
                            photosInFolderArrayList.add(photo);
                        }
                    } else {
                        //Todo: håndter hvis files er null
                    }
                }

                theModel.savePhotosToDatabase(photosInFolderArrayList);

                Photo currentClosestPhoto = null;
                ArrayList<Photo> finalPhotoList = new ArrayList<>();
                ArrayList<Moment> momentsArrayList = new ArrayList<>();
                for (int j = 0; j < 10; j++) {
                    long smallestDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderArrayList.get(0).getDateTime().getTime());
                    currentClosestPhoto = photosInFolderArrayList.get(0);

                    for (int k = 0; k < photosInFolderArrayList.size() - 1; k++) {
                        long currentDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderArrayList.get(k + 1).getDateTime().getTime());

                        if (currentDifference < smallestDifference) {
                            currentClosestPhoto = photosInFolderArrayList.get(k + 1);
                            smallestDifference = currentDifference;
                        }
                    }
                    finalPhotoList.add(currentClosestPhoto);
                    Moment moment = new Moment(listWithAllIncreasesInGsrMeasurements.get(j), currentClosestPhoto, currentClosestPhoto.getDateTime());

                    momentsArrayList.add(moment);
                }

                theModel.saveMomentToDatabase(momentsArrayList);

                // Create directory for top 10 photos
                new File("C:\\In the Moment\\Moments\\Images\\" + photoDirectory.photoDirectoryToString()).mkdirs();

                // Copy top 10 photos to new directory
                for (int j = 0; j < finalPhotoList.size(); j++) {
                    Path finalPhoto = Paths.get(originalPhotoDirectoryBaseFolderPath + "\\" + photoDirectory.getYear() + "\\" + photoDirectory.getMonth() + "\\" + photoDirectory.getDay() + "\\" + finalPhotoList.get(j).getUUID());
                    Path finalPhotoMove = Paths.get("C:\\In the Moment\\Moments\\Images\\" + photoDirectory.photoDirectoryToString() + "\\" + finalPhotoList.get(j).getUUID());
                    copyFileToDirectory(finalPhoto, finalPhotoMove);
                }

                // Save name of topten list to textfile
                try (FileWriter fileWriter = new FileWriter("C:\\In the Moment\\Moments\\topten.txt", true);
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                     PrintWriter line = new PrintWriter(bufferedWriter)) {
                    line.println(photoDirectory.photoDirectoryToString());
                    // Update combobox
                String newTopTendate = parseDate(photoDirectory.photoDirectoryToString());
                theView.comboDate.addElement(newTopTendate);
                } catch (IOException io) {
                }
            }
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
    }

    public class ComboBoxListener implements ActionListener, ImageFilter{

        DateFormat parseComboBoxDateFromStringToDate = new SimpleDateFormat("EEE dd. MMMM yyyy");

        @Override
        public void actionPerformed(ActionEvent e) {
            Date selectedDateAsDate = null;

            if (theView.getComboBoxDate().getSelectedItem() != null) {
                try {
                    selectedDateAsDate = new Date(parseComboBoxDateFromStringToDate.parse(theView.getComboBoxDate().getSelectedItem().toString()).getTime());
                } catch (ParseException pa) {
                    System.out.println("Get selected date from combobox: " + pa);
                }

                if(selectedDateAsDate != null) {
                    System.out.println("Fra combobox: " + selectedDateAsDate);
                }

                // Create listmodel with photo objects for the jlist view
                DefaultListModel<Moment> momentDefaultListModel = theModel.queryMomentsFromDatabase(selectedDateAsDate);

                theView.getJlist1().setModel(momentDefaultListModel);
                theView.getJlist1().setCellRenderer(new CustomPhotoRenderer());

                // Add first photo from jlist listmodel to jlabel view
                java.awt.image.BufferedImage myImage;
                SimpleDateFormat formatMomentDateFromDateToString = new SimpleDateFormat("yyyyMMdd");
                String photoFolderName = formatMomentDateFromDateToString.format(momentDefaultListModel.firstElement().getMomentDate());
                String photoFilename = momentDefaultListModel.firstElement().getPhoto().getFileName();
                try {
                    myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + photoFolderName + "\\" + photoFilename));
                    ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                    RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                    theView.getJlabel1().setIcon(rotatedImageIcon);
                } catch (IOException i) {
                }

                // Add description to first photo from jlist listmodel
                if (momentDefaultListModel.firstElement().getMomentDescription() != null) {
                    theView.getMomentDescriptionTextField().setText(momentDefaultListModel.firstElement().getMomentDescription());
                } else {
                    theView.getMomentDescriptionTextField().setText("Add description...");
                }

                MouseAdapter mouseAdapter = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if (e.getClickCount() >= 1) {

                            Moment selectedItem = (Moment) theView.getJlist1().getSelectedValue();
                            String photoFilename = theView.getJlist1().getSelectedValue().getPhoto().getFileName();
                            BufferedImage myImage;
                            try {
                                myImage = ImageIO.read(new File("C:\\In the Moment\\Moments\\Images\\" + photoFolderName + "\\" + photoFilename));
                                ImageIcon myImageAsIcon = new ImageIcon(new ImageIcon(myImage).getImage().getScaledInstance(816, 612, Image.SCALE_DEFAULT));
                                RotatedIcon rotatedImageIcon = new RotatedIcon(myImageAsIcon, RotatedIcon.Rotate.UP);
                                theView.getJlabel1().setIcon(rotatedImageIcon);
                            } catch (IOException er) {
                                System.out.println("JList Image Clicked: " + photoFilename + " " + er);
                            }

                            if (selectedItem.getMomentDescription() != null) {
                                theView.getMomentDescriptionTextField().setText(selectedItem.getMomentDescription());
                            } else {
                                theView.getMomentDescriptionTextField().setText("Add description...");
                            }
                        }
                    }
                };
                //jlist1.removeMouseListener(mouseAdapter);
                theView.getJlist1().addMouseListener(mouseAdapter);
            }
        }
    }

     public class photoDescriptionMouseAdapter extends MouseAdapter{

         String saveText;

         @Override
         public void mouseClicked (MouseEvent e){
             super.mouseClicked(e);
             saveText = theView.getMomentDescriptionTextField().getText();
             Moment selectedItem;
             if (!(theView.getJlist1().getSelectedValue() == null)) {
                 selectedItem = (Moment) theView.getJlist1().getSelectedValue();
             } else {
                 theView.getJlist1().setSelectedIndex(0);
                 selectedItem = (Moment) theView.getJlist1().getSelectedValue();
             }
             System.out.println(selectedItem.getPhoto().getFileName());
             selectedItem.setMomentDescription(saveText);

             // Save description to mysql database
             theModel.saveMomentDescriptionToDatabase(saveText, selectedItem.getMomentUUID());
         }
     }
}
