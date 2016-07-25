/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistManagerService.java
 *  @package	de.hska.iam.presentationapp.services.playlist
 *  @brief      A service which provides access to the playlist
 *              manager.
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

package de.hska.iam.presentationapp.services.playlist;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import de.hska.iam.presentationapp.playlist.PlaylistManager;
import de.hska.iam.presentationapp.preferences.PlaylistPreferences;

public class PlaylistManagerService extends Service {

    private final IBinder binder = new PlaylistManagerBinder();
    private PlaylistManager playlistManager;
    private PlaylistPreferences playlistPreferences;

    class PlaylistManagerBinder extends Binder {
        public PlaylistManagerService getService() {
            return PlaylistManagerService.this;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        Context context = getBaseContext();
        playlistManager = new PlaylistManager();
        playlistPreferences = new PlaylistPreferences(context);
        playlistPreferences.loadPlaylists(playlistManager);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onLowMemory()
     */
    @Override
    public void onDestroy() {
        playlistPreferences.clear();
        playlistPreferences.savePlaylists(playlistManager);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(final Intent intent) {
        return binder;
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

}
