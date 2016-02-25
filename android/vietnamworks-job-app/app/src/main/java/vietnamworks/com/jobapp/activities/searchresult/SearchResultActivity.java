package vietnamworks.com.jobapp.activities.searchresult;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import org.json.JSONObject;

import R.helper.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.JobSearchBox;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;

public class SearchResultActivity extends BaseActivity {
    @Bind(R.id.job_search_box)
    JobSearchBox jobSearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getExtras().getString("data");
        setContentView(R.layout.activity_search_result);

        ButterKnife.bind(this);

        try {
            if (data != null && !data.isEmpty()) {
                JSONObject obj = new JSONObject(data);
                SearchHistoryEntity entity = new SearchHistoryEntity();
                entity.importFromJson(obj);

                jobSearchBox.importData(entity);
            }
        }catch (Exception E) {}

        ActionBar b = getSupportActionBar();
        if (b != null) {
            b.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }

    public void onLayoutChanged(Rect r, boolean isSoftKeyShown, boolean lastState) {
        if (isSoftKeyShown != lastState) {
            if (!isSoftKeyShown) {
                SearchResultActivity.showActionBar();
            } else {
                SearchResultActivity.hideActionBar();
            }
        }
    }
}
