/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       GridViewFragment.java
 *  @package	de.hska.iam.presentation.fragments
 *  @brief      The Class GridViewFragment provides a GridView of the
 *  			folder-content.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 02.08.2014 Benjamin Roth
 *  @lastmodified 31.05.2015 Markus Maier
 *  @lastmodified 05.09.2015 Issa Al-Kamaly
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

package de.hska.iam.presentationapp.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.cache.BitmapCacheManager;
import de.hska.iam.presentationapp.fragments.adapters.ThumbnailAdapter;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.playlist.Playlist;
import de.hska.iam.presentationapp.playlist.PlaylistManager;
import de.hska.iam.presentationapp.preferences.MediaFormatFiltersPreferences;
import de.hska.iam.presentationapp.queries.ConfigurableFileQuery;
import de.hska.iam.presentationapp.services.ServiceConnector;
import de.hska.iam.presentationapp.services.cache.BitmapCacheManagerServiceConnection;
import de.hska.iam.presentationapp.services.playlist.PlaylistManagerServiceConnection;



/**
 * The Class GridViewFragment provides a GridView of the folder-content.
 */
public class GridViewFragment extends Fragment implements
    LoaderManager.LoaderCallbacks<Cursor>, BitmapCacheManagerServiceConnection.BitmapCacheManagerServiceConnectionListener,
        PlaylistManagerServiceConnection.PlaylistManagerServiceConnectionListener {


    private static final int THUMBNAIL_LOADER = 0;
    private GridView gridView;
    private ThumbnailAdapter thumbnailAdapter;
    private OnStartPresentationPausedListener onStartPresentationPausedListener;
    private MediaFormatFiltersPreferences preferences;
    private String folderPath;
    private PlaylistHelper playlistHelper;
    private ServiceConnector serviceConnector;


    public interface OnStartPresentationPausedListener {
        void startPresentationPaused(boolean paused);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            onStartPresentationPausedListener = (OnStartPresentationPausedListener) activity;
        } catch (final ClassCastException ignored) {
            throw new ClassCastException(activity
                    + " must implement OnStartPresentationPausedListener");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_view, container, false);

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.gridview);
        setOnItemClickListener();
        preferences = new MediaFormatFiltersPreferences(getActivity());
        thumbnailAdapter = new ThumbnailAdapter(getActivity(), R.layout.grid_view_entry, null, 0);
        playlistHelper = new PlaylistHelper(thumbnailAdapter);
        serviceConnector = new ServiceConnector(getActivity());
        serviceConnector.connect(ServiceConnector.Services.BITMAP_CACHE_MANAGER, this);
        serviceConnector.connect(ServiceConnector.Services.PLAYLIST_MANAGER, this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        serviceConnector.disconnect(ServiceConnector.Services.BITMAP_CACHE_MANAGER);
        serviceConnector.disconnect(ServiceConnector.Services.PLAYLIST_MANAGER);
    }

    @Override
    public void onServiceConnected(final BitmapCacheManager bitmapCacheManager) {
        thumbnailAdapter.setBitmapCacheManager(bitmapCacheManager);
        gridView.setAdapter(thumbnailAdapter);
    }

    @Override
    public void onServiceConnected(final PlaylistManager playlistManager) {
        playlistHelper.setPlaylistManager(playlistManager);
    }

    private void setOnItemClickListener() {
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

                if (thumbnailAdapter.isItemPositionSelected(position)) {
                    thumbnailAdapter.removeSelectedItemPosition(position);
                    ThumbnailAdapter.deselectView(view);
                } else {
                    thumbnailAdapter.addSelectedItemPosition(position);
                    ThumbnailAdapter.selectView(view);
                }

            }
        });


    }
    public void addPlaylistToPlaylistManager() {
        Playlist playlist = new Playlist(getActivity(), "The one and only playlist");
        playlistHelper.addMediaFiles(playlist);
        playlistHelper.addPlaylistToPlaylistManager(playlist);
    }

    public void updateThumbnailsFromFolder(final String folderPath) {
        this.folderPath = folderPath;
        getActivity().getSupportLoaderManager().restartLoader(THUMBNAIL_LOADER, null, this);
    }

    public void refreshThumbnailsWithChangedFilters() {
        getActivity().getSupportLoaderManager().restartLoader(THUMBNAIL_LOADER, null, this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
     */
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case THUMBNAIL_LOADER:
                ConfigurableFileQuery query = new ConfigurableFileQuery(getActivity());
                boolean showImages = preferences.getMediaFormatFilters()[0];
                boolean showVideos = preferences.getMediaFormatFilters()[1];
                boolean showPDFs = preferences.getMediaFormatFilters()[2];
                query.filterImages(showImages);
                query.filterVideos(showVideos);
                query.filterPdfs(showPDFs);
                query.limitSearchToFolders(new String[]{folderPath});
                return query.createCursorLoader();
            default:
                return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(android.support.v4.content.Loader, android.database.Cursor)
     */
    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        if (thumbnailAdapter != null && data != null) {
            thumbnailAdapter.clearSelectedItems();
            thumbnailAdapter.changeCursor(data);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.LoaderManager.onLoaderReset#onCreateLoader(android.support.v4.content.Loader)
     */
    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        if (thumbnailAdapter != null) {
            thumbnailAdapter.clearSelectedItems();
            thumbnailAdapter.changeCursor(null);
        }
    }

    // TODO handle if nothing is selected
    public Media getSelectedItem() {
        if(this.thumbnailAdapter.iterator().hasNext()){
            return this.thumbnailAdapter.iterator().next();
        }
        else{
            return null;
        }
    }
}
