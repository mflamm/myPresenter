/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistHelper.java
 *  @package	de.hska.iam.presentation.fragments
 *  @brief      Helper class of the GridViewFragment.
 *              Abstracts the handling of the Playlist.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 02.08.2014 Benjamin Roth
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

package de.hska.iam.presentationapp.fragments;

import de.hska.iam.presentationapp.fragments.adapters.ThumbnailAdapter;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.playlist.Playlist;
import de.hska.iam.presentationapp.playlist.PlaylistManager;

class PlaylistHelper {

    private final ThumbnailAdapter thumbnailAdapter;
    private PlaylistManager playlistManager;

    PlaylistHelper(final ThumbnailAdapter thumbnailAdapter) {
        this.thumbnailAdapter = thumbnailAdapter;
    }

    public void setPlaylistManager(final PlaylistManager playlistManager) {
        this.playlistManager = playlistManager;
    }

    public void addPlaylistToPlaylistManager(final Playlist playlist) {
        playlistManager.set(playlist);
    }

    public void addMediaFiles(final Playlist playlist) {
        for (final Media media : thumbnailAdapter) {
            if (media != null) {
                playlist.add(media);
            }
        }
    }

}
