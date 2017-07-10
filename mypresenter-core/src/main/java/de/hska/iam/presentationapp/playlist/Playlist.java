/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       Playlist.java
 *  @package	de.hska.iam.presentationapp.playlist
 *  @brief      A playlist of media files.
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

package de.hska.iam.presentationapp.playlist;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.media.MediaFactory;
import de.hska.iam.presentationapp.media.Pdf;
import de.hska.iam.presentationapp.util.UriUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Playlist implements Iterable<Media> {

    private final Context context;
    private final String name;
    private int startPosition;
    private final List<Media> playlist;

    /**
     * Constructor.
     *
     * @param name
     *         The name of the playlist.
     */
    public Playlist(final Context context, final String name) {
        this.context = context;
        this.name = name;
        playlist = new ArrayList<>();
    }

    /**
     * Constructor.
     *
     * @param name
     *         The name of the playlist.
     *
     * @param startPosition
     *         The position of the media file
     *         which shall be shown first.
     */
    public Playlist(final Context context, final String name, final int startPosition) {
        this(context, name);
        this.startPosition = startPosition;
    }

    /**
     * Gets the name of the playlist.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the start position of the playlist.
     *
     * @return The start position.
     */
    public int getStartPosition() {
        return startPosition;
    }

    /*
     * (non-Javadoc)
     * @see java.util.List#get(int)
     */
    public Media get(final int location) {

            return playlist.get(location);
    }

    /*
     * (non-Javadoc)
     * @see java.util.List#getNumberOfItems()
     */
    public int getNumberOfItems() {
        return playlist.size();
    }

    /**
     * Adds a collection of media files.
     *
     * @param collection
     *          The collection of media files.
     */
    public void addAll(final Iterable<? extends Media> collection) {
        for (final Media media : collection) {
            add(media);
        }
    }

    /**
     * Adds a media file.
     *
     * @param media
     *          The media file.
     */
    public void add(final Media media) {
        if (isPdf(media)) {
            Pdf pdf = (Pdf) media;
            adjustStartPosition(pdf);
            addPdfPages(pdf);
        } else {
            playlist.add(media);
        }
    }

    private static boolean isPdf(final Media media) {
        String absolutePath = media.getAbsolutePath();
        Uri uri = UriUtils.fromFilePath(absolutePath);
        return UriUtils.isPdf(uri);
    }

    private void addPdfPages(final Pdf pdf) {
        int pageCount = pdf.getPageCount();
        for (int pageNumber = 0; pageNumber < pageCount; pageNumber++) {
            String mediaFilePath = pdf.getAbsolutePath();
            Media pdfPage = MediaFactory.INSTANCE.getMedia(context, mediaFilePath, pageNumber);
            playlist.add(pdfPage);
        }
    }

    private void adjustStartPosition(final Pdf pdf) {
        int pdfInsertionPosition = playlist.size();
        if (pdfInsertionPosition < startPosition) {
            startPosition += pdf.getPageCount() - 1;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.util.List#remove()
     */
    public void remove(final Media media) {
        playlist.remove(media);
    }

    /*
     * (non-Javadoc)
     * @see java.util.List#clear()
     */
    public void clear() {
        playlist.clear();
    }

    @Override
    public Iterator<Media> iterator() {
        return playlist.iterator();
    }

}
