package com.google.android.mms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class APNHelper {

    public APNHelper(final Context context) {
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public List<APN> getMMSApns() {
        try {
            final Cursor apnCursor = this.context.getContentResolver().query(Uri.withAppendedPath(Uri.parse("content://telephony/carriers"), "current"), null, null, null, null);
            if (apnCursor == null) {
                return Collections.EMPTY_LIST;
            } else {
                final List<APN> results = new ArrayList<APN>();
                if (apnCursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") final String type = apnCursor.getString(apnCursor.getColumnIndex("type"));
                        if (!TextUtils.isEmpty(type) && (type.equalsIgnoreCase("*") || type.equalsIgnoreCase("mms"))) {
                            @SuppressLint("Range") final String mmsc = apnCursor.getString(apnCursor.getColumnIndex("mmsc"));
                            @SuppressLint("Range") final String mmsProxy = apnCursor.getString(apnCursor.getColumnIndex("mmsproxy"));
                            @SuppressLint("Range") final String port = apnCursor.getString(apnCursor.getColumnIndex("mmsport"));
                            final APN apn = new APN();
                            apn.MMSCenterUrl = mmsc;
                            apn.MMSProxy = mmsProxy;
                            apn.MMSPort = port;
                            results.add(apn);

                            Toast.makeText(context, mmsc + " " + mmsProxy + " " + port, Toast.LENGTH_LONG).show();
                        }
                    } while (apnCursor.moveToNext());
                }
                apnCursor.close();
                return results;
            }
        } catch (Exception e) {
            // insignificant permissions
            return Collections.EMPTY_LIST;
        }
    }

    private Context context;
}
