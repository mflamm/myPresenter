/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistPreferences.java
 *  @package    de.hska.iam.presentationapp.preferences
 *  @brief      Preferences for the playlists.
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
import android.content.SharedPreferences;
import de.hska.iam.presentationapp.playlist.Playlist;
import de.hska.iam.presentationapp.playlist.PlaylistManager;

import java.util.HashSet;
import java.util.Set;

public class PlaylistPreferences extends Preferences {

    private static final String PLAYLISTS = "playlists";
    private final Context context;

    public PlaylistPreferences(final Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void clear() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = getEditor();
        Set<String> playlistNames = sharedPreferences.getStringSet(PLAYLISTS, new HashSet<String>());
        for (final String playlistName : playlistNames) {
            editor.remove(playlistName);
        }
        editor.remove(PLAYLISTS);
        editor.apply();
    }

    public void savePlaylists(final PlaylistManager playlistManager) {
        savePlaylistNames(playlistManager);
        for (final Playlist playlist : playlistManager) {
            savePlaylist(playlist);
        }
    }

    private void savePlaylistNames(final PlaylistManager playlistManager) {
        Set<String> playlistNames = PlaylistPreferencesHelper.getPlaylistNames(playlistManager);
        SharedPreferences.Editor editor = getEditor();
        editor.putStringSet(PLAYLISTS, playlistNames);
        editor.apply();
    }

    private void savePlaylist(final Playlist playlist) {
        String name = playlist.getName();
        Set<String> filePaths = PlaylistPreferencesHelper.getFilePaths(playlist);
        SharedPreferences.Editor editor = getEditor();
        editor.putStringSet(name, filePaths);
        editor.apply();
    }

    public void loadPlaylists(final PlaylistManager playlistManager) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> playlistNames = sharedPreferences.getStringSet(PLAYLISTS, new HashSet<String>());
        for (final String playlistName : playlistNames) {
            Set<String> filePaths = sharedPreferences.getStringSet(playlistName, new HashSet<String>());
            Playlist playlist = PlaylistPreferencesHelper.createPlaylist(playlistName, filePaths, context);
            playlistManager.add(playlist);
        }
    }

}
