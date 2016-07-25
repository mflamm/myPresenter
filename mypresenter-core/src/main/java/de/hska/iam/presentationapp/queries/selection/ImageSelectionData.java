/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ImageSelectionData.java
 *  @package	de.hska.iam.presentationapp.queries.selection
 *  @brief	    Holds the selection data for images.
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

package de.hska.iam.presentationapp.queries.selection;

import android.provider.MediaStore;

public class ImageSelectionData implements SelectionData {

    private static final String SELECTION = MediaStore.Files.FileColumns.MEDIA_TYPE + " = ?";
    private static final String SELECTION_ARG = String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

    @Override
    public String getSelection() {
        return SELECTION;
    }

    @Override
    public String getSelectionArg() {
        return SELECTION_ARG;
    }

}
