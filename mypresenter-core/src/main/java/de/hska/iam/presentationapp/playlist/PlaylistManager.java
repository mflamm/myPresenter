/**
 * @MyPresenter Presentation-App for Android-Devices
 * @copyright 2014 IMP - Institute of Materials and Processes
 * University of Applied Sciences
 * Karlsruhe
 * @file PlaylistManager.java
 * @package de.hska.iam.presentationapp.playlist
 * @brief Manages playlists of media files.
 * <p/>
 * <p/>
 * *******************************************************************
 * @lastmodified 29.05.2015 Markus Maier
 * <p/>
 * *******************************************************************
 * <p/>
 * LICENSE:
 * <p/>
 * MyPresenter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file PlaylistManager.java
 *  @package de.hska.iam.presentationapp.playlist
 *  @brief Manages playlists of media files.
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

package de.hska.iam.presentationapp.playlist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PlaylistManager {

    private final List<Playlist> playlists;

    public PlaylistManager() {
        playlists = new ArrayList<>();
    }

    /**
     * Adds a playlist.
     *
     * @param playlist
     *          The playlist that shall be added.
     */
    public void add(final Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * Removes a playlist.
     *
     * @param playlist
     *          The playlist that shall be removed.
     */
    public void remove(final Playlist playlist) {
        playlists.remove(playlist);
    }

    /**
     * Clears the playlists.
     */
    public void clear() {
        playlists.clear();
    }

    public Playlist get(final int index) {
        return playlists.get(index);
    }

    /**
     * Sets a playlist. Clears the list of
     * playlists and adds the given playlist
     * as the only playlist to the list.
     *
     * @param playlist
     *          The playlist that shall be set.
     */
    public void set(final Playlist playlist) {
        playlists.clear();
        playlists.add(playlist);
    }

    /**
     * Returns all playlists.
     */
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Returns all Playlistnames.
     */
    public Set<String> getPlaylistNames() {
        Set<String> playlistNames = new HashSet<String>();
        for(Playlist playlist : playlists){
            playlistNames.add(playlist.getName());
        }
        return playlistNames;
    }
}
