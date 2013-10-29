package org.smash.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.xor.utils.FileTools;

public class ImageFilter extends FileFilter {

    //Accept all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = FileTools.getExtension(f);
        if (extension != null) {
            if (extension.equals("tiff") ||
                extension.equals("tif") ||
                extension.equals("gif") ||
                extension.equals("jpeg") ||
                extension.equals("jpg") ||
                extension.equals("png")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Images ( tiff, tif, gif, jpeg, jpg, png)";
    }
}
