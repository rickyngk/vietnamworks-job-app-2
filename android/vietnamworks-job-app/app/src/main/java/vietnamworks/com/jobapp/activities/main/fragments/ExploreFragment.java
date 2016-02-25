package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    @Bind(R.id.recycler_explore)
    RecyclerView recyclerExploredJobs;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        loadData();

        swipeLayout.setRefreshing(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        recyclerExploredJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    private void loadData() {
        ExploredJobModel.load(getContext(), new Callback() {
            @Override
            public void onCompleted(Context context, CallbackResult result) {
                if (context instanceof MainActivity) {
                    MainActivity act = (MainActivity)context;
                    if (!act.isFinishing()) {
                        swipeLayout.setRefreshing(false);
                        recyclerExploredJobs.setAdapter(new ExploredJobsAdapter(context));
                    }
                }
            }
        });
    }

    public static class ExploredJobsAdapter extends RecyclerView.Adapter<ExploredJobsAdapter.ViewHolder> {
        Context context;
        public ExploredJobsAdapter(Context context) {
            this.context = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public View view;
            public ViewHolder(View v) {
                super(v);
                view = v;
            }
        }

        @Override
        public ExploredJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_explored_item_view, parent, false);
            v.findViewById(R.id.view_item_holder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            View view = holder.view;
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
                    for (String l:locations_array) {
                        l = l.trim();
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


        @Override
        public int getItemCount() {
            return ExploredJobModel.size();
        }
    }
}
