package com.in_the_moment.Model;

import com.in_the_moment.PhotoDirectory;
import org.junit.Test;
import static org.junit.Assert.*;

public class PhotoDirectoryTest {

    @Test
    public void photoDirectoryToString() {
        PhotoDirectory photoDirectory = new PhotoDirectory("2018", "07", "22");
        assertEquals("20180722", photoDirectory.photoDirectoryToString());
    }

    @Test
    public void photoDirectoryStringParser() {
        PhotoDirectory photoDirectory = new PhotoDirectory();
        String photoDirectoryString = "20180722";
        assertArrayEquals(new String[]{"2018", "07", "22"}, photoDirectory.photoDirectoryStringParser(photoDirectoryString));
    }
}