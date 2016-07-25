/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      BitmapDiskCache.java
 *  @package   de.hska.iam.presentationapp.cache
 *  @brief     Manages the cache. Maps the uri of a media file to
 *             its corresponding BitmapUri which contains the uris
 *             of the bitmaps.
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

package de.hska.iam.presentationapp.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import de.hska.iam.presentationapp.cache.database.CachedImage;
import de.hska.iam.presentationapp.cache.database.CachedImageManager;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.media.PdfPage;
import de.hska.iam.presentationapp.util.RandomStringGenerator;
import de.hska.iam.presentationapp.util.UriUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapDiskCache {

    private final Context context;
    private final CachedImageManager cachedImageManager;
    private final FileLruCache fileCache;

    BitmapDiskCache(final Context context) {
        this.context = context;
        cachedImageManager = new CachedImageManager(context);
        long usableExternalSpace = Environment.getExternalStorageDirectory().getUsableSpace();
        int maxsize = (int) (usableExternalSpace / 8);
        fileCache = new FileLruCache(maxsize);
    }

    public CachedImage getThumbnail(final Media media) {
        String mediaFilePath = media.getAbsolutePath();
        return cachedImageManager.getThumbnail(mediaFilePath);
    }

    public CachedImage getFullscreenImage(final Media media) {
        String mediaFilePath = media.getAbsolutePath();
        int pdfPageNumber = getPdfPageNumber(media);
        return cachedImageManager.getFullscreenImage(mediaFilePath, pdfPageNumber);
    }

    private static int getPdfPageNumber(final Media media) {
        int pageNumber = -1;
        String absolutePath = media.getAbsolutePath();
        Uri uri = UriUtils.fromFilePath(absolutePath);
        if (UriUtils.isPdf(uri)) {
            PdfPage pdfPage = (PdfPage) media;
            pageNumber = pdfPage.getPageNumber();
        }
        return pageNumber;
    }

    public static boolean contains(final CachedImage cachedImage) {
        // If an image file path is set then the
        // bitmap file was written to disk
        return !cachedImage.getImageFilePath().isEmpty();
    }

    public void put(final Long id, final Bitmap bitmap) {
        CachedImage cachedImage = cachedImageManager.get(id);
        if (contains(cachedImage)) {
            File imageFile = new File(cachedImage.getImageFilePath());
            fileCache.put(id, imageFile);
        } else {
            FileCreatorTask fileCreatorTask = new FileCreatorTask(cachedImage, bitmap);
            fileCreatorTask.execute();
        }
    }

    private class FileCreatorTask extends AsyncTask<Void, Void, File> {

        private final CachedImage cachedImage
            ;
        private final Bitmap bitmap;

        FileCreatorTask(final CachedImage cachedImage, final Bitmap bitmap) {
            this.cachedImage = cachedImage;
            this.bitmap = bitmap;
        }

        @Override
        protected File doInBackground(final Void... params) {
            File file = createImageFile();
            writeBitmapToDisk(file, bitmap);
            return file;
        }

        @Override
        protected void onPostExecute(final File result) {
            if (result != null) {
                cachedImage.setImageFilePath(result.getAbsolutePath());
                cachedImageManager.update(cachedImage);
                fileCache.put(cachedImage.getId(), result);
            }
        }

        private File createImageFile() {
            String bitmapFilename = createImageFilename();
            String cacheDir = context.getExternalCacheDir().getPath();
            return new File(cacheDir, bitmapFilename);
        }

        private String createImageFilename() {
            String bitmapFileName = RandomStringGenerator.generateString();
            bitmapFileName += ".png";
            return bitmapFileName;
        }

        private void writeBitmapToDisk(final File file, final Bitmap bitmap) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (final RuntimeException | IOException e) {
                Log.e(getClass().getName(), e.getMessage());
            }
        }

    }

    private class FileLruCache extends LruCache<Long, File> {

        FileLruCache(final int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(final Long key, final File value) {
            return (int) value.length();
        }

        @Override
        protected void entryRemoved(final boolean evicted, final Long key, final File oldValue, final File newValue) {
            CachedImage cachedImage = cachedImageManager.get(key);
            cachedImageManager.remove(cachedImage);
        }

    }

}
