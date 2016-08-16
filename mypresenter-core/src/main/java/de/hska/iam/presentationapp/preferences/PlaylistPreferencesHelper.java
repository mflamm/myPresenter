/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistPreferencesHelper.java
 *  @package    de.hska.iam.presentationapp.preferences
 *  @brief      Helper class for the playlists preferences.
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

package de.hska.iam.presentationapp.preferences;

import android.content.Context;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.media.MediaFactory;
import de.hska.iam.presentationapp.playlist.Playlist;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

final class PlaylistPreferencesHelper {

    private PlaylistPreferencesHelper() {
    }

    public static Set<String> getPlaylistNames(final Iterable<Playlist> playlistManager) {
        Set<String> playlistNames = new HashSet<>();
        for (final Playlist playlist : playlistManager) {
            String playlistName = playlist.getName();
            playlistNames.add(playlistName);
        }
        return playlistNames;
    }

    public static Set<String> getFilePaths(final Playlist playlist) {
        Set<String> filePaths = new HashSet<>(playlist.getNumberOfItems());
        for (final Media media : playlist) {
            String absolutePath = media.getAbsolutePath();
            filePaths.add(absolutePath);
        }
        return filePaths;
    }

    public static Playlist createPlaylist(final String name, final Iterable<String> filePaths, final Context context) {
        Playlist playlist = new Playlist(context, name);
        for (final String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists()) {
                Media media = MediaFactory.INSTANCE.getMedia(context, filePath);
                if (media != null) {
                    playlist.add(media);
                }
            }
        }
        return playlist;
    }

}
