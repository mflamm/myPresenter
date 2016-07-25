/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ThumbnailAdapterHelper.java
 *  @package	de.hska.iam.presentation.fragments.adapter
 *  @brief      Helper class for the ThumbnailAdapter. Creates
 *              thumbnails from cursor data and manages the
 *              thumbnail cache.
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

package de.hska.iam.presentationapp.fragments.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.ImageView;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.media.MediaFactory;

class ThumbnailAdapterHelper {

    private final Context context;
    private BitmapCacheManager cache;

    ThumbnailAdapterHelper(final Context context) {
        this.context = context;
    }

    public void setBitmapCacheManager(final BitmapCacheManager bitmapCacheManager) {
        cache = bitmapCacheManager;
    }

    public void addImageToView(final ImageView imageView, final Cursor cursor) {
        Media media = getMedia(cursor);
        if (media == null) {
            return;
        }

        if (cache.containsThumbnail(media)) {
            cache.loadThumbnail(media, imageView);
        } else {
            cache.saveThumbnail(media, imageView);
        }
    }

    public Media getMedia(final Cursor cursor) {
        String absolutePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
        Media media = null;
        if (absolutePath != null) {
            media = MediaFactory.INSTANCE.getMedia(context, absolutePath);
        }
        return media;
    }

}
