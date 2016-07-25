/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       UriUtils.java
 *  @package	de.hska.iam.presentationapp.util
 *  @brief      Utility class for uris.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 27.02.2015 Markus Maier
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

package de.hska.iam.presentationapp.util;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public final class UriUtils {

    private UriUtils() {
    }

    public static boolean isPdf(final Uri uri) {
        String mimeType = getMimeType(uri);
        boolean mimeTypePdf = false;
        if (mimeType != null) {
            mimeTypePdf = mimeType.endsWith("pdf");
        }
        return mimeTypePdf;
    }

    public static boolean isImage(final Uri uri) {
        String mimeType = getMimeType(uri);
        boolean mimeTypeImage = false;
        if (mimeType != null) {
            mimeTypeImage = mimeType.startsWith("image");
        }
        return mimeTypeImage;
    }

    public static boolean isVideo(final Uri uri) {
        String mimeType = getMimeType(uri);
        boolean mimeTypeVideo = false;
        if (mimeType != null) {
            mimeTypeVideo = mimeType.startsWith("video");
        }
        return mimeTypeVideo;
    }

    private static String getMimeType(final Uri uri) {
        String fileExtension = getFileExtension(uri);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
    }

    private static String getFileExtension(final Uri uri) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        return fileExtension.toLowerCase();
    }

    public static Uri fromFilePath(final String absolutePath) {
        File file = new File(absolutePath);
        return Uri.fromFile(file);
    }

}
