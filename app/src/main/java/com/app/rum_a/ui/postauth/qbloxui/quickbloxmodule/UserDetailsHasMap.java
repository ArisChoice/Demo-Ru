package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.util.SparseArray;

import com.quickblox.users.model.QBUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunny on 4/5/2017.
 */

public class UserDetailsHasMap {


    public static UserDetailsHasMap userImagesHasMap;
    public SparseArray<QBUser> userDetailMap = new SparseArray<>();
    public Map<Integer, Integer> userImage = new HashMap<>();
    public Map<Integer, String> userName = new HashMap<>();

    public static UserDetailsHasMap getInstance() {
        if (userImagesHasMap == null) {
            userImagesHasMap = new UserDetailsHasMap();
        }
        return userImagesHasMap;

    }

}
