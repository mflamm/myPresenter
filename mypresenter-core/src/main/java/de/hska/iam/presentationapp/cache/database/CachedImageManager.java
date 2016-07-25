/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      CachedImageManager.java
 *  @package   de.hska.iam.presentationapp.cache.database
 *  @brief     Manager for cached images.
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

package de.hska.iam.presentationapp.cache.database;

import android.content.Context;
import android.os.Handler;

import java.io.File;

public class CachedImageManager {

    private final CachedImageDao dao;
    private final CachedImages cachedImages;
    private final Handler workerHandler;

    public CachedImageManager(final Context context) {
        dao = new CachedImageDao(context);
        cachedImages = new CachedImages();
        workerHandler = new Handler();
        populate();
    }

    public CachedImage getThumbnail(final String mediaFilePath) {
        CachedImage cachedImageRet = null;
        CachedImage.ImageType imageType = CachedImage.ImageType.THUMBNAIL;
        for (final CachedImage cachedImage : cachedImages.values()) {
            if (imageType == cachedImage.getImageType() &&
                mediaFilePath.equals(cachedImage.getMediaFilePath())) {
                cachedImageRet = cachedImage;
                break;
            }
        }
        if (cachedImageRet == null) {
            int pdfPageNumber = -1;
            cachedImageRet = dao.create(imageType, mediaFilePath, pdfPageNumber);
            cachedImages.put(cachedImageRet);
        }
        return cachedImageRet;
    }

    public CachedImage getFullscreenImage(final String mediaFilePath, final int pdfPageNumber) {
        CachedImage cachedImageRet = null;
        CachedImage.ImageType imageType = CachedImage.ImageType.FULLSCREEN_IMAGE;
        for (final CachedImage cachedImage : cachedImages.values()) {
            if (imageType == cachedImage.getImageType() &&
                pdfPageNumber == cachedImage.getPdfPageNumber() &&
                mediaFilePath.equals(cachedImage.getMediaFilePath())) {
                cachedImageRet = cachedImage;
                break;
            }
        }
        if (cachedImageRet == null) {
            cachedImageRet = dao.create(imageType, mediaFilePath, pdfPageNumber);
            cachedImages.put(cachedImageRet);
        }
        return cachedImageRet;
    }

    public CachedImage get(final long id) {
        return cachedImages.get(id);
    }

    public void update(final CachedImage cachedImage) {
        dao.update(cachedImage);
        cachedImages.put(cachedImage);
    }

    public void remove(final CachedImage cachedImage) {
        dao.delete(cachedImage);
        cachedImages.remove(cachedImage);
        deleteFile(cachedImage.getImageFilePath());
    }

    private void deleteFile(final String filePath) {
        workerHandler.post(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                file.delete();
            }
        });
    }

    private void populate() {
        for (final CachedImage cachedImage : dao.getAll()) {
            if (isValid(cachedImage)) {
                cachedImages.put(cachedImage);
            }
        }
    }

    private boolean isValid(final CachedImage cachedImage) {
       boolean valid = true;
        if (!isMediaFileValid(cachedImage)) {
            remove(cachedImage);
            valid = false;
        } else if (!isImageFileValid(cachedImage)) {
            cachedImage.setImageFilePath("");
        }
        return valid;
    }

    private static boolean isMediaFileValid(final CachedImage cachedImage) {
        File mediaFile = new File(cachedImage.getMediaFilePath());
        return mediaFile.exists();
    }

    private static boolean isImageFileValid(final CachedImage cachedImage) {
        File imageFile = new File(cachedImage.getImageFilePath());
        return imageFile.exists();
    }

}
