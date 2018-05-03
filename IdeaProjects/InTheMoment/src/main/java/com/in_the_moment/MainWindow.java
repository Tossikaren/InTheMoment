package com.in_the_moment;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MainWindow {

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

    public static URLConnection login(String _url, String _username, String _password) throws IOException, MalformedURLException {

        String data = URLEncoder.encode("Usuario", "UTF-8") + "=" + URLEncoder.encode(_username, "UTF-8");
        data += "&" + URLEncoder.encode("Contrase", "UTF-8") + "=" + URLEncoder.encode(_password, "UTF-8");

        // Send data
        URL url = new URL(_url);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        wr.close();



        return conn;
    }

    private MainWindow(){

        DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat newDateTimeFormat = new SimpleDateFormat("EEE dd. MMMM yyyy");
        DefaultComboBoxModel<String> comboDate = new DefaultComboBoxModel<>();

        /*try {
            *//* turn off annoying htmlunit warnings *//*
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            //webClient.waitForBackgroundJavaScript(3000);
            //webClient.setHTMLParserListener(HTMLParserListener.LOG_REPORTER);
            HtmlPage page = webClient.getPage("https://www.empatica.com/connect/download.php?id=463105");
            HtmlForm form = page.getForms().get(0);
            HtmlEmailInput emailUsername = form.getInputByName("username");
            //HtmlTextInput emailUsername = form.getInputByName("username");
            //HtmlInput emailUsername = page.getElementByName("username");
            HtmlPasswordInput password = form.getInputByName("password");
            //HtmlInput password = page.getElementByName("password");
            emailUsername.setValueAttribute("mstent13@student.aau.dk");
            password.setValueAttribute("Martin2981");
            //password.setValueAttribute(".2612801277.");

            //HtmlSubmitInput loginButton = page.getElementByName("login-button");
            //page = loginButton.click();
            System.out.println(page.getPage().toString());
            HtmlButton button = form.getButtonByName("login-button");
            //HtmlSubmitInput button = form.getInputByName("submit");

            HtmlPage dashboardPage = button.click();
            System.out.println(dashboardPage.getPage().toString());
            HtmlPage sessionPage = dashboardPage.getAnchorByHref("sessions.php").click();
            System.out.println(sessionPage.getPage().toString());
            sessionPage.getAnchorByText("All Sessions").click().getWebResponse();
            InputStream is = sessionPage.getAnchorByHref("https://www.empatica.com/connect/download.php?id=468840").click();
            //InputStream is = sessionPage.getAnchorByText('"' + " Download" + '"').click();
            *//*InputStream is = button.click().getWebResponse().getContentAsStream();*//*
            try {
                File f = new File("c:\\In the Moment\\download\\1523455157_A01163.zip");
                OutputStream os = new FileOutputStream(f);
                byte[] bytes = new byte[1024]; // make it bigger if you want. Some recommend 8x, others 100x
                int length = 0;
                while ((length = is.read(bytes))!=-1) {
                    os.write(bytes, 0, length);
                }
                os.close();
                is.close();
            } catch (IOException ex) {
                // Exception handling
            }
        } catch (IOException io){
        }*/

        /*try {
            URL url = new URL("https://www.empatica.com/connect/download.php?id=463105");
            String userPass = "mstent13%40student.aau.dk:.2612801277.";
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userPass.getBytes());
            //or
            //String basicAuth = "Basic " + new String(Base64.encode(userPass.getBytes(), Base64.No_WRAP));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.connect();
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\1523455157_A01163.zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException io){

        }*/

        /*try {
           URLConnection uo= login("https://www.empatica.com/connect/download.php?id=463105", "mstent13@student.aau.dk", ".2612801277.");

            ReadableByteChannel rbc = Channels.newChannel(uo.getInputStream());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\activation_info_students_only_2017-2018.pdf");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException io){

        }*/

        /*MyAuthenticator.setPasswordAuthentication( "mstent13%40student.aau.dk",".2612801277.");
        Authenticator.setDefault(new MyAuthenticator());
        try {
            String urlLink = "https://www.empatica.com/connect/login.php?username=mstent13@student.aau.dk&password=Martin2981";
            URL url = new URL(urlLink);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

*//*            String urlLinkFile = "https://www.empatica.com/connect/download.php?id=463105?username=mstent13@student.aau.dk&password=Martin2981";
            URL urlFile = new URL(urlLinkFile);
            HttpURLConnection httpConnFile = (HttpURLConnection) urlFile.openConnection();
            httpConnFile.setRequestMethod("GET");*//*
            System.out.println("File size: " + httpConn.getContentLengthLong());

        } catch (IOException io){

        }*/


/*        String username = "mstent13%40student.aau.dk";
        String password = ".2612801277.";

        String usepass = username + ":" + password;
        String basicAuth = "Basic "+  javax.xml.bind.DatatypeConverter.printBase64Binary(usepass.getBytes());

        try {
            URL url = new URL("https://www.empatica.com/connect/download.php?id=463105");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setRequestProperty("Authorization", basicAuth);
            System.out.println("File size: " + httpConn.getContentLengthLong());
            ReadableByteChannel rbc = Channels.newChannel(httpConn.getInputStream());
            //String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\1523455157_A01163.zip");

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            fos.close();
            rbc.close();
        } catch (IOException io){
            System.out.println(io);
        }*/

        /*CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("mstent13%40student.aau.dk", ".2612801277."));
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        HttpGet httpget = new HttpGet("http://files.portal.aau.dk/filesharing/download/person/tsuki@its/aau/dk/~/aau-only/Mathworks/activation_info_students_only_2017-2018.pdf");

        try {
            InputStream input = null;
            OutputStream output = null;
            try {

                byte[] buffer = new byte[1024];
                //System.out.println("Executing request " + httpget.getRequestLine());
                CloseableHttpResponse response = httpclient.execute(httpget);
                 input = response.getEntity().getContent();
                output = new FileOutputStream("c:\\In the Moment\\download\\activation_info_students_only_2017-2018.pdf");
                for (int length; (length = input.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }*/
/*                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } finally {
                    response.close();
                }*//*
            } finally {
                httpclient.close();
                if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
                if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
            }
        } catch (IOException io){

        }*/

        /*try {
            //URL website = new URL("https://mstent13@student.aau.dk:Martin2981@www.empatica.com/connect/download.php?id=463105");
            //URL website = new URL("https://www.empatica.com/connect/download.php?id=463105");

            URL website = new URL("http://files.portal.aau.dk/filesharing/download/person/tsuki@its/aau/dk/~/aau-only/Mathworks/activation_info_students_only_2017-2018.pdf");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\activation_info_students_only_2017-2018.pdf");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }catch (IOException ex){
            System.out.println("Download from Emphatica: " + ex);
        }*/

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<ArrayList<String>> originalPhotoDataList = new ArrayList<ArrayList<String>>();
                try {
                    File originalPhotosDirectory = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0");
                    int i = 0;
                    if (originalPhotosDirectory.isDirectory()) {
                        for (final File f : originalPhotosDirectory.listFiles()) {
                            if (f.isDirectory()) {
                                File directoryMonth = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + f.getName());
                                if (directoryMonth.isDirectory()) {
                                    for (final File g : directoryMonth.listFiles()) {
                                        if (g.isDirectory()) {
                                            File directoryDay = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + f.getName() + "\\" + g.getName());
                                            if (directoryDay.isDirectory()) {
                                                for (final File h : directoryDay.listFiles()) {
                                                    originalPhotoDataList.add(new ArrayList<String>());
                                                    originalPhotoDataList.get(i).add(f.getName());
                                                    originalPhotoDataList.get(i).add(g.getName());
                                                    originalPhotoDataList.get(i).add(h.getName());
                                                    i++;
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

                // Check each date to see if there already is processed a top 10 photo set for that date. If a date has no top 10 photo set check if there is a eda.csv file present for the date. If true process a top 10 photo set for the date using data from the csv file

                // Check if there is a top 10 photo set present for each date
                ArrayList<String> hasTopTen = new ArrayList<>();
                try {
                    File file = new File("C:\\In the Moment\\Moments\\topten.txt");
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null) {
                        hasTopTen.add(st);
                    }
                } catch (IOException io) {
                }

                systemOutArrayListLoop("Does have top ten: ", hasTopTen);

                for (int i = 0; i < originalPhotoDataList.size(); i++) {
                    if (hasTopTen.contains(originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2))){
                        originalPhotoDataList.remove(i);
                    }
                }

                for (int i = 0; i < originalPhotoDataList.size(); i++) {
                    System.out.println("Does not have topten list: " + originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2));
                }

                // Check if the csv file is present
                File checkGsrFileDirectory = new File("C:\\In the Moment\\GSR files");
                for (int i = 0; i < originalPhotoDataList.size(); i++) {
                    String date = originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2);
                    for (final File f : checkGsrFileDirectory.listFiles()) {
                        //System.out.println("Equals? : " + date + ".csv == " + f.getName());
                        if ((date + ".csv").equals(f.getName())) {
                            System.out.println("It's true woo hoo: " + date);

                            // Process the top 10

                            // create list for GSR measurement objects
                            DefaultListModel<GsrMeasurement> gsrMeasurementListModel = new DefaultListModel<>();

                            // read csv file with GSR measurements
                            try (BufferedReader br = new BufferedReader(new FileReader("C:\\In the Moment\\GSR files\\" + f.getName()))) {
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
                                    gsrMeasurementListModel.addElement(new GsrMeasurement(Double.parseDouble(line), stringWithTimeStampDateObjectFormat));

                                    // increase timestamp with measurement frequency pr second (represented in milliseconds)
                                    timeStampDateObjectFormat.setTime(timeStampDateObjectFormat.getTime() + 1000 / Math.round(frequency));
                                }
                            } catch (IOException io) {
                            }

                            // Create list with top 10 highest GSR measurements. Create list with timestamps from all photos from that day. Foreach GSR measurement on 10 highest GSR measurement list compare timestamp with timestamps from list of photos to find the photo which was taken closest to the measurement

                            // Compare each measurement on the list of total gsr measurements with its adjacent measurements and create list of objects with all gsr measurement jumps including the size of the jump
                            ArrayList<GsrMeasurement> listWithAllIncreasesInGsrMeasurements = new ArrayList<GsrMeasurement>();
                            GsrMeasurement increasedGsrMeasurements;
                            for (int j = 0; j < gsrMeasurementListModel.size() - 1; j++) {
                                int diff = gsrMeasurementListModel.getElementAt(j + 1).getMeasurement() - gsrMeasurementListModel.getElementAt(j).getMeasurement();

                                if (diff > 0) {
                                    increasedGsrMeasurements = gsrMeasurementListModel.getElementAt(j + 1);
                                    increasedGsrMeasurements.setIncreaseDiff(diff);
                                    listWithAllIncreasesInGsrMeasurements.add(increasedGsrMeasurements);
                                }
                            }

                            // Sort list with gsr measurement jumps to find the 10 highest jumps
                            listWithAllIncreasesInGsrMeasurements.sort(Comparator.comparing(GsrMeasurement::getIncreaseDiff).reversed());

                            // Write list to console to check contents
                            for (int j = 0; j < 10; j++) {
                                System.out.println("Increased GSR measurement: " + listWithAllIncreasesInGsrMeasurements.get(j).getMeasurement() + " " + listWithAllIncreasesInGsrMeasurements.get(j).getIncreaseDiff() + " " + listWithAllIncreasesInGsrMeasurements.get(j).getDateTime() + " " + j);
                            }

                            DefaultListModel<Photo> photosInFolderListModel = new DefaultListModel<>();
                            File dir = new File("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + originalPhotoDataList.get(i).get(0) + "\\" + originalPhotoDataList.get(i).get(1) + "\\" + originalPhotoDataList.get(i).get(2));
                            if (dir.isDirectory()) { // make sure it's a directory
                                for (final File g : dir.listFiles(IMAGE_FILTER)) {

                                    Photo newPhoto = new Photo(g.getName(), g.getName());
                                    photosInFolderListModel.addElement(newPhoto);
                                }
                            }

                            Photo currentClosest = null;
                            DefaultListModel<Photo> finalPhotoList = new DefaultListModel<>();

                            for (int j = 0; j < 10; j++) {
                                long smallestDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.firstElement().getDateTime().getTime());
                                currentClosest = photosInFolderListModel.firstElement();

                                for (int k = 0; k < photosInFolderListModel.getSize() - 1; k++) {
                                    long currentDifference = Math.abs(listWithAllIncreasesInGsrMeasurements.get(j).getDateTime().getTime() - photosInFolderListModel.getElementAt(k + 1).getDateTime().getTime());
                                    //System.out.println("Diff: " + currentDifference);

                                    if (currentDifference < smallestDifference) {
                                        currentClosest = photosInFolderListModel.getElementAt(k + 1);
                                        smallestDifference = currentDifference;
                                        //System.out.println("Smallest difference: " + smallestDifference);
                                        //System.out.println(currentClosest.getID() + " " + currentClosest.getDateTime());
                                    }
                                }
                                finalPhotoList.addElement(currentClosest);
                            }

                            // Create directory for top 10 photos
                            new File("C:\\In the Moment\\Moments\\Images\\" + originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2)).mkdirs();

                            // Copy top 10 photos to new directory
                            for (int j = 0; j < finalPhotoList.getSize(); j++) {
                                Path finalPhoto = Paths.get("E:\\OneDrive\\Martin\\Martin billeder\\Narrative Clip\\martinstentoft@gmail.com\\81103ff0\\" + originalPhotoDataList.get(i).get(0) + "\\" + originalPhotoDataList.get(i).get(1) + "\\" + originalPhotoDataList.get(i).get(2) + "\\" + finalPhotoList.getElementAt(j).getID());
                                Path finalPhotoMove = Paths.get("C:\\In the Moment\\Moments\\Images\\" + originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2) + "\\" + finalPhotoList.getElementAt(j).getID());
                                //System.out.println("Final photo: " + finalPhoto);
                                //System.out.println("Exists? " + Files.exists(finalPhoto));
                                try {
                                    Files.copy(finalPhoto, finalPhotoMove, REPLACE_EXISTING);
                                } catch (IOException io) {
                                }
                            }

                            // Save name of topten list to textfile and update combobox
                            try (FileWriter fw = new FileWriter("C:\\In the Moment\\Moments\\topten.txt", true);
                                 BufferedWriter bw = new BufferedWriter(fw);
                                 PrintWriter line = new PrintWriter(bw)) {
                                line.println(originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2));

                                try {
                                    Date newTopTendate;
                                    newTopTendate = dateTimeFormat.parse(originalPhotoDataList.get(i).get(0) + originalPhotoDataList.get(i).get(1) + originalPhotoDataList.get(i).get(2));
                                    comboDate.addElement(newDateTimeFormat.format(newTopTendate));

                                }catch (ParseException p) {
                                }
                            } catch (IOException io) {
                            }
                        }
                    }
                }
                /*try {
                    File file = new File("C:\\In the Moment\\Moments\\topten.txt");
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    Date date;
                    while ((line = br.readLine()) != null) {
                        try {
                            date = dateTimeFormat.parse(line);

                            if (((DefaultComboBoxModel)comboBoxDate.getModel()).getIndexOf(date) == -1){
                                comboDate.addElement(newDateTimeFormat.format(date));
                            }
                        }catch (ParseException p) {
                        }
                    }
                }catch (IOException io){
                }
                comboBoxDate.setModel(comboDate);*/
            }
        });

        try {
            File file = new File("C:\\In the Moment\\Moments\\topten.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            Date date;
            while ((line = br.readLine()) != null) {
                try {
                    date = dateTimeFormat.parse(line);
                    comboDate.addElement(newDateTimeFormat.format(date));
                }catch (ParseException p) {
                }
            }
        }catch (IOException io){
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

    private void systemOutArrayListLoop(String string, ArrayList arrayList){
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println( string + arrayList.get(i));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().main_window_main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}