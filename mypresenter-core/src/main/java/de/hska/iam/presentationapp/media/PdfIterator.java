/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PdfIterator.java
 *  @package	de.hska.iam.presentationapp.media.pdf
 *  @brief      An iterator to iterate over a pdf. Returns pdf pages.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 29.05.2015 Markus Maier
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

package de.hska.iam.presentationapp.media;

import java.util.Iterator;
import java.util.NoSuchElementException;

class PdfIterator implements Iterator<PdfPage> {

    private final int pageCount;
    private final Pdf pdf;
    private int pageNumber;

    PdfIterator(final Pdf pdf) {
        this.pdf = pdf;
        pageCount = pdf.getPageCount();
    }

    /**
     * Returns true if there is at least one more element, false otherwise.
     *
     * @see #next
     */
    @Override
    public boolean hasNext() {
        return pageNumber < pageCount;
    }

    /**
     * Returns the next object and advances the iterator.
     *
     * @return the next object.
     * @throws java.util.NoSuchElementException if there are no more elements.
     * @see #hasNext
     */
    @Override
    public PdfPage next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        PdfPage pdfPage = pdf.getPage(pageNumber);
        pageNumber++;
        return pdfPage;
    }

    /**
     * Removes the last object returned by {@code next} from the collection.
     * This method can only be called once between each call to {@code next}.
     *
     * @throws UnsupportedOperationException
     *             if removing is not supported by the collection being
     *             iterated.
     * @throws IllegalStateException
     *             if {@code next} has not been called, or {@code remove} has
     *             already been called after the last call to {@code next}.
     */
    @Override
    public void remove() {
    }

}
