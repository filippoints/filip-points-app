package com.filippoints.util;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.filippoints.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hlib on 7/2/18.
 */

public class Util {
    public static void offlinePointsWillBeAssignedLaterSnackbar(View view) {
        Snackbar.make(view, R.string.offline_points_assigned_later, Toast.LENGTH_SHORT).show();
    }


    public static void noConnectionSnackbar(View view) {
        Snackbar.make(view, R.string.check_connection, Toast.LENGTH_SHORT).show();
    }

    public static Set<String> intListToStrSet(List<Integer> ints) {
        HashSet<String> strings = new HashSet<>();
        for (Integer intt: ints) {
            strings.add(intt.toString());
        }
        return strings;
    }

    public static List<Integer> stringSetToIntList(Set<String> strings) {
        ArrayList<Integer> integers = new ArrayList<>();
        for (String str: strings) {
            integers.add(Integer.parseInt(str));
        }
        return integers;
    }
}
