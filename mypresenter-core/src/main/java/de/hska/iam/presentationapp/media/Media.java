/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       Media.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      Base class for a media file.
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

public class Media {

    private final Context context;
    private final String absolutePath;

    Media(final Context context, final String absolutePath) {
        this.context = context;
        this.absolutePath = absolutePath;
    }

    public Context getContext() {
        return context;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public Bitmap createThumbnail() {
        return null;
    }

    public Bitmap createFullscreenImage() {
        return null;
    }

}
