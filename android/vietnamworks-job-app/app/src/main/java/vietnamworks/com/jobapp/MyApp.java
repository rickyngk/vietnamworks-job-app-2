package vietnamworks.com.jobapp;

import R.helper.BaseApplication;
import vietnamworks.com.vnwcore.VNWAPI;

/**
 * Created by duynk on 2/22/16.
 */
public class MyApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultFont(FontType.SERIF, "fonts/RobotoSlab-Regular.ttf");
        VNWAPI.init("2ed19d9c84fa9280fe6fa1a9e58de807a9d076646de8327c53fc8ed64ca4e268", "", false);
    }
}
