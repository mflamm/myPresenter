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
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import de.hska.iam.presentationapp.cache.database.CachedImage.ImageType;

public class CachedImageManager {

    private final CachedImageDao dao;
    private final ImageCache imageCache;
    private final Handler workerHandler;

    public CachedImageManager(final Context context) {
        dao = new CachedImageDao(context);
        imageCache = new ImageCache();
        workerHandler = new Handler();
        populate();
    }

    /**
     *
     * @param mediaFilePath
     * @return Thumbnailbild
     */
    public CachedImage getThumbnail(final String mediaFilePath) {
        ImageType imageType = ImageType.THUMBNAIL;
        return getCachedImage(mediaFilePath, imageType);
    }

    /**
     *
     * @param mediaFilePath
     * @param imageType
     * @return das Bild aus dem Cache. Falls Bild nicht im Cache vorhanden ist, wird es hinzugefügt und zurück gegeben.
     */
    private CachedImage getCachedImage(String mediaFilePath, ImageType imageType) {
        ArrayList<Long> possibleIds = imageCache.getIdsByMediaFilePath(mediaFilePath);
        for (Long id : possibleIds) {
            CachedImage currentImage = imageCache.get(id);
            if (currentImage.getImageType() == imageType) {
                Log.i("CachedImageManager", "possibleIds HIT ID: " + id);
                return currentImage;
            }
        }
        int pdfPageNumber = -1;
        CachedImage newlyCachedImage = addImageToCache(mediaFilePath, imageType, pdfPageNumber);
        imageCache.put(newlyCachedImage);
        return newlyCachedImage;
    }

    private CachedImage addImageToCache(String mediaFilePath, ImageType imageType, int pdfPageNumber) {
        return dao.create(imageType, mediaFilePath, pdfPageNumber);
    }

    public CachedImage getFullscreenImage(final String mediaFilePath, final int pdfPageNumber) {
        ImageType imageType = ImageType.FULLSCREEN_IMAGE;
        for (final CachedImage cachedImage : imageCache.getAll()) {
            if (imageType == cachedImage.getImageType() &&
                    pdfPageNumber == cachedImage.getPdfPageNumber() &&
                    mediaFilePath.equals(cachedImage.getMediaFilePath())) {
                return cachedImage;
            }
        }
        CachedImage newlyCachedImage = addImageToCache(mediaFilePath, imageType, pdfPageNumber);
        imageCache.put(newlyCachedImage);
        return newlyCachedImage;
    }

    public CachedImage get(final long id) {
        return imageCache.get(id);
    }

    public void update(final CachedImage cachedImage) {
        dao.update(cachedImage);
        imageCache.put(cachedImage);
    }

    public void remove(final CachedImage cachedImage) {
        dao.delete(cachedImage);
        imageCache.remove(cachedImage);
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
                imageCache.put(cachedImage);
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
