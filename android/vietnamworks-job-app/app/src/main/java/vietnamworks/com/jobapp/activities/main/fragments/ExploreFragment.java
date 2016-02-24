package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import R.helper.BaseFragment;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.jobapp.models.ExploredJobModel;

/**
 * Created by duynk on 2/22/16.
 */
public class ExploreFragment extends BaseFragment {

    @Bind(R.id.listview_explore)
    ListView listviewExploredJobs;

    @Bind(R.id.loading_view)
    View loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, rootView);

        loadingView.setVisibility(View.VISIBLE);
        ExploredJobModel.load(getContext(), new Callback() {
            @Override
            public void onCompleted(Context context, CallbackResult result) {
                if (context instanceof MainActivity) {
                    MainActivity act = (MainActivity)context;
                    if (!act.isFinishing()) {
                        loadingView.setVisibility(View.GONE);
                        listviewExploredJobs.setAdapter(new ExploredJobsAdapter(context));
                    }
                }
            }
        });
        return rootView;
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

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.cv_search_history_item_list, parent, false);
                TextView tv = (TextView)convertView.findViewById(R.id.textView1);
                tv.setText(ExploredJobModel.get(position).getCompany());
            }
            return convertView;
        }
    }
}
