/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       MediaFactory.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      Factory for media files.
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

package de.hska.iam.presentationapp.media;

import android.content.Context;
import android.net.Uri;
import de.hska.iam.presentationapp.util.UriUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum MediaFactory {
    INSTANCE;

    private final Map<String, Media> pool = new ConcurrentHashMap<>();

    public Media getMedia(final Context context, final String mediaFilePath) {
        Media media = pool.get(mediaFilePath);
        if (media == null) {
            media = createMedia(context, mediaFilePath);
            if (media != null) {
              pool.put(mediaFilePath, media);
            }
        }
        return media;
    }

    public Media getMedia(final Context context, final String mediaFilePath, final int pdfPageNumber) {
        Media pdfPage = null;
        Media media = getMedia(context, mediaFilePath);
        if (media != null) {
            Uri uri = UriUtils.fromFilePath(mediaFilePath);
            if (UriUtils.isPdf(uri)) {
                Pdf pdf = (Pdf) media;
                pdfPage = pdf.getPage(pdfPageNumber);
            }
        }
        return pdfPage;
    }

    private static Media createMedia(final Context context, final String mediaFilePath) {
        Media media = null;
        Uri uri = UriUtils.fromFilePath(mediaFilePath);
        if (UriUtils.isImage(uri)) {
            media = new Image(context, mediaFilePath);
        } else if (UriUtils.isVideo(uri)) {
            media = new Video(context, mediaFilePath);
        } else if (UriUtils.isPdf(uri)) {
            media = new Pdf(context, mediaFilePath);
        }

        return media;
    }

}
