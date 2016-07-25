/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2015 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      RandomStringGenerator.java
 *  @package   de.hska.iam.presentationapp.util
 *  @brief     Helper class to generate random strings.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 15.02.2015 Markus Maier
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

package de.hska.iam.presentationapp.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class RandomStringGenerator {

    private static final int RADIX = 32;
    private static final int NUM_BITS = 130;

    private RandomStringGenerator() {
    }

    public static String generateString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(NUM_BITS, random).toString(RADIX);
    }

}
