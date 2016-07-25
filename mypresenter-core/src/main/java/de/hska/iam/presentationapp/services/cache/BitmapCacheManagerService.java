/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       BitmapCacheManagerService.java
 *  @package	de.hska.iam.presentationapp.services.cache
 *  @brief      A service which provides access to the bitmap
 *              cache manager.
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

package de.hska.iam.presentationapp.services.cache;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;

public class BitmapCacheManagerService extends Service {

    private final IBinder binder = new BitmapCacheManagerBinder();
    private BitmapCacheManager bitmapCacheManager;

    class BitmapCacheManagerBinder extends Binder {
        public BitmapCacheManagerService getService() {
            return BitmapCacheManagerService.this;
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
        bitmapCacheManager = new BitmapCacheManager(context);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Service#onLowMemory()
     */
    @Override
    public void onLowMemory() {
        bitmapCacheManager.clearMemoryCache();
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

    public BitmapCacheManager getBitmapCacheManager() {
        return bitmapCacheManager;
    }

}
