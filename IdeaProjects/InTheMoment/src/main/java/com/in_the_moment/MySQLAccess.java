package com.in_the_moment;

import com.in_the_moment.Model.GsrMeasurement;
import com.in_the_moment.Model.Moment;
import com.in_the_moment.Model.Photo;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class MySQLAccess {

    private String inTheMomentDatabase = "in_the_moment";

    private Connection mysqlDatabaseConnection(String databaseName) {
        Connection connection = null;
        try {
            MysqlDataSource mysqlDataSource = new MysqlDataSource();
            mysqlDataSource.setUser("root");
            mysqlDataSource.setPassword("root");
            mysqlDataSource.setDatabaseName(databaseName);
            connection = (Connection) mysqlDataSource.getConnection();
        } catch (Exception e) {
            System.out.println("Exception in method mysqlDatabaseConnection: " + e);
        }
        return connection;
    }

    public void saveGsrMeasurementsToDatabase(ArrayList<GsrMeasurement> arrayList) {
        if (arrayList != null) {
            try (Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)) {
                if (connection != null) {
                    connection.setAutoCommit(false);
                    try (PreparedStatement preparedStatement = connection.prepareStatement("insert into  in_the_moment.gsr_measurements (gsr_measurement_id, measurement, gsr_measurement_datetime, increase_diff) values (?, ?, ?, ?)")) {
                        for (GsrMeasurement gsrMeasurement : arrayList) {
                            preparedStatement.setString(1, gsrMeasurement.getUUID().toString());
                            preparedStatement.setInt(2, gsrMeasurement.getMeasurement());
                            preparedStatement.setTimestamp(3, new Timestamp(gsrMeasurement.getDateTime().getTime()));
                            preparedStatement.setInt(4, gsrMeasurement.getIncreaseDiff());
                            preparedStatement.addBatch();
                            System.out.println("Saving: " + gsrMeasurement.getMeasurement() + " " + gsrMeasurement.getDateTime() + " " + gsrMeasurement.getIncreaseDiff());
                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                } else {
                    System.out.println("Can not run saveGsrMeasurementsToDatabase method because connection is null");
                }
            } catch (SQLException e) {
                System.out.println("Exception in method saveGsrMeasurementsToDatabase: " + e);
            }
        } else {
            System.out.println("Can not run saveGsrMeasurementsToDatabase method because arrayList is null");
        }
    }

    public void savePhotosToDatabase(ArrayList<Photo> arrayList) {
        if (arrayList != null){
            try(Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)) {
                if(connection != null){
                    connection.setAutoCommit(false);
                    try(PreparedStatement preparedStatement = connection.prepareStatement("insert into in_the_moment.photos (photo_id, photo_filename, photo_datetime) values (?, ?, ?)")){
                        for (Photo photo : arrayList) {
                            preparedStatement.setString(1, photo.getUUID().toString());
                            preparedStatement.setString(2, photo.getFileName());
                            preparedStatement.setTimestamp(3, new Timestamp(photo.getDateTime().getTime()));
                            preparedStatement.addBatch();
                            System.out.println("Saving photo: " + photo.getUUID() + " " + photo.getDateTime());
                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (SQLException e){
                System.out.println("Exception in method savePhotosToDatabase: " + e);
            }
        }
    }

    public void saveMomentToDatabase(ArrayList<Moment> arrayList) {
        if (arrayList != null){
            try (Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)){
                if (connection != null){
                    connection.setAutoCommit(false);
                    try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO in_the_moment.moments (moment_id, photo_id, gsr_measurement_id, moment_date) VALUES (?, ?, ?, ?)")){
                        for (Moment moment: arrayList) {
                            preparedStatement.setString(1, moment.getMomentUUID().toString());
                            preparedStatement.setString(2, moment.getPhoto().getUUID().toString());preparedStatement.setString(3, moment.getGsrMeasurement().getUUID().toString());
                            preparedStatement.setDate(4, new java.sql.Date(moment.getMomentDate().getTime()));
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                        connection.commit();
                    }
                }
            } catch (SQLException e){
                System.out.println("Exception in method saveMomentToDatabase: " + e);
            }
        }
    }

    public void saveMomentDescriptionToDatabase(String description, UUID uuid) {
        if (description != null || uuid !=null){
            try(Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)){
                if(connection != null){
                    try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE in_the_moment.moments SET moment_description = ? WHERE moment_id = ?")){
                        preparedStatement.setString(1, description);
                        preparedStatement.setString(2, uuid.toString());
                        preparedStatement.executeUpdate();
                        System.out.println("Save description to mysql database: " + description + " to moment with id: " + uuid.toString());
                    }
                }
            } catch (SQLException e){
                System.out.println("Exception in method saveMomentDescriptionToDatabase: " + e);
            }
        }
    }

    public ArrayList<Date> queryMomentDatesFromDatabase(){
        ArrayList<Date> arrayList = new ArrayList<>();
        try(Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)){
            try(Statement statement = connection.createStatement()){
                ResultSet resultSet = statement.executeQuery("select distinct moment_date from moments");
                while (resultSet.next()){
                    Date date = resultSet.getDate("moment_date");
                    System.out.println("Unique date: " + date);
                    arrayList.add(date);
                }
            }
        } catch (SQLException e){
            System.out.println("Exception in method queryMomentDatesFromDatabase: " + e);
        }
        return arrayList;
    }

    public DefaultListModel<Moment> queryMomentsFromDatabase(Date selectedDate) {
        DefaultListModel<Moment> defaultListModel = new DefaultListModel<>();
        try (Connection connection = mysqlDatabaseConnection(inTheMomentDatabase)){
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM moments right JOIN photos ON moments.photo_id = photos.photo_id right JOIN gsr_measurements ON moments.gsr_measurement_id = gsr_measurements.gsr_measurement_id WHERE moments.moment_date = (?) ORDER BY gsr_measurements.measurement DESC")){
                preparedStatement.setDate(1, new java.sql.Date(selectedDate.getTime()));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    UUID photoUUID = UUID.nameUUIDFromBytes(resultSet.getBytes("photo_id"));
                    String photoFilename = resultSet.getString("photo_filename");
                    Date date = new Date(resultSet.getTimestamp("photo_datetime").getTime());
                    Photo photo = new Photo(photoUUID, photoFilename, date);
                    UUID gsrMeasurementUUID = UUID.fromString(resultSet.getString("gsr_measurement_id"));
                    int measurement = resultSet.getInt("measurement");
                    Date gsrMeasurementDate = new Date(resultSet.getTimestamp("gsr_measurement_datetime").getTime());
                    int increaseDiff = resultSet.getInt("increase_diff");
                    GsrMeasurement gsrMeasurement = new GsrMeasurement(gsrMeasurementUUID, measurement, gsrMeasurementDate, increaseDiff);
                    UUID momentUUID = UUID.fromString(resultSet.getString("moment_id"));
                    String momentDescription = resultSet.getString("moment_description");
                    Moment moment = new Moment(momentUUID, gsrMeasurement, photo, momentDescription, selectedDate );
                    System.out.println(moment.toString());
                    defaultListModel.addElement(moment);
                }
            }
        }catch (SQLException e){
            System.out.println("Exception in method queryMomentsFromDatabase: " + e);
        }
        return defaultListModel;
    }
}