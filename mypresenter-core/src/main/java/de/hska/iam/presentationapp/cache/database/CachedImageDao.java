/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      CachedImageDao.java
 *  @package   de.hska.iam.presentationapp.cache.database
 *  @brief     Database access object of the cached images
 *             database.
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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class CachedImageDao extends SQLiteOpenHelper {

    private static final String TABLE = "cachedimages";
    private static final String ID = "_id";
    private static final String IMAGE_TYPE = "image_type";
    private static final String MEDIA_FILE_PATH = "media_file_path";
    private static final String IMAGE_FILE_PATH = "image_file_path";
    private static final String PDF_PAGE_NUMBER = "pdf_page_number";

    private static final String DATABASE_NAME = "cachedimages.db";
    private static final int DATABASE_VERSION = 1;

    private static final String[] COLUMNS = {
        ID,
        IMAGE_TYPE,
        MEDIA_FILE_PATH,
        IMAGE_FILE_PATH,
        PDF_PAGE_NUMBER
    };

    CachedImageDao(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        String createTable = String.format("create table if not exists %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s text);",
            TABLE,
            ID,
            IMAGE_TYPE,
            MEDIA_FILE_PATH,
            IMAGE_FILE_PATH,
            PDF_PAGE_NUMBER
        );
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(getClass().getName(), "Upgrading database from version "
            + oldVersion + " to "+ newVersion
            + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public CachedImage create(final CachedImage.ImageType imageType, final String mediaFilePath, final int pdfPageNumber) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(IMAGE_TYPE, String.valueOf(imageType));
        values.put(MEDIA_FILE_PATH, mediaFilePath);
        values.put(IMAGE_FILE_PATH, "");
        values.put(PDF_PAGE_NUMBER, pdfPageNumber);
        long id = 0;
        try {
            id =  writableDatabase.insertOrThrow(TABLE, null, values);
        } catch (final SQLiteException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
        writableDatabase.close();
        return new CachedImage(id, imageType, mediaFilePath, "", pdfPageNumber);
    }

    public void update(final CachedImage cachedImage) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEDIA_FILE_PATH, cachedImage.getMediaFilePath());
        values.put(IMAGE_FILE_PATH, cachedImage.getImageFilePath());
        values.put(PDF_PAGE_NUMBER, cachedImage.getPdfPageNumber());
        Long id = cachedImage.getId();
        writableDatabase.update(TABLE, values, ID + " = ?", new String[]{Long.toString(id)});
        writableDatabase.close();
    }

    public void delete(final CachedImage cachedImage) {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        long id = cachedImage.getId();
        writableDatabase.delete(TABLE, ID + " = ?", new String[]{Long.toString(id)});
        writableDatabase.close();
    }

    public List<CachedImage> getAll() {
        final SQLiteDatabase readableDatabase = getReadableDatabase();
        List<CachedImage> cachedImages = new ArrayList<>();
        final Cursor cursor = readableDatabase.query(
            TABLE,
            COLUMNS,
            null,
            null,
            null,
            null,
            null
        );
        try {
            while (cursor.moveToNext()) {
                CachedImage cachedImage = cachedImageFromCursor(cursor);
                cachedImages.add(cachedImage);
            }
        } finally {
            cursor.close();
        }
        readableDatabase.close();
        return cachedImages;
    }

    private static CachedImage cachedImageFromCursor(final Cursor cursor) {
        long id = cursor.getLong(0);
        CachedImage.ImageType imageType = CachedImage.ImageType.valueOf(cursor.getString(1));
        String mediaFilePath = cursor.getString(2);
        String imageFilePath = cursor.getString(3);
        int pdfPageNumber = Integer.parseInt(cursor.getString(4));
        return new CachedImage(id, imageType, mediaFilePath, imageFilePath, pdfPageNumber);
    }

}
