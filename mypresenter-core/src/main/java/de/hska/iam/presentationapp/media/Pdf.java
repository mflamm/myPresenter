/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       Pdf.java
 *  @package	de.hska.iam.presentationapp.media
 *  @brief      A media file representing a pdf.
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
import de.hska.iam.presentationapp.util.PdfRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pdf extends Media implements Iterable<PdfPage> {

    private final PdfRenderer renderer;
    private final List<PdfPage> pdfPages;

    Pdf(final Context context, final String absolutePath) {
        super(context, absolutePath);
        renderer = new PdfRenderer(context, absolutePath);
        pdfPages = new ArrayList<>();
        for (int pageNumber = 0; pageNumber <  renderer.getPageCount(); pageNumber++) {
            PdfRenderer.Page page = renderer.openPage(pageNumber);
            PdfPage pdfPage = new PdfPage(context, absolutePath, page);
            pdfPages.add(pdfPage);
        }
    }

    public PdfPage getPage(final int pageNumber) {
        return pdfPages.get(pageNumber);
    }

    public int getPageCount() {
        return renderer.getPageCount();
    }

    @Override
    public Bitmap createThumbnail() {
        PdfPage pdfPage = getPage(0);
        return PdfBitmapCreator.createThumbnail(pdfPage);
    }

    /**
     * Returns an {@link java.util.Iterator} for the elements in this object.
     *
     * @return An {@code Iterator} instance.
     */
    @Override
    public Iterator<PdfPage> iterator() {
        return new PdfIterator(this);
    }

}
