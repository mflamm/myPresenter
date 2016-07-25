/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       FlipperPreferences.java
 *  @package    de.hska.iam.presentationapp.preferences
 *  @brief      Preferences of the DisplayTimeDialog.
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

public class DisplayTimeDialogPreferences extends Preferences {

    private static final String SECONDS = "seconds";
    private static final String MINUTES = "minutes";

    public DisplayTimeDialogPreferences(final Context context) {
        super(context);
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(SECONDS);
        editor.remove(MINUTES);
        editor.apply();
    }

    public int getSeconds() {
        return getSharedPreferences().getInt(SECONDS, 5);
    }

    public void saveSeconds(final int seconds) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(SECONDS, seconds);
        editor.apply();
    }

    public int getMinutes() {
        return getSharedPreferences().getInt(MINUTES, 0);
    }

    public void saveMinutes(final int minutes) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(MINUTES, minutes);
        editor.apply();
    }
}
