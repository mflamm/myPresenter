/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       ChooseFormatDialog.java
 *  @package	de.hska.iam.presentationapp.fragments.dialogs
 *  @brief      Dialog to deactivate or activate the shown datatype.
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

package de.hska.iam.presentationapp.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.preferences.MediaFormatFiltersPreferences;

public class ChooseFormatDialog extends DialogFragment {

    private OnMediaFormatFiltersSelectedListener onMediaFormatFiltersSelectedListener;
    private MediaFormatFiltersPreferences preferences;

    public interface OnMediaFormatFiltersSelectedListener {
        void onMediaFormatFiltersSelected();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        try {
            onMediaFormatFiltersSelectedListener = (OnMediaFormatFiltersSelectedListener) activity;
        } catch (final ClassCastException ignored) {
            throw new ClassCastException(activity
                    + " must implement OnMediaFormatFiltersSelectedListener");
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new MediaFormatFiltersPreferences(getActivity());
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final boolean[] checkedItems = preferences.getMediaFormatFilters();
        CharSequence[] items = {"Bilder", "Videos", "PDF"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogChooseFormatTitle).setMultiChoiceItems(items, checkedItems,
               new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which, final boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int id) {
                        preferences.saveMediaFormatFilters(checkedItems);
                        onMediaFormatFiltersSelectedListener.onMediaFormatFiltersSelected();
                    }
                })
                .setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

}
