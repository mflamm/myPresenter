/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PresentationActivity.java
 *  @package	de.hska.iam.presentationapp.activities
 *  @brief      The Class PresentationActivity is responsible for
 *  			displaying a Presentation.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 02.08.2014 Benjamin Roth
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

package de.hska.iam.presentationapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.flipper.PresentationFlipper;
import de.hska.iam.presentationapp.preferences.DisplayTimeDialogPreferences;
import de.hska.iam.presentationapp.services.ServiceConnector;


public class PresentationActivity extends Activity implements GestureDetector.OnGestureListener {

    private PresentationFlipper flipper;
    private GestureDetectorCompat gestureDetector;
    private ServiceConnector serviceConnector;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_viewer);

        gestureDetector = new GestureDetectorCompat(this, this);

        DisplayTimeDialogPreferences preferences = new DisplayTimeDialogPreferences(this);
        int flipIntervalImages = preferences.getSeconds();

        flipper = (PresentationFlipper) findViewById(R.id.flipper);
        flipper.setFlipInterval(flipIntervalImages * 1000);
        flipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        boolean paused = getIntent().getBooleanExtra("isPaused", false);
        flipper.setPaused(paused);


        serviceConnector = new ServiceConnector(this);
        serviceConnector.connect(ServiceConnector.Services.BITMAP_CACHE_MANAGER, flipper);
        serviceConnector.connect(ServiceConnector.Services.PLAYLIST_MANAGER, flipper);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceConnector.disconnect(ServiceConnector.Services.BITMAP_CACHE_MANAGER);
        serviceConnector.disconnect(ServiceConnector.Services.PLAYLIST_MANAGER);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
     */
    @Override
    public boolean onDown(final MotionEvent e) {
        flipper.resetFlipInterval();
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            final float SWIPE_THRESHOLD = 100;
            final float SWIPE_VELOCITY_THRESHOLD = 100;
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                flipper.resetFlipInterval();
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                return true;
            }
        }
        return false;
    }

    private void onSwipeLeft() {
        flipper.showNext();
    }

    private void onSwipeRight() {
        flipper.showPrevious();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
     */
    @Override
    public void onLongPress(final MotionEvent e) {
        if (flipper.isPaused()) {
            // animation aus
            flipper.setInAnimation(null);
            flipper.setOutAnimation(null);

            flipper.start();
            Toast.makeText(getApplicationContext(), "START", Toast.LENGTH_SHORT).show();
        } else {
            flipper.stop();
            Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
     */
    @Override
    public void onShowPress(final MotionEvent e) {

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
     */
    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        return false;
    }



}
