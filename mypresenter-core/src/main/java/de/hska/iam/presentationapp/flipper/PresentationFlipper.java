/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       PresentationFlipper.java
 *  @package	de.hska.iam.presentationapp.flipper
 *  @brief      The Class PresentationFlipper flips the views during
 *  			the presentation with an animation.
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

package de.hska.iam.presentationapp.flipper;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;
import android.widget.ViewFlipper;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;
import de.hska.iam.presentationapp.playlist.Playlist;
import de.hska.iam.presentationapp.playlist.PlaylistManager;
import de.hska.iam.presentationapp.services.cache.BitmapCacheManagerServiceConnection;
import de.hska.iam.presentationapp.services.playlist.PlaylistManagerServiceConnection;

public class PresentationFlipper extends ViewFlipper implements PlaylistManagerServiceConnection.PlaylistManagerServiceConnectionListener,
        BitmapCacheManagerServiceConnection.BitmapCacheManagerServiceConnectionListener {

    private boolean open;
    private boolean paused;
    private Context context;
    private VideoView videoView;
    private ViewManager viewManager;
    private int callbackCount;

    public PresentationFlipper(final Context context) {
        super(context);
        init(context);
    }

    public PresentationFlipper(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(final Context context) {
        this.context = context;
        ImageView imageView = new ImageView(this.context);
        videoView = new VideoView(this.context);
        viewManager = new ViewManager(imageView, videoView);
        addView(imageView);
        addView(videoView);

        videoView.setMediaController(new MediaController(this.context));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                stopFlipping();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mp) {
                if (paused) {
                    mp.seekTo(0);
                } else {
                    showNext();
                    startFlipping();
                }
            }
        });
    }

    @Override
    public void onServiceConnected(final PlaylistManager playlistManager) {
        Playlist playlist = playlistManager.get(0);
        viewManager.setPlaylist(playlist);
        callbackCount++;
        startWhenReady();
    }

    @Override
    public void onServiceConnected(final BitmapCacheManager bitmapCacheManager) {
        viewManager.setBitmapCacheManager(bitmapCacheManager);
        callbackCount++;
        startWhenReady();
    }

    private void startWhenReady() {
        if (callbackCount == 2) {
            showNext();
            if (!paused) {
                start();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ViewAnimator#showNext()
     */
    @Override
    public void showNext() {
        View nextView = viewManager.getNextView();
        setNextAnimation(nextView);
        displayView(nextView);
        viewManager.preloadNextMedia();
    }

    private void setNextAnimation(final View nextView) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_left));
        adjustOutAnimationForVideoView(nextView);
    }

    private void displayView(final View view) {
        if (view.equals(videoView)) {
            playVideo();
        }
        setDisplayedChild(indexOfChild(view));
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ViewAnimator#showPrevious()
     */
    @Override
    public void showPrevious() {
        View previousView = viewManager.getPreviousView();
        setPreviousAnimation(previousView);
        displayView(previousView);
        viewManager.preloadPreviousMedia();
    }

    private void setPreviousAnimation(final View previousView) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_left));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right));
        adjustOutAnimationForVideoView(previousView);
    }

    private void adjustOutAnimationForVideoView(final View successorView) {
        View currentView = getCurrentView();
        if (currentView.equals(videoView) || successorView.equals(videoView)) {
            setOutAnimation(null);
        }
    }

    public void resetFlipInterval() {
        if (isFlipping()) {
            Animation inAnimation = getInAnimation();
            Animation outAnimation = getOutAnimation();
            setInAnimation(null);
            setOutAnimation(null);
            stopFlipping();
            startFlipping();
            setInAnimation(inAnimation);
            setOutAnimation(outAnimation);
        }
    }

    private void playVideo() {
        videoView.start();
    }

    public void start() {
        paused = false;
        View currentView = getCurrentView();
        if (currentView.equals(videoView)) {
            playVideo();
        } else {
            startFlipping();
        }
    }

    public void stop() {
        paused = true;
        stopFlipping();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    public boolean isOpen() {
        return open;
    }

    public void setOpen(final boolean open) {
        this.open = open;
    }


}
