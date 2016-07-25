/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ThumbnailAdapter.java
 *  @package	de.hska.iam.presentation.fragments.adapter
 *  @brief      Creates views with thumbnails of the data from the
 *              given cursor.
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
import android.graphics.Color;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;
import de.hska.iam.presentationapp.media.Media;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThumbnailAdapter extends ResourceCursorAdapter implements Iterable<Media> {

    private final ThumbnailAdapterHelper helper;
    private final List<Integer> selectedItemPositions;

    public ThumbnailAdapter(final Context context, final int layout, final Cursor c, final int flags) {
        super(context, layout, c, flags);
        helper = new ThumbnailAdapterHelper(context);
        selectedItemPositions = new ArrayList<>();
    }

    public void setBitmapCacheManager(final BitmapCacheManager bitmapCacheManager) {
        helper.setBitmapCacheManager(bitmapCacheManager);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.widget.CursorAdapter#bindView(android.view.View, android.content.Context, android.database.Cursor)
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        restoreViewSelection(view, cursor);
        ImageView imageView = (ImageView) view;
        helper.addImageToView(imageView, cursor);
    }

    private void restoreViewSelection(final View view, final Cursor cursor) {
        int position = cursor.getPosition();
        if (isItemPositionSelected(position)) {
            selectView(view);
        } else {
            deselectView(view);
        }
    }

    public static void selectView(final View view) {
        view.setBackgroundColor(Color.BLUE);
    }

    public static void deselectView(final View view) {
        view.setBackgroundColor(Color.TRANSPARENT);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.widget.CursorAdapter#getItem(int)
     */
    @Override
    public Media getItem(final int position) {
        Cursor cursor = getCursor();
        int oldPosition = cursor.getPosition();
        cursor.moveToPosition(position);
        Media media = helper.getMedia(cursor);
        cursor.moveToPosition(oldPosition);
        return media;
    }

    public Media getSelectedItem(final int position) {
        int selectedItemPosition = selectedItemPositions.get(position);
        return getItem(selectedItemPosition);
    }

    public int getIndexOfSelectedItemPosition(final int position) {
        return selectedItemPositions.indexOf(position);
    }

    public boolean isItemPositionSelected(final int position) {

       return selectedItemPositions.contains(position);
    }

    public void addSelectedItemPosition(final int position) {
        selectedItemPositions.add(position);
    }

    public void removeSelectedItemPosition(final int position) {
        int index = selectedItemPositions.indexOf(position);
        selectedItemPositions.remove(index);
    }

    public boolean hasSelectedItems() {
        return !selectedItemPositions.isEmpty();
    }

    public int getSelectedItemPositionsCount() {
        return selectedItemPositions.size();
    }

    public void clearSelectedItems() {
        selectedItemPositions.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Media> iterator() {
        return new ThumbnailAdapterIterator(this);
    }

}
