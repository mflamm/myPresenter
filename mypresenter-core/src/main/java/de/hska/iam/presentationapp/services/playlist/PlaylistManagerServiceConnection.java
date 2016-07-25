/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PlaylistManagerServiceConnection.java
 *  @package	de.hska.iam.presentationapp.services.playlist
 *  @brief      The connection to the PlaylistManagerService.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 29.05.2014 Markus Maier
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
import android.content.ComponentName;
import android.os.IBinder;
import de.hska.iam.presentationapp.playlist.PlaylistManager;
import de.hska.iam.presentationapp.services.ManagedServiceConnection;

public class PlaylistManagerServiceConnection extends ManagedServiceConnection {

    private PlaylistManagerService playlistManagerService;
    private PlaylistManagerServiceConnectionListener callback;

    public interface PlaylistManagerServiceConnectionListener extends ServiceConnectionListener {
        void onServiceConnected(PlaylistManager playlistManager);
    }

    /**
     * Sets the callback function which is invoked when the service is connected.
     *
     * @param callback The instance which implements callback function of the service.
     */
    @Override
    public void setCallback(final ServiceConnectionListener callback) {
        try {
            this.callback = (PlaylistManagerServiceConnectionListener) callback;
        } catch (final ClassCastException ignored) {
            throw new ClassCastException("Wrong callback " + callback + ". Class must implement PlaylistManagerServiceConnectionListener to use the playlist manager service.");
        }
    }

    @Override
    protected Class<? extends Service> getServiceClass() {
        return PlaylistManagerService.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
     */
    @Override
    public void onServiceConnected(final ComponentName name, final IBinder service) {
        PlaylistManagerService.PlaylistManagerBinder playlistManagerBinder = (PlaylistManagerService.PlaylistManagerBinder) service;
        playlistManagerService = playlistManagerBinder.getService();
        PlaylistManager playlistManager = playlistManagerService.getPlaylistManager();
        callback.onServiceConnected(playlistManager);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.content.onServiceDisconnected#onServiceConnected(android.content.ComponentName)
     */
    @Override
    public void onServiceDisconnected(final ComponentName name) {
        playlistManagerService = null;
    }

}
