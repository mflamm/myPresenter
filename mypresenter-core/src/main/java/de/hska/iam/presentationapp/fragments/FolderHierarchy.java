/**
 * @MyPresenter Presentation-App for Android-Devices
 * @copyright 2014 IMP - Institute of Materials and Processes
 * University of Applied Sciences
 * Karlsruhe
 * @file FolderHierarchy.java
 * @package de.hska.iam.presentationapp.fragments
 * @brief Navigates a folder hierarchy.
 * <p/>
 * <p/>
 * *******************************************************************
 * @lastmodified 19.05.2015 Markus Maier
 * <p/>
 * *******************************************************************
 * <p/>
 * LICENSE:
 * <p/>
 * MyPresenter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file FolderHierarchy.java
 *  @package de.hska.iam.presentationapp.fragments
 *  @brief Navigates a folder hierarchy.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 19.05.2015 Markus Maier
 *
 ********************************************************************
 *
 *	LICENSE:
 *
 *	MyPresenter is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the
 *	License, or (at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 ********************************************************************/

package de.hska.iam.presentationapp.fragments;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderHierarchy {

    private final File root;
    private File current;

    FolderHierarchy(final String path) {
        current = new File(path);
        root = current.getParentFile();
    }

    /**
     * Gets the current absolute path of the folder.
     *
     * @return The current path.
     */
    public String getCurrentPath() {
        return current.getAbsolutePath();
    }

    /**
     * Moves up in the directory hierarchy.
     */
    public void up() {
        current = current.getParentFile();
    }

    /**
     * Changes the directory.
     *
     * @param folderName
     *          The name of the folder.
     */
    public void cd(final String folderName) {
        if (folderName.startsWith("<")) {
            up();
            return;
        }

        List<String> directories = dir();
        if (directories.contains(folderName)) {
            String currentPath = current.getAbsolutePath();
            current = new File(currentPath + File.separator + folderName);
        }
    }

    /**
     * Gets a list with names of directories in the current directory.
     *
     * @return A list with names of directories.
     */
    public List<String> dir() {
        String[] dirs = current.list(new DirectoryFilter());
        List<String> directories = new ArrayList<>();

        if (!current.equals(root)) {
            String absolutPath = current.getAbsolutePath();
            String currentDirectory = absolutPath.replace(root.toString(),"");
            directories.add("< " + currentDirectory);
        }

        if(dirs.length > 0){
            directories.addAll(Arrays.asList(dirs));
        }

    return directories;
}

    /**
     * Checks if the current directory is the root directory.
     *
     * @return True if the current directory is the root directory else false.
     */
    public boolean isRootDirectory() {
        return root.equals(current);
    }

private static class DirectoryFilter implements FilenameFilter {
    @Override
    public boolean accept(final File dir, final String filename) {
        File file = new File(dir, filename);
        return file.isDirectory();
    }
}

}
