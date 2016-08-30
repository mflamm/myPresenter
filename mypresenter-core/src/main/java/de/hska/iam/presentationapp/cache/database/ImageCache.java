/**
 * @MyPresenter Presentation-App for Android-Devices
 * @copyright 2015 IMP - Institute of Materials and Processes
 * University of Applied Sciences
 * Karlsruhe
 * @file ImageCache.java
 * @package de.hska.iam.presentationapp.cache.database
 * @brief Wrapper for a collection of {@link CachedImage}.
 * <p/>
 * <p/>
 * *******************************************************************
 * @lastmodified 29.05.2015 Markus Maier
 * <p/>
 * *******************************************************************
 * <p/>
 * LICENSE:
 * <p/>
 * MyPresenter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * @MyPresenter Presentation-App for Android-Devices
 * @copyright 2015 IMP - Institute of Materials and Processes
 * University of Applied Sciences
 * Karlsruhe
 * @file ImageCache.java
 * @package de.hska.iam.presentationapp.cache.database
 * @brief Wrapper for a collection of {@link CachedImage}.
 * <p/>
 * <p/>
 * *******************************************************************
 * @lastmodified 29.05.2015 Markus Maier
 * <p/>
 * *******************************************************************
 * <p/>
 * LICENSE:
 * <p/>
 * MyPresenter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file ImageCache.java
 *  @package de.hska.iam.presentationapp.cache.database
 *  @brief Wrapper for a collection of {@link CachedImage}.
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

import android.content.ContentProviderOperation;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class ImageCache {

    private final Map<Long, CachedImage> cachedImages;
    private final Map<String, ArrayList<Long>> mediaPathToIdHint;

    ImageCache() {
        cachedImages = new ConcurrentHashMap<>();
        mediaPathToIdHint = new ConcurrentHashMap<>();
    }

    public CachedImage get(final long id) {
        return cachedImages.get(id);
    }

    public ArrayList<Long> getIdsByMediaFilePath(String mediaFilePath) {
        if (mediaPathToIdHint.containsKey(mediaFilePath)) {
            //Log.i("IMAGE CACHE", "mediaPathToIdHint HIT");
            ArrayList<Long> possibleCacheHits = mediaPathToIdHint.get(mediaFilePath);
            Log.i("IMAGE CACHE", "CacheHint HIT Count - " + possibleCacheHits.size());
            return possibleCacheHits;
        } else {
            Log.i("IMAGE CACHE", "CacheHint NO HIT");
            return new ArrayList<>();
        }
    }

    public void put(final CachedImage cachedImage) {
        long id = cachedImage.getId();
        String mediaFilePath = cachedImage.getMediaFilePath();
        //Putting Image into Cache
        cachedImages.put(id, cachedImage);

        ArrayList<Long> cachedEntrysForMediaPath = mediaPathToIdHint.get(mediaFilePath);
        if (cachedEntrysForMediaPath != null && !cachedEntrysForMediaPath.contains(id)) {
            cachedEntrysForMediaPath.add(id);
            mediaPathToIdHint.put(mediaFilePath, cachedEntrysForMediaPath);
            Log.i("IMAGE CACHE", cachedEntrysForMediaPath.size() + " Hits: put() Hint in List - " + cachedImage.getMediaFilePath());
        } else if (cachedEntrysForMediaPath == null) {
            cachedEntrysForMediaPath = new ArrayList<>();
            cachedEntrysForMediaPath.add(id);
            mediaPathToIdHint.put(mediaFilePath, cachedEntrysForMediaPath);
            Log.i("IMAGE CACHE", "No Hints: put() new List - " + cachedImage.getMediaFilePath());
        }

        if (id % 20 == 0) {
            Integer size = cachedImages.size();
            Log.i("Image Cache", "Number of cached images: " + size.toString());
        }
    }

    public void remove(final CachedImage cachedImage) {
        long id = cachedImage.getId();
        Log.i("Cache", "ID: " + id + "File: " + cachedImage.getMediaFilePath() + " - deleted");
        synchronized (this) {
            cachedImages.remove(id);

            ArrayList<Long> possibleEntrys = mediaPathToIdHint.get(cachedImage.getMediaFilePath());
            if (possibleEntrys != null) {
                Log.i("IMAGE CACHE", "Cache Hint remove() - Hit Count: " + possibleEntrys.size());
                Log.i("IMAGE CACHE", "Cache Hint remove() - ID: " + id + "File: " + cachedImage.getMediaFilePath() + " - deleted");
                possibleEntrys.remove(possibleEntrys.indexOf(cachedImage.getId()));
                mediaPathToIdHint.put(cachedImage.getMediaFilePath(), possibleEntrys);
            } else {
                Log.i("IMAGE CACHE", "remove(), possibleEntrys empty");
            }
        }
    }

    public Collection<CachedImage> getAll() {
        return cachedImages.values();
    }
}
