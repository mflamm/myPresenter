/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       MediaFormatFiltersPreferences.java
 *  @package    de.hska.iam.presentationapp.preferences
 *  @brief      Preferences of the ChooseFormatDialog.
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

public class MediaFormatFiltersPreferences extends Preferences {

    private static final String SHOW_IMAGES = "showImages";
    private static final String SHOW_VIDEOS = "showVideos";
    private static final String SHOW_PDFS = "showPdfs";


    public MediaFormatFiltersPreferences(final Context context) {
        super(context);
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(SHOW_IMAGES);
        editor.remove(SHOW_VIDEOS);
        editor.remove(SHOW_PDFS);
        editor.apply();
    }

    public boolean[] getMediaFormatFilters() {
        boolean[] mediaFormatFilters = new boolean[3];
        SharedPreferences sharedPreferences = getSharedPreferences();
        mediaFormatFilters[0] = sharedPreferences.getBoolean(SHOW_IMAGES, true);
        mediaFormatFilters[1] = sharedPreferences.getBoolean(SHOW_VIDEOS, true);
        mediaFormatFilters[2] = sharedPreferences.getBoolean(SHOW_PDFS, true);
        return mediaFormatFilters;
    }

    public void saveMediaFormatFilters(final boolean... mediaFormatFilters) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(SHOW_IMAGES, mediaFormatFilters[0]);
        editor.putBoolean(SHOW_VIDEOS, mediaFormatFilters[1]);
        editor.putBoolean(SHOW_PDFS, mediaFormatFilters[2]);
        editor.apply();
    }

}
