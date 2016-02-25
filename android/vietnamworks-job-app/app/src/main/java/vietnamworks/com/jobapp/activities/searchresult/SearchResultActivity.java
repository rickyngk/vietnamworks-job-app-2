package vietnamworks.com.jobapp.activities.searchresult;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import R.helper.BaseActivity;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.customviews.JobSearchBox;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;
import vietnamworks.com.jobapp.models.ExploredJobModel;
import vietnamworks.com.vnwcore.entities.Configuration;
import vietnamworks.com.vnwcore.entities.JobSearchResult;
import vietnamworks.com.vnwcore.entities.Location;

public class SearchResultActivity extends BaseActivity {
    @Bind(R.id.recycler_job_search)
    RecyclerView recyclerJobSearch;

    SearchHistoryEntity searchEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String data = getIntent().getExtras().getString("data");
        setContentView(R.layout.activity_search_result);

        ButterKnife.bind(this);

        try {
            if (data != null && !data.isEmpty()) {
                JSONObject obj = new JSONObject(data);
                searchEntity = new SearchHistoryEntity();
                searchEntity.importFromJson(obj);
            }
        }catch (Exception E) {}

        ActionBar b = getSupportActionBar();
        if (b != null) {
            b.setDisplayHomeAsUpEnabled(true);
        }

        loadData();
        recyclerJobSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        ExploredJobModel.load(this, new Callback() {
            @Override
            public void onCompleted(Context context, CallbackResult result) {
                if (context instanceof SearchResultActivity) {
                    SearchResultActivity act = (SearchResultActivity) context;
                    if (!act.isFinishing()) {
                        recyclerJobSearch.setAdapter(new JobSearchAdapter(context));
                    }
                }
            }
        });
    }

    public static class JobSearchAdapter extends RecyclerView.Adapter<JobSearchAdapter.ViewHolder> {
        Context context;
        public JobSearchAdapter(Context context) {
            this.context = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public ViewHolder(View v) {
                super(v);
                view = v;
            }
        }

        @Override
        public JobSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_explored_item_view, parent, false);
                v.findViewById(R.id.view_item_holder).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                return new ViewHolder(v);
            } else {
                return new ViewHolder(new JobSearchBox(context));
            }
        }

        public int getItemViewType(int position) {
            return position > 0?1:0;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            View view = holder.view;
            if (!(view instanceof JobSearchBox)) {
                JobSearchResult r = ExploredJobModel.get(position - 1);
                if (r != null) {
                    ((TextView) view.findViewById(R.id.txt_job_title)).setText(r.getTitle());
                    ((TextView) view.findViewById(R.id.txt_company)).setText(r.getCompany());

                    String logo = r.getLogoURL();
                    if (logo != null && !logo.isEmpty()) {
                        Picasso.with(context).load(logo).into(((ImageView) view.findViewById(R.id.img_company)));
                    }
                    String locations = r.getLocations();
                    StringBuilder locationDetail = new StringBuilder();
                    String delim = "";
                    if (locations != null && !locations.isEmpty()) {
                        String[] locations_array = locations.split(",");
                        for (String l : locations_array) {
                            l = l.trim();
                            Location loc = Configuration.findLocation(l);
                            if (loc != null) {
                                locationDetail.append(delim);
                                locationDetail.append(loc.getEn());
                                delim = ", ";
                            }
                        }
                    }
                    ((TextView) view.findViewById(R.id.txt_location)).setText(locationDetail.toString());
                }
            } else {
                JobSearchBox b = (JobSearchBox)view;
                b.importData(((SearchResultActivity)context).searchEntity);
            }
        }


        @Override
        public int getItemCount() {
            return ExploredJobModel.size() + 1;
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
