/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       FolderSelectionData.java
 *  @package	de.hska.iam.presentationapp.queries.selection
 *  @brief	    Holds the selection data for folders.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 29.05.2015 Markus Maier
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

package de.hska.iam.presentationapp.queries.selection;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class FolderSelectionData implements SelectionData {

    private static final String SELECTION = MediaStore.Files.FileColumns.PARENT + " = ?";
    private final String selectionArg;

    public FolderSelectionData(final Context context, final String folder) {
        selectionArg = getFolderId(context, folder);
    }

    private static String getFolderId(final Context context, final String folder) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.PARENT};
        String selection = MediaStore.Files.FileColumns.DATA + " = ?";
        String[] selectionArgs = getFolderIdSelectionArgs(folder);
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        String folderId = "";
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    folderId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.PARENT));
                }
            } finally {
                cursor.close();
            }
        }
        return folderId;
    }

    private static String[] getFolderIdSelectionArgs(final String folder) {
        String selectionArg = "";
        if (folder != null) {
            File file = new File(folder);
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                selectionArg = files[0].getAbsolutePath();
            }
        }
        return new String[]{selectionArg};
    }

    @Override
    public String getSelection() {
        return SELECTION;
    }

    @Override
    public String getSelectionArg() {
        return selectionArg;
    }

}
