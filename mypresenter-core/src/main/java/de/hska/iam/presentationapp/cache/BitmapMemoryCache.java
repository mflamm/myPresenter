/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      BitmapMemoryCache.java
 *  @package   de.hska.iam.presentationapp.cache
 *  @brief     Provides a memory cache for bitmaps.
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

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import de.hska.iam.presentationapp.cache.database.CachedImage;

class BitmapMemoryCache {

    private static final int KILOBYTE = 1024;
    private final LruCache<Long, Bitmap> lruCache;

    BitmapMemoryCache(final BitmapDiskCache diskCache) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / KILOBYTE);
        int cacheSize = maxMemory / 8;
        lruCache = new BitmapLruCache(cacheSize, diskCache);
    }

    public boolean contains(final CachedImage key) {
        long id = key.getId();
        return lruCache.get(id) != null;
    }

    public Bitmap get(final CachedImage key) {
        long id = key.getId();
        return lruCache.get(id);
    }

    public void put(final CachedImage key, final Bitmap value) {
        long id = key.getId();
        lruCache.put(id, value);
    }

    public void clear() {
        lruCache.evictAll();
    }

    private static class BitmapLruCache extends LruCache<Long, Bitmap> {

        private final BitmapDiskCache diskCache;

        BitmapLruCache(final int cacheSize, final BitmapDiskCache diskCache) {
            super(cacheSize);
            this.diskCache = diskCache;
        }

        @Override
        protected int sizeOf(final Long key, final Bitmap value) {
            return value.getByteCount() / KILOBYTE;
        }

        @Override
        protected void entryRemoved(final boolean evicted, final Long key, final Bitmap oldValue, final Bitmap newValue) {
            diskCache.put(key, oldValue);
        }

    }

}
