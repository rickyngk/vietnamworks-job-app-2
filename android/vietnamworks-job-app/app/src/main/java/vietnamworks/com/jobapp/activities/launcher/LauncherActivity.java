package vietnamworks.com.jobapp.activities.launcher;

import android.content.Context;
import android.os.Bundle;

import R.helper.Callback;
import R.helper.CallbackResult;
import R.helper.Common;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.vnwcore.Auth;
import vietnamworks.com.vnwcore.entities.Configuration;

/**
 * Created by duynk on 2/25/16.
 */
public class LauncherActivity extends vietnamworks.com.vnwcore.LauncherActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth.login(this, "rickylol@yopmail.com", "1234", new Callback<Object>() {
            @Override
            public void onCompleted(Context context, CallbackResult<Object> result) {

            }
        });

        Configuration.load(LauncherActivity.this, new Callback<Configuration>() {
            @Override
            public void onCompleted(Context context, CallbackResult<Configuration> result) {
                if (result.hasError()) {
                    //TODO: handle error here
                    Common.Dialog.alert(context, result.getError().getMessage(), new Callback() {
                        @Override
                        public void onCompleted(Context context, CallbackResult result) {
                            LauncherActivity.this.finish();
                        }
                    });
                } else {
                    openActivity(MainActivity.class);
                }
            }
        });
    }
}
