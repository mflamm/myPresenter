/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      AsyncDrawableTask.java
 *  @package   de.hska.iam.presentationapp.cache
 *  @brief     A bitmap drawable which holds a reference to its
 *             AsyncTask. Can be used to cancel a set task when
 *             its ImageView is known.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 11.01.2015 Markus Maier
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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

class AsyncDrawableTask<T extends AsyncTask> extends BitmapDrawable {
    private final WeakReference<T> taskReference;

    AsyncDrawableTask(final Resources res, final Bitmap bitmap, final T task) {
        super(res, bitmap);
        taskReference = new WeakReference<>(task);
    }

    public T getTask() {
        return taskReference.get();
    }

}
