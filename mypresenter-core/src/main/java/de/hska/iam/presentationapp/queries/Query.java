/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       Query.java
 *  @package	de.hska.iam.presentationapp.queries
 *  @brief      Base class for a database query.
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

package de.hska.iam.presentationapp.queries;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.List;

public abstract class Query {

    private final Context context;

    Query(final Context context) {
        this.context = context;
    }

    public Cursor createCursor() {
        Uri uri = getUri();
        String[] projection = getProjectionWithId();
        String selection = getSelection();
        String[] selectionArgs = getSelectionArgs();
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
    }

    public Loader<Cursor> createCursorLoader() {
        Uri uri = getUri();
        String[] projection = getProjectionWithId();
        String selection = getSelection();
        String[] selectionArgs = getSelectionArgs();
        return new CursorLoader(context, uri, projection, selection, selectionArgs, null);
    }

    protected abstract Uri getUri();

    private String[] getProjectionWithId() {
        List<String> projection = getProjection();
        projection.add(0, BaseColumns._ID);
        return projection.toArray(new String[projection.size()]);
    }

    protected abstract List<String> getProjection();

    protected abstract String getSelection();

    protected abstract String[] getSelectionArgs();

}
