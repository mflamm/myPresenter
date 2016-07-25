/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PdfPage.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      A pdf page from a pdf file.
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

package de.hska.iam.presentationapp.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import de.hska.iam.presentationapp.util.PdfRenderer;

public class PdfPage extends Media {

    private final PdfRenderer.Page page;

    PdfPage(final Context context, final String absolutePath, final PdfRenderer.Page page) {
        super(context, absolutePath);
        this.page = page;
    }

    public int getWidth() {
        return page.getWidth();
    }

    public int getHeight() {
        return page.getHeight();
    }

    public int getPageNumber() {
        return page.getIndex();
    }

    public void render(final Bitmap bitmap, final Rect destClip, final Matrix transform) {
        page.render(bitmap, destClip, transform);
    }

    @Override
    public Bitmap createFullscreenImage() {
        return PdfBitmapCreator.createFullscreenImage(this);
    }

}
