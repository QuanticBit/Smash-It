package org.smash.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.xor.utils.FileTools;

public class SkinFilter extends FileFilter {

	 //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = FileTools.getExtension(f);
        if (extension != null) {
            if (extension.equals("zip") ||
                extension.equals("tar") ||
                extension.equals("rar") ||
                extension.equals("sskin") ||
                extension.equals("jar")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Smash It Skin ( sskin, zip, tar, jar, rar)";
    }
    
    public static boolean isValid(File f) {
        if (f.isDirectory()) {
            return false;
        }

        String extension = FileTools.getExtension(f);
        if (extension != null) {
            if (extension.equals(".zip") ||
                extension.equals(".tar") ||
                extension.equals(".rar") ||
                extension.equals(".sskin") ||
                extension.equals(".jar")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }
}