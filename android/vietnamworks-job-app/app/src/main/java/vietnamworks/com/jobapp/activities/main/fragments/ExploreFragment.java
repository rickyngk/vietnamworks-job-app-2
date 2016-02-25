package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import R.helper.BaseFragment;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.jobapp.models.ExploredJobModel;
import vietnamworks.com.vnwcore.entities.Configuration;
import vietnamworks.com.vnwcore.entities.JobSearchResult;
import vietnamworks.com.vnwcore.entities.Location;

/**
 * Created by duynk on 2/22/16.
 */
public class ExploreFragment extends BaseFragment {

    @Bind(R.id.listview_explore)
    ListView listviewExploredJobs;

    @Bind(R.id.loading_view)
    View loadingView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        loadingView.setVisibility(View.VISIBLE);
        loadData();

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        return rootView;
    }

    private void loadData() {
        ExploredJobModel.load(getContext(), new Callback() {
            @Override
            public void onCompleted(Context context, CallbackResult result) {
                if (context instanceof MainActivity) {
                    MainActivity act = (MainActivity)context;
                    if (!act.isFinishing()) {
                        loadingView.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);
                        listviewExploredJobs.setAdapter(new ExploredJobsAdapter(context));
                    }
                }
            }
        });
    }

    public static class ExploredJobsAdapter extends BaseAdapter {
        Context context;
        public ExploredJobsAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return ExploredJobModel.size();
        }

        @Override
        public Object getItem(int position) {
            return ExploredJobModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cv_explored_item_view, parent, false);
                JobSearchResult r = ExploredJobModel.get(position);
                if (r != null) {
                    ((TextView)view.findViewById(R.id.txt_job_title)).setText(r.getTitle());
                    ((TextView)view.findViewById(R.id.txt_company)).setText(r.getCompany());

                    String logo = r.getLogoURL();
                    if (logo != null && !logo.isEmpty()) {
                        Picasso.with(context).load(logo).into(((ImageView) view.findViewById(R.id.img_company)));
                    }
                    String locations = r.getLocations();
                    StringBuilder locationDetail = new StringBuilder();
                    String delim = "";
                    if (locations != null && !locations.isEmpty()) {
                        String[] locations_array = locations.split(",");
                        for (int i = 0; i < locations_array.length; i++) {
                            String l = locations_array[i].trim();
                            Location loc = Configuration.findLocation(l);
                            if (loc != null) {
                                locationDetail.append(delim);
                                locationDetail.append(loc.getEn());
                                delim = ", ";
                            }
                        }
                    }
                    ((TextView)view.findViewById(R.id.txt_location)).setText(locationDetail.toString());
                }
            }
            return view;
        }
    }
}
