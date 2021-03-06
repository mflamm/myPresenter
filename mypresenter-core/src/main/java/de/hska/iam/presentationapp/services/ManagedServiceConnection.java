/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ManagedServiceConnection.java
 *  @package	de.hska.iam.presentationapp.services.playlist
 *  @brief      Base class for service connections which are
 *              managed by the ServiceConnector.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 11.02.2015 Markus Maier
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
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public abstract class ManagedServiceConnection implements ServiceConnection {

    private boolean connected;

    public interface ServiceConnectionListener {
    }

    public final void connect(final Activity activity, final ServiceConnectionListener callback) {
        setCallback(callback);
        connect(activity);
    }

    public final void disconnect(final Activity activity) {
        if (connected) {
            activity.unbindService(this);
            connected = false;
        }
    }

    protected abstract void setCallback(final ServiceConnectionListener callback);

    private void connect(final Activity activity) {
        Class<? extends Service> serviceClass = getServiceClass();
        Intent intent = new Intent(activity, serviceClass);
        activity.bindService(intent, this, Context.BIND_AUTO_CREATE);
        connected = true;
    }

    protected abstract Class<? extends Service> getServiceClass();

}
