/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      BitmapLoader.java
 *  @package   de.hska.iam.presentationapp.activities
 *  @brief     Loads a bitmap from the cache directory into a view.
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
import java.lang.ref.WeakReference;

class BitmapLoader {

    private final Context context;
    private final BitmapMemoryCache memoryCache;

    BitmapLoader(final Context context, final BitmapMemoryCache memoryCache) {
        this.context = context;
        this.memoryCache = memoryCache;
    }

    public void loadThumbnail(final CachedImage cachedImage, final ImageView imageView) {
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        loadBitmap(cachedImage, imageView, placeHolder);
    }

    public void loadFullscreenImage(final CachedImage cachedImage, final ImageView imageView) {
        loadBitmap(cachedImage, imageView, null);
    }

    private void loadBitmap(final CachedImage cachedImage, final ImageView imageView, final Bitmap placeHolder) {
        if (cancelPotentialWork(cachedImage, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            Resources resources = context.getResources();
            AsyncDrawable asyncDrawable = null;
            try {
                asyncDrawable = new AsyncDrawable(resources, placeHolder, task);
            } catch (final RuntimeException e) {
                Log.e(getClass().getName(), e.getMessage());
            }
            imageView.setImageDrawable(asyncDrawable);
            task.execute(cachedImage);
        }
    }

    private static boolean cancelPotentialWork(final CachedImage cachedImage, final ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        boolean cancel = true;

        if (bitmapWorkerTask != null) {
            final CachedImage bitmapData = bitmapWorkerTask.getImageVo();
            if (bitmapData == null || bitmapData != cachedImage) {
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
        private CachedImage cachedImage;

        BitmapWorkerTask(final ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        public CachedImage getImageVo() {
            return cachedImage;
        }

        @Override
        protected Bitmap doInBackground(final CachedImage... params) {
            cachedImage = params[0];
            String imageFilePath = cachedImage.getImageFilePath();
            return BitmapFactory.decodeFile(imageFilePath);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (isCancelled()) {
                result = null;
            }

            if (result != null) {
                memoryCache.put(cachedImage, result);
                setImageBitmap(result);
            }
        }

        private void setImageBitmap(final Bitmap result) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(result);
            }
        }
    }

}
