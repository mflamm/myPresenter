/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       Preferences.java
 *  @package	de.hska.iam.presentationapp.preferences
 *  @brief      Helper class for preferences.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 11.01.2015 Markus Maier
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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

abstract class Preferences {

    private static final String SHARED_PREFERENCES = "mypresenter_preferences";
    private final SharedPreferences sharedPreferences;

    Preferences(final Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
    }

    public abstract void clear();

    SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

}
