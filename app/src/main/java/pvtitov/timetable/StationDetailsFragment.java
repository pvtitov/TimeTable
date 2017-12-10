package pvtitov.timetable;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Павел on 10.12.2017.
 */

public class StationDetailsFragment extends DialogFragment {

    String mStationDetails;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mStationDetails);
        return builder.create();
    }


    void setDetails(String stationDetails) {mStationDetails = stationDetails;}
}
