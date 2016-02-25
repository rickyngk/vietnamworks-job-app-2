package vietnamworks.com.jobapp.models;

import android.content.Context;

import java.util.ArrayList;

import R.helper.Callback;
import R.helper.CallbackResult;
import vietnamworks.com.vnwcore.VNWAPI;
import vietnamworks.com.vnwcore.entities.AppliedJob;

/**
 * Created by duynk on 2/25/16.
 */
public class UserJobModel {
    static ArrayList<AppliedJob> data;
    public static void load(Context ctx, final Callback callback) {
        VNWAPI.getAppliedJobs(ctx, new Callback<ArrayList<AppliedJob>>() {
            @Override
            public void onCompleted(Context context, CallbackResult<ArrayList<AppliedJob>> result) {
                data = result.getData();
                callback.onCompleted(context, null);
            }
        });
    }
    public static int size() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    public static boolean hasData() {
        return data != null;
    }

    public static AppliedJob get(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }
}
