package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import R.helper.BaseFragment;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.jobapp.models.UserJobModel;
import vietnamworks.com.vnwcore.entities.AppliedJob;
import vietnamworks.com.vnwcore.entities.Configuration;
import vietnamworks.com.vnwcore.entities.Location;

/**
 * Created by duynk on 2/22/16.
 *
 */
public class UserJobsFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, rootView);

        loadData();

        swipeLayout.setRefreshing(true);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    private void loadData() {
        UserJobModel.load(getContext(), new Callback() {
            @Override
            public void onCompleted(Context context, CallbackResult result) {
                if (context instanceof MainActivity) {
                    MainActivity act = (MainActivity) context;
                    if (!act.isFinishing()) {
                        swipeLayout.setRefreshing(false);
                        recyclerView.setAdapter(new CustomRecyclerViewAdapter(context));
                    }
                }
            }
        });
    }

    public static class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
        Context context;
        public CustomRecyclerViewAdapter(Context context) {
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
        public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_user_applied_job_item_view, parent, false);
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
            AppliedJob r = UserJobModel.get(position);
            if (r != null) {
                ((TextView)view.findViewById(R.id.txt_job_title)).setText(r.getJobTitle());
                ((TextView)view.findViewById(R.id.txt_company)).setText(r.getJobCompany());
                String locations = r.getJobLocations();
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
            return UserJobModel.size();
        }
    }
}
