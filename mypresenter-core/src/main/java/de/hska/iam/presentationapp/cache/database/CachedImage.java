/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      CachedImage.java
 *  @package   de.hska.iam.presentationapp.cache.database
 *  @brief     Value object of a cached image entry in a
 *             database.
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

package de.hska.iam.presentationapp.cache.database;

public class CachedImage {

    public enum ImageType {
        THUMBNAIL,
        FULLSCREEN_IMAGE
    }

    private final Long id;
    private final ImageType imageType;
    private final String mediaFilePath;
    private String imageFilePath;
    private final int pdfPageNumber;

    CachedImage(final Long id, final ImageType imageType, final String mediaFilePath,
                final String imageFilePath, final int pdfPageNumber) {
        this.id = id;
        this.imageType = imageType;
        this.mediaFilePath = mediaFilePath;
        this.imageFilePath = imageFilePath;
        this.pdfPageNumber = pdfPageNumber;
    }

    public Long getId() {
        return id;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getMediaFilePath() {
        return mediaFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(final String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public int getPdfPageNumber() {
        return pdfPageNumber;
    }

}
