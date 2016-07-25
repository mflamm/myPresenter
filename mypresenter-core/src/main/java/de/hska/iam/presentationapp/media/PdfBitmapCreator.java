/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PdfBitmapCreator.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      Creates a bitmap from a pdf.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 23.02.2015 Markus Maier
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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import de.hska.iam.presentationapp.R;

final class PdfBitmapCreator {

    private static final int THUMBNAIL_SIZE = 150;

    private PdfBitmapCreator() {
    }

    public static Bitmap createFullscreenImage(final PdfPage pdfPage) {
        int pdfPageWidth = pdfPage.getWidth();
        int pdfPageHeight = pdfPage.getHeight();
        Bitmap bitmap = createBitmapCanvas(pdfPageWidth, pdfPageHeight);
        pdfPage.render(bitmap, null, null);
        return bitmap;
    }

    public static Bitmap createThumbnail(final PdfPage pdfPage) {
        Context context = pdfPage.getContext();
        Bitmap bitmap = createBitmapCanvas(THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        pdfPage.render(bitmap, null, null);
        drawPdfIcon(context, bitmap);
        return bitmap;
    }

    private static void drawPdfIcon(final Context context, final Bitmap bitmap) {
        Bitmap pdfIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf_document_icon_96);
        Rect src = new Rect(0, 0, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        Rect dst = new Rect(100, 100, THUMBNAIL_SIZE, THUMBNAIL_SIZE);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(pdfIcon, src, dst, null);
    }

    private static Bitmap createBitmapCanvas(final int width, final int height) {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

}
