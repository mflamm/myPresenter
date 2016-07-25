/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PdfRenderer.java
 *  @package	de.hska.iam.presentationapp.media.pdf
 *  @brief      Wrapper for the MuPDF library for rendering pdf
 *              files. Mimics the API of
 *              {@link android.graphics.pdf.PdfRenderer} so it
 *              can be replaced in the future when the minSDK
 *              version is raised to 21 (Android 5.0).
 *
 *
 ********************************************************************
 *
 *  @lastmodified 23.02.2015 Markus Maier
 *
 ********************************************************************
 *
 *  LICENSE:
 *
 *  MyPresenter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 ********************************************************************/

package de.hska.iam.presentationapp.util;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import com.artifex.mupdfdemo.MuPDFCore;

public final class PdfRenderer {

    private MuPDFCore core;

    public PdfRenderer(final Context context, final String absolutePath) {
        try {
            core = new MuPDFCore(context, absolutePath);
        } catch (final Exception e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    public void close() {
        core.onDestroy();
    }

    public int getPageCount() {
        return core.countPages();
    }

    public Page openPage(final int index) {
        return new Page(index);
    }

    public class Page {

        private final int index;
        private final MuPDFCore.Cookie cookie;

        Page(final int index) {
            this.index = index;
            cookie = core.new Cookie();
        }

        public void close() {
            cookie.destroy();
        }

        public int getIndex() {
            return index;
        }

        public int getWidth() {
            PointF pageSize = core.getPageSize(index);
            return (int) pageSize.x;
        }

        public int getHeight() {
            PointF pageSize = core.getPageSize(index);
            return (int) pageSize.y;
        }

        public void render(final Bitmap destination, final Rect destClip, final Matrix transform) {
            if (destination.getConfig() != Bitmap.Config.ARGB_8888) {
                throw new IllegalArgumentException("Unsupported pixel format");
            }

            if (destClip != null) {
                if (destClip.left < 0 || destClip.top < 0
                    || destClip.right > destination.getWidth()
                    || destClip.bottom > destination.getHeight()) {
                    throw new IllegalArgumentException("destBounds not in destination");
                }
            }

            final int contentLeft = destClip != null ? destClip.left : 0;
            final int contentTop = destClip != null ? destClip.top : 0;
            final int contentRight = destClip != null ? destClip.right : destination.getWidth();
            final int contentBottom = destClip != null ? destClip.bottom : destination.getHeight();

            drawPage(destination, index, contentLeft, contentTop, contentRight, contentBottom);

            if (transform != null) {
                applyTransformation(destination, transform);
            }
        }

        private void applyTransformation(final Bitmap destination, final Matrix transform) {
            Bitmap src = Bitmap.createBitmap(destination.getWidth(), destination.getHeight(), destination.getConfig());
            Canvas canvasResult = new Canvas(destination);
            canvasResult.drawBitmap(src, transform, null);
        }

        private void drawPage(final Bitmap bitmap, final int pageNumber, final int destLeft, final int destTop, final int destWidth, final int destHeight) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            core.drawPage(bitmap, pageNumber, bitmapWidth, bitmapHeight, destLeft, destTop, destWidth, destHeight, cookie);
        }
    }

}
