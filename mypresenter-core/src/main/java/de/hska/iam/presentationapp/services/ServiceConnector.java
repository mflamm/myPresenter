/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ServiceConnector.java
 *  @package	de.hska.iam.presentationapp.services
 *  @brief      Manages the connection to a Service.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 17.02.2015 Markus Maier
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

package de.hska.iam.presentationapp.services;

import android.app.Activity;
import de.hska.iam.presentationapp.services.cache.BitmapCacheManagerServiceConnection;
import de.hska.iam.presentationapp.services.playlist.PlaylistManagerServiceConnection;

import java.util.EnumMap;
import java.util.Map;

public class ServiceConnector {

    private final Activity activity;
    private final Map<Services, ManagedServiceConnection> serviceConnections;

    public enum Services {
        PLAYLIST_MANAGER,
        BITMAP_CACHE_MANAGER
    }

    public ServiceConnector(final Activity activity) {
        this.activity = activity;
        serviceConnections = new EnumMap<>(Services.class);
        serviceConnections.put(Services.PLAYLIST_MANAGER, new PlaylistManagerServiceConnection());
        serviceConnections.put(Services.BITMAP_CACHE_MANAGER, new BitmapCacheManagerServiceConnection());
    }

    public void connect(final Services serviceName, final ManagedServiceConnection.ServiceConnectionListener callback) {
        ManagedServiceConnection serviceConnection = serviceConnections.get(serviceName);
        serviceConnection.connect(activity, callback);
    }

    public void disconnect(final Services serviceName) {
        ManagedServiceConnection serviceConnection = serviceConnections.get(serviceName);
        serviceConnection.disconnect(activity);
    }

}
