package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import R.helper.BaseActivity;
import R.helper.BaseFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.customviews.JobSearchBox;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.jobapp.activities.searchresult.SearchResultActivity;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;
import vietnamworks.com.jobapp.models.SearchHistoryModel;

/**
 * Created by duynk on 2/22/16.
 *
 */
public class SearchFragment extends BaseFragment {
    @Bind(R.id.listview_recent_search)
    ListView listViewRecentSearch;

    @Bind(R.id.job_search_box)
    JobSearchBox jobSearchBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        jobSearchBox.setDelegate(new JobSearchBox.JobSearchBoxDelegate() {
            @Override
            public void onSearch(final SearchHistoryEntity entity) {
                BaseActivity.timeout(new Runnable() {
                    @Override
                    public void run() {
                        loadRecentSearch();
                        Bundle b = new Bundle();
                        String data = null;
                        try {
                            data = entity.exportToJson().toString();
                        } catch (Exception E) {}
                        b.putString("data", data);
                        BaseActivity.pushActivity(SearchResultActivity.class, b);
                    }
                });
            }
        });
        loadRecentSearch();
        return rootView;
    }

    public void onLayoutChanged(boolean isSoftKeyShown) {
        if (!isSoftKeyShown) {
            MainActivity act = getActivityRef(MainActivity.class);
            MainActivity.showActionBar();
            act.showTabs();
        } else {
            MainActivity act = getActivityRef(MainActivity.class);
            MainActivity.hideActionBar();
            act.hideTabs();
        }
    }


    private void loadRecentSearch() {
        SearchHistoryModel.load();
        listViewRecentSearch.setAdapter(new RecentSearchAdapter(getContext()));
    }
    public static class RecentSearchAdapter extends BaseAdapter {
        Context context;
        public RecentSearchAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return SearchHistoryModel.size();
        }

        @Override
        public Object getItem(int position) {
            return SearchHistoryModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (view == null) {
                view = layoutInflater.inflate(R.layout.cv_search_history_item_list, parent, false);
                TextView tv = (TextView)view.findViewById(R.id.textView1);
                tv.setText(SearchHistoryModel.get(position).getSummary());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    String data = null;
                    try {
                        SearchHistoryEntity entity = (SearchHistoryEntity)getItem(position);
                        data = entity.exportToJson().toString();
                    } catch (Exception E) {}
                    b.putString("data", data);
                    BaseActivity.pushActivity(SearchResultActivity.class, b);
                }
            });
            return view;
        }

    }

}
