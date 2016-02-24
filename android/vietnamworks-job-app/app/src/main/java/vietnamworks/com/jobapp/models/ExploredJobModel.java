package vietnamworks.com.jobapp.models;

import android.content.Context;

import java.util.ArrayList;

import R.helper.Callback;
import R.helper.CallbackResult;
import vietnamworks.com.vnwcore.VNWAPI;
import vietnamworks.com.vnwcore.entities.JobSearchResult;

/**
 * Created by duynk on 2/24/16.
 */
public class ExploredJobModel {
    static ArrayList<JobSearchResult> data;
    public static void load(Context ctx, final Callback callback) {
        VNWAPI.searchJob(ctx, 0, 20, "", null, null, new Callback<ArrayList<JobSearchResult>>() {
            @Override
            public void onCompleted(Context context, CallbackResult<ArrayList<JobSearchResult>> result) {
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

    public static JobSearchResult get(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }
}
