/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ViewManager.java
 *  @package	de.hska.de.presentationapp.flipper
 *  @brief      Manages the views of the presentation flipper.
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

package de.hska.iam.presentationapp.flipper;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.playlist.Playlist;
import de.hska.iam.presentationapp.util.UriUtils;

class ViewManager {

    private final ImageView imageView;
    private final VideoView videoView;
    private PlaylistIterator playlistIterator;
    private BitmapCacheManager cache;

    ViewManager(final ImageView imageView, final VideoView videoView) {
        this.imageView = imageView;
        this.videoView = videoView;
    }

    public void setPlaylist(final Playlist playlist) {
        playlistIterator = new PlaylistIterator(playlist);
    }

    public void setBitmapCacheManager(final BitmapCacheManager bitmapCacheManager) {
        cache = bitmapCacheManager;
    }

    public View getNextView() {
        Media media = playlistIterator.next();
        setViewContent(media);
        return getView(media);
    }

    public void preloadNextMedia() {
        Media media = playlistIterator.peekNext();
        createBitmap(media);
    }

    public View getPreviousView() {
        Media media = playlistIterator.previous();
        setViewContent(media);
        return getView(media);
    }

    public void preloadPreviousMedia() {
        Media media = playlistIterator.peekPrevious();
        createBitmap(media);
    }

    private void createBitmap(final Media media) {
        if (!cache.containsFullscreenImage(media)) {
            cache.saveFullscreenImage(media, null);
        }
    }

    private View getView(final Media media) {
        View view;
        String absolutePath = media.getAbsolutePath();
        Uri uri = UriUtils.fromFilePath(absolutePath);
        if (UriUtils.isVideo(uri)) {
            view = videoView;
        } else {
            view = imageView;
        }
        return view;
    }

    private void setViewContent(final Media media) {
        String absolutePath = media.getAbsolutePath();
        Uri uri = UriUtils.fromFilePath(absolutePath);
        if (UriUtils.isVideo(uri)) {
            videoView.setVideoPath(absolutePath);
        } else {
            if (cache.containsFullscreenImage(media)) {
                cache.loadFullscreenImage(media, imageView);
            } else {
                cache.saveFullscreenImage(media, imageView);
            }
        }
    }

}
