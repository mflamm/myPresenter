/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistIterator.java
 *  @package	de.hska.iam.presentationapp.flipper
 *  @brief      A playlist iterator for the presentation flipper.
 *              It iterates over the bounds of a playlist i.e. when
 *              the end of the playlist is reached it starts from
 *              the beginning and the other way round.
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

package de.hska.iam.presentationapp.flipper;

import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.playlist.Playlist;

class PlaylistIterator {

    private final Playlist playlist;
    private int position;

    PlaylistIterator(final Playlist playlist) {
        this.playlist = playlist;
        position = playlist.getStartPosition() - 1;
    }

    /**
     * Gets the next media file
     * without incrementing the current
     * position.
     *
     * @return
     *      The next media file.
     */
    public Media peekNext() {
        int nextIndex = position + 1;
        if (nextIndex > playlist.getNumberOfItems() - 1) {
            nextIndex = 0;
        }
        return playlist.get(nextIndex);
    }

    /**
     * Gets the previous media file
     * without decrementing the current
     * position.
     *
     * @return
     *      The previous media file.
     */
    public Media peekPrevious() {
        int previousIndex = position - 1;
        if (previousIndex < 0) {
            previousIndex = playlist.getNumberOfItems() - 1;
        }
        return playlist.get(previousIndex);
    }

    /**
     * Gets the next media file.
     *
     * @return
     *      The next media file.
     */
    public Media next() {
        position += 1;
        if (position > playlist.getNumberOfItems() - 1) {
            position = 0;
        }
        return playlist.get(position);
    }

    /**
     * Gets the previous media file.
     *
     * @return
     *      The previous media file.
     */
    public Media previous() {
        position -= 1;
        if (position < 0) {
            position = playlist.getNumberOfItems() - 1;
        }
        return playlist.get(position);
    }

}
