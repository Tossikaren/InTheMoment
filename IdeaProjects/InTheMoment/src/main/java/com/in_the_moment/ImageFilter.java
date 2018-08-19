package com.in_the_moment;

import java.io.File;
import java.io.FilenameFilter;

public interface ImageFilter {

    // array of supported extensions (use a List if you prefer)
    String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg" // and other formats you need
    };

    // filter to identify images based on their extensions
    FilenameFilter IMAGE_FILTER = new FilenameFilter() {

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
}
