/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      BitmapCreator.java
 *  @package   de.hska.iam.presentationapp.cache
 *  @brief     Creates a bitmap from a media file and saves it
 *             to the memory cache. If a view was given also sets
 *             the created bitmap for the view. In addition starts
 *             the BitmapSaver to save the bitmap to the cache
 *             directory on the sd card.
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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.cache.database.CachedImage;
import de.hska.iam.presentationapp.media.Media;

import java.lang.ref.WeakReference;

class BitmapCreator {

    private final Context context;
    private final BitmapMemoryCache memoryCache;

    BitmapCreator(final Context context, final BitmapMemoryCache memoryCache) {
        this.context = context;
        this.memoryCache = memoryCache;
    }

    public void createThumbnail(final CachedImage cachedImage, final Media media, final ImageView imageView) {
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.seek_progress);
        createBitmap(cachedImage, media, imageView, placeHolder);
    }

    public void createFullscreenImage(final CachedImage cachedImage, final Media media, final ImageView imageView) {
        createBitmap(cachedImage, media, imageView, null);
    }

    private void createBitmap(final CachedImage cachedImage, final Media media, final ImageView imageView, final Bitmap placeHolder) {
        if (cancelPotentialWork(cachedImage, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, media);
            if (imageView != null) {
                Resources resources = context.getResources();
                AsyncDrawable asyncDrawable = null;
                try {
                    asyncDrawable = new AsyncDrawable(resources, placeHolder, task);
                } catch (final RuntimeException e) {
                    Log.e(getClass().getName(), e.getMessage());
                }
                imageView.setImageDrawable(asyncDrawable);
            }
            task.execute(cachedImage);
        }
    }

    private static boolean cancelPotentialWork(final CachedImage cachedImage, final ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        boolean cancel = true;

        if (bitmapWorkerTask != null) {
            final CachedImage taskData = bitmapWorkerTask.getImageVo();
            if (taskData == null || taskData != cachedImage) {
                bitmapWorkerTask.cancel(true);
            } else {
                cancel = false;
            }
        }
        return cancel;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(final ImageView imageView) {
        BitmapWorkerTask task = null;
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                task = asyncDrawable.getTask();
            }
        }
        return task;
    }

    private static class AsyncDrawable extends AsyncDrawableTask<BitmapWorkerTask> {
        AsyncDrawable(final Resources res, final Bitmap bitmap, final BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap, bitmapWorkerTask);
        }
    }

    private class BitmapWorkerTask extends AsyncTask<CachedImage, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private final Media media;
        private CachedImage cachedImage;

        BitmapWorkerTask(final ImageView imageView, final Media media) {
            imageViewReference = new WeakReference<>(imageView);
            this.media = media;
        }

        public CachedImage getImageVo() {
            return cachedImage;
        }

        @Override
        protected Bitmap doInBackground(final CachedImage... params) {
            cachedImage = params[0];
            Bitmap bitmap = null;
            CachedImage.ImageType imageType = cachedImage.getImageType();
            if (imageType == CachedImage.ImageType.THUMBNAIL) {
                bitmap = media.createThumbnail();
            } else if (imageType == CachedImage.ImageType.FULLSCREEN_IMAGE) {
                bitmap = media.createFullscreenImage();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled()) {
                result = null;
            }

            if (result != null) {
                putBitmapInMemoryCache(result);
                setImageBitmap(result);
            }
        }

        private void putBitmapInMemoryCache(final Bitmap bitmap) {
            memoryCache.put(cachedImage, bitmap);
        }

        private void setImageBitmap(final Bitmap bitmap) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

    }

}
