package com.in_the_moment;

public class PhotoDirectory {
    private String year;
    private String month;
    private String day;

    public PhotoDirectory() {
    }

    public PhotoDirectory(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String photoDirectoryToString(){
        return year + month + day;
    }

    public String[] photoDirectoryStringParser(String directoryString){
        return new String[] {
                directoryString.substring(0, 4),
                directoryString.substring(4, 6),
                directoryString.substring(6, 8)
        };
    }
}
