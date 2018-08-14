package com.filippoints.util;

import android.content.Context;
import android.widget.Toast;

import com.filippoints.R;

/**
 * Created by hlib on 7/2/18.
 */

public class Util {
    public static void displayCheckConnectionToast(Context context) {
        Toast.makeText(context, R.string.check_connection, Toast.LENGTH_LONG).show();
    }
}
