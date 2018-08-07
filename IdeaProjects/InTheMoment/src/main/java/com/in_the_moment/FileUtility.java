package com.in_the_moment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public interface FileUtility{

    default ArrayList<String> textFileReader(String directoryPath) {
        ArrayList<String> arrayList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(directoryPath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
            }
        } catch (IOException io) {
        }
        return arrayList;
    }

    default ArrayList<String> directoryFileReader(File directoryPath) {
        ArrayList<String> arrayList = new ArrayList<>();
        if(directoryPath.isDirectory()) {
            File[] filesInDirectory = directoryPath.listFiles();
            if (filesInDirectory != null) {
                for (final File fileInDirectory : filesInDirectory) {
                    arrayList.add(fileInDirectory.getName());
                }
            }
        }
        return arrayList;
    }

    default void copyFileToDirectory(Path copyFromDirectoryPath, Path copyToDirectoryPath) {
        try {
            Files.copy(copyFromDirectoryPath, copyToDirectoryPath, REPLACE_EXISTING);
        } catch (IOException io) {
        }
    }
}