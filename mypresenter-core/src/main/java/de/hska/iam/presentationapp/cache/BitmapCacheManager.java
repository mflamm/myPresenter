/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      BitmapCacheManager.java
 *  @package   de.hska.iam.presentationapp.cache
 *  @brief     Manages the access to the bitmap cache. Saves bitmaps
 *             to or loads them from the cache directory.
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
import android.widget.ImageView;
import de.hska.iam.presentationapp.cache.database.CachedImage;
import de.hska.iam.presentationapp.media.Media;

public class BitmapCacheManager {

    private final BitmapDiskCache diskCache;
    private final BitmapMemoryCache memoryCache;
    private final BitmapCreator bitmapCreator;
    private final BitmapLoader bitmapLoader;

    public BitmapCacheManager(final Context context) {
        diskCache = new BitmapDiskCache(context);
        memoryCache = new BitmapMemoryCache(diskCache);
        bitmapCreator = new BitmapCreator(context, memoryCache);
        bitmapLoader = new BitmapLoader(context, memoryCache);
    }

    public void clearMemoryCache() {
        memoryCache.clear();
    }

    public boolean containsThumbnail(final Media media) {
        CachedImage cachedImage = diskCache.getThumbnail(media);
        return memoryCache.contains(cachedImage) || BitmapDiskCache.contains(cachedImage);
    }

    public boolean containsFullscreenImage(final Media media) {
        CachedImage cachedImage = diskCache.getFullscreenImage(media);
        return memoryCache.contains(cachedImage) || BitmapDiskCache.contains(cachedImage);
    }

    public void loadThumbnail(final Media media, final ImageView imageView) {
        CachedImage cachedImage = diskCache.getThumbnail(media);
        if (memoryCache.contains(cachedImage)) {
            Bitmap bitmap = memoryCache.get(cachedImage);
            imageView.setImageBitmap(bitmap);
        } else {
            bitmapLoader.loadThumbnail(cachedImage, imageView);
        }
    }

    public void loadFullscreenImage(final Media media, final ImageView imageView) {
        CachedImage cachedImage = diskCache.getFullscreenImage(media);
        if (memoryCache.contains(cachedImage)) {
            Bitmap bitmap = memoryCache.get(cachedImage);
            imageView.setImageBitmap(bitmap);
        } else {
            bitmapLoader.loadFullscreenImage(cachedImage, imageView);
        }
    }

    public void saveFullscreenImage(final Media media, final ImageView imageView) {
        CachedImage cachedImage = diskCache.getFullscreenImage(media);
        bitmapCreator.createFullscreenImage(cachedImage, media, imageView);
    }

    public void saveThumbnail(final Media media, final ImageView imageView) {
        CachedImage cachedImage = diskCache.getThumbnail(media);
        bitmapCreator.createThumbnail(cachedImage, media, imageView);
    }

}
