/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      CachedImages.java
 *  @package   de.hska.iam.presentationapp.cache.database
 *  @brief     Wrapper for a collection of {@link CachedImage}.
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

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CachedImages {

    private final Map<Long, CachedImage> cachedImages;

    CachedImages() {
        cachedImages = new ConcurrentHashMap<>();
    }

    public CachedImage get(final long id) {
        return cachedImages.get(id);
    }

    public void put(final CachedImage cachedImage) {
        long id = cachedImage.getId();
        cachedImages.put(id, cachedImage);
    }

    public void remove(final CachedImage cachedImage) {
        long id = cachedImage.getId();
        cachedImages.remove(id);
    }

    public Collection<CachedImage> values() {
        return cachedImages.values();
    }

}
