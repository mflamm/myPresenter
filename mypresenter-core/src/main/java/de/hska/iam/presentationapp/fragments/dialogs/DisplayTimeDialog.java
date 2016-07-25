/********************************************************************//**
 *
 *  @MyPresenter     Presentation-App for Android-Devices
 *
 *  @copyright  2014 IMP - Institute of Materials and Processes
 *                   University of Applied Sciences
 *                   Karlsruhe
 *
 *  @file       DisplayTimeDialog.java
 *  @package	de.hska.iam.presentationapp.fragments.dialogs
 *  @brief      Dialog to change the presentation-time of the
 *  			presentation mode.
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import de.hska.iam.presentationapp.R;
import de.hska.iam.presentationapp.preferences.DisplayTimeDialogPreferences;

public class DisplayTimeDialog extends DialogFragment {

    private NumberPicker secondsPicker;
    private NumberPicker minutesPicker;
    private DisplayTimeDialogPreferences preferences;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Activity activity = getActivity();
        preferences = new DisplayTimeDialogPreferences(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.time_picker_dialog, null);
        secondsPicker = (NumberPicker) dialogView.findViewById(R.id.secondsTimePicker);
        minutesPicker = (NumberPicker) dialogView.findViewById(R.id.minutesTimePicker);
        setupSecondsPicker();
        setupMinutesPicker();
        return createAlertDialog(dialogView);
    }

    private void setupSecondsPicker() {
        secondsPicker.setWrapSelectorWheel(true);
        setSecondsPickerRange();
        loadSecondsPickerValue();
    }

    private void loadSecondsPickerValue() {
        int seconds = preferences.getSeconds();
        secondsPicker.setValue(seconds);
    }

    private void setSecondsPickerRange() {
        secondsPicker.setMinValue(1);
        secondsPicker.setMaxValue(59);
    }

    private void setupMinutesPicker() {
        minutesPicker.setWrapSelectorWheel(true);
        setMinutesPickerRange();
        loadMinutesPickerValue();
    }

    private void loadMinutesPickerValue() {
        int minutes = preferences.getMinutes();
        minutesPicker.setValue(minutes);
    }

    private void setMinutesPickerRange() {
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
    }

    private AlertDialog createAlertDialog(final View dialogView) {
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialogDisplayTimeTitle)
                .setView(dialogView)
                .setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        int seconds = secondsPicker.getValue();
                        preferences.saveSeconds(seconds);
                        int minutes = minutesPicker.getValue();
                        preferences.saveMinutes(minutes);
                    }
                })
                .setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int id) {
                    }
                });
        return builder.create();
    }

}
