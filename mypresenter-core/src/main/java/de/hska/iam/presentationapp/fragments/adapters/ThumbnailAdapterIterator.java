/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ThumbnailAdapterIterator.java
 *  @package	de.hska.iam.presentationapp.fragments.adapter
 *  @brief      An iterator to iterate over the thumbnail adapter.
 *              If items are selected returns only the selected
 *              items else all items are returned. Returns media
 *              files.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 29.05.2015 Markus Maier
 *
 ********************************************************************
 *
 *  LICENSE:
 *
 *  MyPresenter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 ********************************************************************/

package de.hska.iam.presentationapp.fragments.adapters;

import de.hska.iam.presentationapp.media.Media;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ThumbnailAdapterIterator implements Iterator<Media> {

    private final ThumbnailAdapter thumbnailAdapter;
    private final int count;
    private int position;

    ThumbnailAdapterIterator(final ThumbnailAdapter thumbnailAdapter) {
        this.thumbnailAdapter = thumbnailAdapter;
        if (thumbnailAdapter.hasSelectedItems()) {
            count = thumbnailAdapter.getSelectedItemPositionsCount();
        } else {
            count = thumbnailAdapter.getCount();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return position < count;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Iterator#next()
     */
    @Override
    public Media next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Media media;
        if (thumbnailAdapter.hasSelectedItems()) {
            media = thumbnailAdapter.getSelectedItem(position);
        } else {
            media = thumbnailAdapter.getItem(position);
        }
        position++;

        return media;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
    }

}
