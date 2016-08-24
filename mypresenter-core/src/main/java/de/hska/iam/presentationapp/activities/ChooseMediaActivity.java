/********************************************************************//**
 *
 *  @MyPresenter Presentation-App for Android-Devices
 *
 *  @copyright 2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file      ChooseMediaActivity.java
 *  @package   de.hska.iam.presentationapp.activities
 *  @brief     The root activity. Responsible for choosing
 *             the media-files for a presentation.
 *
 *
 ********************************************************************
 *
 *  @lastmodified 02.08.2014 Benjamin Roth
 *  @lastmodified 29.05.2015 Markus Maier
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

package de.hska.iam.presentationapp.activities;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.fragments.FolderListFragment;
import de.hska.iam.presentationapp.fragments.GridViewFragment;
import de.hska.iam.presentationapp.fragments.dialogs.ChooseFormatDialog;
import de.hska.iam.presentationapp.fragments.dialogs.DisplayTimeDialog;
import de.hska.iam.presentationapp.media.Media;
import de.hska.iam.presentationapp.util.UriUtils;

public class ChooseMediaActivity extends FragmentActivity implements FolderListFragment.OnFolderSelectedListener,
        ChooseFormatDialog.OnMediaFormatFiltersSelectedListener, GridViewFragment.OnStartPresentationPausedListener {

    private FolderListFragment folderListFragment;
    private GridViewFragment gridViewFragment;
    private Uri uri;


    /*
     *(non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridViewFragment = (GridViewFragment) Fragment.instantiate(this, GridViewFragment.class.getName(), null);
        folderListFragment = (FolderListFragment) Fragment.instantiate(this, FolderListFragment.class.getName(), null);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.gridView_fragment_container, gridViewFragment);
        fragmentTransaction.add(R.id.folder_list_fragment_container, folderListFragment);
        fragmentTransaction.commit();

        //button = (Button) findViewById(R.)
      // button = (Button) findViewById(R.id.Choss_Data_button);
//
    }
//


    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*
         * (non-Javadoc)
         *
         * @see android.support.v4.app.FragmentActivity#onOptionsItemSelected(android.view.MenuItem)
         */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chooseFormat:
                    ChooseFormatDialog chooseFormatDialog = new ChooseFormatDialog();
                    chooseFormatDialog.show(getSupportFragmentManager(), "dialog");
                return true;
            case R.id.setTime:
                DisplayTimeDialog displayTimeDialog = new DisplayTimeDialog();
                displayTimeDialog.show(getSupportFragmentManager(), "dialog");
                return true;
            case R.id.action_start_set:
                    gridViewFragment.addPlaylistToPlaylistManager();
                    startPresentationPaused(false);
                return true;
            case R.id.Open_Data:
                openData(dataPath().toString());
                return true;
            case R.id.Share_Menu:
                shareDate(dataPath().toString());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Uri dataPath() {
        Media selecteItem = gridViewFragment.getSelectedItem();
        this.uri = Uri.parse("file://" + selecteItem.getAbsolutePath());
        return this.uri;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.hska.iam.presentationapp.fragments.FolderListFragment..OnFolderSelectedListener#onFolderSelected(java.lang.String)
     */

    @Override
    public void onFolderSelected(final String folderPath) {
        gridViewFragment.updateThumbnailsFromFolder(folderPath);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.hska.iam.presentationapp.fragments.dialogs.ChooseFormatDialog.OnMediaFormatFiltersSelectedListener#onMediaFormatFiltersSelected()
     */

    @Override
    public void onMediaFormatFiltersSelected() {
        gridViewFragment.refreshThumbnailsWithChangedFilters();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.hska.iam.presentationapp.fragments.GridViewFragment..OnStartPresentationPausedListener#startPresentationPaused(boolean)
     */

    @Override
    public void startPresentationPaused(final boolean paused) {
        Intent intent = new Intent(this, PresentationActivity.class);
        intent.putExtra("isPaused", paused);
        startActivity(intent);

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */

    @Override
    public void onBackPressed() {
        if (folderListFragment.isRootDirectory()) {
            super.onBackPressed();
        } else {
            folderListFragment.backPressed();
        }
    }

    public void shareDate(String share) {

        Intent shareDataIntent = new Intent(Intent.ACTION_SEND);
        shareDataIntent.putExtra(Intent.EXTRA_STREAM, uri);

        if (UriUtils.isImage(uri)) {

            shareDataIntent.setType("image/*");
            startActivity(Intent.createChooser(shareDataIntent, "Teilen mit "));

        } else if (UriUtils.isPdf(uri)) {

            shareDataIntent.setType("pdf/*");
            startActivity(Intent.createChooser(shareDataIntent, "Teilen mit "));

        } else if (UriUtils.isVideo(uri)) {

            shareDataIntent.setType("video/*");
            startActivity(Intent.createChooser(shareDataIntent, "Teilen mit "));

        }
    }

    public void openData(String open){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> app = packageManager.queryIntentActivities(intent, 0);

        boolean isAppsAvailable = app.size() > 0;

        if (isAppsAvailable) {

            if (UriUtils.isPdf(uri)) {

                intent.setDataAndType(uri, "application/pdf");
                startActivity(Intent.createChooser(intent, "Starten mit "));

            } else if (UriUtils.isImage(uri)) {

                intent.setDataAndType(uri, "image/*");
                startActivity(Intent.createChooser(intent, "Starten mit "));

            } else if (UriUtils.isVideo(uri)) {

                intent.setDataAndType(uri,"video/*");
                startActivity(Intent.createChooser(intent, "Starten mit "));

            }
        }
    }
}



