/*
package com.in_the_moment;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HtmlLogin {
*/
/*    public static URLConnection login(String _url, String _username, String _password) throws IOException, MalformedURLException {

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
    }*//*


    public void login() {
        try {
     */
/* turn off annoying htmlunit warnings *//*

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
            password.setValueAttribute("Apunasa11");
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
            */
/*InputStream is = button.click().getWebResponse().getContentAsStream();*//*

            try {
                File f = new File("c:\\In the Moment\\download\\1523455157_A01163.zip");
                OutputStream os = new FileOutputStream(f);
                byte[] bytes = new byte[1024]; // make it bigger if you want. Some recommend 8x, others 100x
                int length = 0;
                while ((length = is.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                }
                os.close();
                is.close();
            } catch (IOException ex) {
                // Exception handling
            }
        } catch (IOException io) {
        }
    }

        */
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

        }*//*


        */
/*try {
           URLConnection uo= login("https://www.empatica.com/connect/download.php?id=463105", "mstent13@student.aau.dk", ".2612801277.");

            ReadableByteChannel rbc = Channels.newChannel(uo.getInputStream());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\activation_info_students_only_2017-2018.pdf");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException io){

        }*//*


        */
/*MyAuthenticator.setPasswordAuthentication( "mstent13%40student.aau.dk",".2612801277.");
        Authenticator.setDefault(new MyAuthenticator());
        try {
            String urlLink = "https://www.empatica.com/connect/login.php?username=mstent13@student.aau.dk&password=Martin2981";
            URL url = new URL(urlLink);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");

*//*
*/
/*            String urlLinkFile = "https://www.empatica.com/connect/download.php?id=463105?username=mstent13@student.aau.dk&password=Martin2981";
            URL urlFile = new URL(urlLinkFile);
            HttpURLConnection httpConnFile = (HttpURLConnection) urlFile.openConnection();
            httpConnFile.setRequestMethod("GET");*//*
*/
/*
            System.out.println("File size: " + httpConn.getContentLengthLong());

        } catch (IOException io){

        }*//*



*/
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
        }*//*


        */
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
                }*//*

*/
/*                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } finally {
                    response.close();
                }*//*
*/
/*
            } finally {
                httpclient.close();
                if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
                if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
            }
        } catch (IOException io){

        }*//*


        */
/*try {
            //URL website = new URL("https://mstent13@student.aau.dk:Martin2981@www.empatica.com/connect/download.php?id=463105");
            //URL website = new URL("https://www.empatica.com/connect/download.php?id=463105");

            URL website = new URL("http://files.portal.aau.dk/filesharing/download/person/tsuki@its/aau/dk/~/aau-only/Mathworks/activation_info_students_only_2017-2018.pdf");
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("c:\\In the Moment\\download\\activation_info_students_only_2017-2018.pdf");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }catch (IOException ex){
            System.out.println("Download from Emphatica: " + ex);
        }*//*

}
*/
