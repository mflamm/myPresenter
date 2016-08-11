/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       FolderListFragment.java
 *  @package	de.hska.iam.presentationapp.fragments
 *  @brief      Shows a list of folders which the user can
 *              navigate through.
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

package de.hska.iam.presentationapp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.hska.iam.presentationapp.R;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public class FolderListFragment extends ListFragment {

    private OnFolderSelectedListener onFolderSelectedListener;
    private FolderHierarchy folderHierarchy;
    private ArrayAdapter<String> adapter;
    private View view;

    public interface OnFolderSelectedListener {
        /**
         * Callback for the activity.
         *
         * @param folderPath The path of the selected folder
         */
        void onFolderSelected(final String folderPath);
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
            onFolderSelectedListener = (OnFolderSelectedListener) activity;
        } catch (final ClassCastException ignored) {
            throw new ClassCastException(activity
                    + " must implement OnFolderSelectedListener");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        view = inflater.inflate(R.layout.folder_list, container, false);
        File externalPath = Environment.getExternalStorageDirectory();
        String path = externalPath + File.separator + "Pictures";
        folderHierarchy = new FolderHierarchy(path);
        folderChanged();

        return view;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(final ListView listView, final View view, final int position, final long id) {
        String folderName = (String) listView.getAdapter().getItem(position);
        folderHierarchy.cd(folderName);
        folderChanged();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     * Current path will be adjusted.
     */
    public void backPressed() {
        folderHierarchy.up();
        folderChanged();
    }

    /**
     * Updates the fragment and notifies the
     * activity when the folder was changed.
     */
    private void folderChanged() {
        updateFolderList();
        notifyAboutChangedFolder();
    }

    private void updateFolderList() {
        adapter.clear();
        List<String> folders = folderHierarchy.dir();
        adapter.addAll(folders);
    }

    private void notifyAboutChangedFolder() {
        String currentPath = folderHierarchy.getCurrentPath();
        onFolderSelectedListener.onFolderSelected(currentPath);
    }

    public boolean isRootDirectory() {
        return folderHierarchy.isRootDirectory();
    }

}
