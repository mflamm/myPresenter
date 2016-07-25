/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       VideoBitmapCreator.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      Creates a bitmap from a video.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 20.02.2015 Markus Maier
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

package de.hska.iam.presentationapp.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import de.hska.iam.presentationapp.R;

final class VideoBitmapCreator {

    private VideoBitmapCreator() {
    }

    public static Bitmap createThumbnail(final Video video) {
        Bitmap bm = createVideoThumbnail(video);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bm);

        Context context = video.getContext();
        Bitmap playIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.playbtn);
        canvas.drawBitmap(playIcon, 42.0f, 42.0f, new Paint(1));
        return bm;
    }

    private static Bitmap createVideoThumbnail(final Video video) {
        String absolutePath = video.getAbsolutePath();
        Bitmap bm = ThumbnailUtils.createVideoThumbnail(absolutePath, MediaStore.Video.Thumbnails.MINI_KIND);
        bm = ThumbnailUtils.extractThumbnail(bm, 150, 150);
        return bm;
    }

}
