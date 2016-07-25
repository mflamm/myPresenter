/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ConfigurableFileQuery.java
 *  @package	de.hska.iam.presentationapp.queries
 *  @brief	    A configurable query for files. Gets all
 *              files from the external storage if the query
 *              isn't configured.
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

package de.hska.iam.presentationapp.queries;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import de.hska.iam.presentationapp.queries.selection.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurableFileQuery extends Query {

    private final Context context;
    private final List<SelectionData> selections;
    private int lastFolderDataIndex = - 1;

    public ConfigurableFileQuery(final Context context) {
        super(context);
        this.context = context;
        selections = new ArrayList<>();
    }

    @Override
    public Uri getUri() {
        return MediaStore.Files.getContentUri("external");
    }

    @Override
    protected List<String> getProjection() {
        return new ArrayList<>(Arrays.asList(
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        ));
    }

    @Override
    protected String getSelection() {
        return createSelection();
    }

    private String createSelection() {
        StringBuilder selectionBuilder = new StringBuilder(0);
        if (!selections.isEmpty()) {
            selectionBuilder.append('(');
            int size = selections.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    if (i == lastFolderDataIndex + 1) {
                        selectionBuilder.append(") AND (");
                    } else {
                        selectionBuilder.append(" OR ");
                    }
                }
                SelectionData selectionData = selections.get(i);
                String selection = selectionData.getSelection();
                selectionBuilder.append(selection);
            }
            selectionBuilder.append(')');
        }
        return selectionBuilder.toString();
    }

    @Override
    protected String[] getSelectionArgs() {
        String[] selectionArgs = null;
        if (!selections.isEmpty()) {
            int size = selections.size();
            selectionArgs = new String[size];
            for (int i = 0; i < size; i++) {
                SelectionData selectionData = selections.get(i);
                selectionArgs[i] = selectionData.getSelectionArg();
            }
        }
        return selectionArgs;
    }

    /**
     * Filters the query for images.
     *
     * @param search True to filter the query for images else false.
     */
    public void filterImages(final boolean search) {
        SelectionData selectionData = new ImageSelectionData();
        filterMedia(search, selectionData);
    }

    /**
     * Filters the query for videos.
     *
     * @param search True to filter the query for videos else false.
     */
    public void filterVideos(final boolean search) {
        SelectionData selectionData = new VideoSelectionData();
        filterMedia(search, selectionData);
    }

    /**
     * Filters the query for pdfs.
     *
     * @param search True to filter the query for pdfs else false.
     */
    public void filterPdfs(final boolean search) {
        SelectionData selectionData = new PdfSelectionData();
        filterMedia(search, selectionData);
    }

    /**
     * Filters the query for docx.
     *
     * @param search True to filter the query for docx else false.
     */

    private void filterMedia(final boolean search, final SelectionData selectionData) {
        if (search && !selections.contains(selectionData)) {
            selections.add(selectionData);
        } else {
            selections.remove(selectionData);
        }
    }

    /**
     * Limits the query to one or several folders.
     *
     * If no folders were set the external sdcard
     * is searched for files.
     *
     * @param folders The folders to limit the query to.
     */
    public void limitSearchToFolders(final String[] folders) {
        clearFolders();
        addFolders(folders);
        lastFolderDataIndex = folders.length - 1;
    }

    private void clearFolders() {
        int index = 0;
        while (index <= lastFolderDataIndex) {
            selections.remove(index);
            index++;
        }
    }

    private void addFolders(final String[] folders) {
        int size = folders.length;
        for (int i = 0; i < size; i++) {
            String folder = folders[i];
            SelectionData folderSelection = new FolderSelectionData(context, folder);
            selections.add(0, folderSelection);
        }
    }

}
