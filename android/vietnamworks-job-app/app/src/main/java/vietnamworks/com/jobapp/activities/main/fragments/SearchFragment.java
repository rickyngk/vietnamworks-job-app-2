package vietnamworks.com.jobapp.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import R.helper.BaseActivity;
import R.helper.BaseFragment;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.MainActivity;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;
import vietnamworks.com.jobapp.models.SearchHistoryModel;
import vietnamworks.com.vnwcore.VNWAPI;

/**
 * Created by duynk on 2/22/16.
 */
public class SearchFragment extends BaseFragment {

    @Bind(R.id.btn_advance_search) ImageButton btnAdvanceSearch;
    @Bind(R.id.btn_search) ImageButton btnSearch;
    @Bind(R.id.select_industry) Spinner selectIndustry;
    @Bind(R.id.select_working_place) Spinner selectWorkingPlace;
    @Bind(R.id.select_min_salary) Spinner selectMinSalary;
    @Bind(R.id.select_position) Spinner selectPosition;
    @Bind(R.id.view_search_extra) View searchExtraView;
    @Bind(R.id.input_job_title) AutoCompleteTextView inputJobTitle;
    @Bind(R.id.listview_recent_search) ListView listViewRecentSearch;
    @Bind(R.id.btn_clear_search_input) ImageButton btnClearSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        importSelect(selectIndustry, R.array.array_industry);
        importSelect(selectWorkingPlace, R.array.array_locations);
        importSelect(selectMinSalary, R.array.array_min_salary);
        importSelect(selectPosition, R.array.array_job_level);

        searchExtraView.setVisibility(View.GONE);
        searchExtraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputJobTitle.setText("");
            }
        });

        btnAdvanceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchExtraView.getVisibility() == View.GONE) {
                    btnAdvanceSearch.setImageDrawable(getDrawableResource(R.drawable.ic_action_less_arrow_circular_symbol));
                    searchExtraView.setVisibility(View.VISIBLE);
                } else {
                    btnAdvanceSearch.setImageDrawable(getDrawableResource(R.drawable.ic_action_more_arrow_circular_symbol));
                    searchExtraView.setVisibility(View.GONE);
                    resetExtraForm();
                }
            }
        });

        btnClearSearch.setVisibility(View.GONE);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.hideKeyboard();
                String jobTitle = inputJobTitle.getText().toString().trim();
                if (jobTitle.length() > 0) {
                    SearchHistoryEntity entity = new SearchHistoryEntity();
                    entity.setJobTitle(jobTitle);
                    entity.setCategory(getResources().getStringArray(R.array.array_industry_code)[selectIndustry.getSelectedItemPosition()]);
                    entity.setLocation(getResources().getStringArray(R.array.location_code)[selectWorkingPlace.getSelectedItemPosition()]);
                    entity.setMinSalary(getResources().getIntArray(R.array.array_min_salary_value)[selectMinSalary.getSelectedItemPosition()]);
                    entity.setLevel(getResources().getStringArray(R.array.array_min_salary_value)[selectPosition.getSelectedItemPosition()]);

                    StringBuilder summary = new StringBuilder(entity.getJobTitle());
                    if (selectIndustry.getSelectedItemPosition() > 0) {
                        summary.append(", ");
                        summary.append(selectIndustry.getSelectedItem().toString());
                    }
                    if (selectWorkingPlace.getSelectedItemPosition() > 0) {
                        summary.append(", ");
                        summary.append(selectWorkingPlace.getSelectedItem().toString());
                    }
                    if (selectMinSalary.getSelectedItemPosition() > 0) {
                        summary.append(", ");
                        summary.append(selectMinSalary.getSelectedItem().toString());
                    }
                    if (selectPosition.getSelectedItemPosition() > 0) {
                        summary.append(", ");
                        summary.append(selectPosition.getSelectedItem().toString());
                    }
                    entity.setSummary(summary.toString());

                    SearchHistoryModel.add(entity);
                    SearchHistoryModel.save();

                    BaseActivity.timeout(new Runnable() {
                        @Override
                        public void run() {
                            loadRecentSearch();
                        }
                    });
                } else {
                    BaseActivity.toast(R.string.validate_job_title_is_require);
                }
            }
        });

        loadRecentSearch();
        setupJobTitleSearchBox();

        return rootView;
    }

    private void resetExtraForm() {
        selectIndustry.setSelection(0);
        selectWorkingPlace.setSelection(0);
        selectMinSalary.setSelection(0);
        selectPosition.setSelection(0);
    }

    private void importSelect(Spinner target, int resId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), resId, R.layout.cv_dropdown_simple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        target.setAdapter(adapter);
    }


    ArrayAdapter<String> jobTitleAdapter;
    boolean preventTextChangedEvent = false;
    private Timer timer = new Timer();
    void setupJobTitleSearchBox() {
        jobTitleAdapter = new ArrayAdapter<>(this.getContext(), R.layout.cv_autocomplete_input_item_view);
        inputJobTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == getResources().getInteger(R.integer.ime_job_title) || actionId == EditorInfo.IME_NULL) && event == null) {
                    BaseActivity.hideKeyboard();
                    jobTitleAdapter.clear();
                    jobTitleAdapter.getFilter().filter(inputJobTitle.getText(), null);
                    inputJobTitle.clearFocus();
                    btnSearch.callOnClick();
                    return true;
                }
                return false;
            }
        });
        inputJobTitle.setAdapter(jobTitleAdapter);
        inputJobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                btnClearSearch.setVisibility(s.length() > 0?View.VISIBLE:View.GONE);
                if (s.length() >= 3) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (preventTextChangedEvent) {
                                preventTextChangedEvent = false;
                                return;
                            }
                            VNWAPI.jobTitleSuggestion(SearchFragment.this.getContext(), inputJobTitle.getText().toString(), new Callback<ArrayList<String>>() {
                                @Override
                                public void onCompleted(Context context, CallbackResult result) {
                                    jobTitleAdapter.clear();
                                    if (!result.hasError()) {
                                        try {
                                            Object re = result.getData();
                                            ArrayList<?> data = (ArrayList<?>) re;
                                            for (int i = 0; i < data.size(); i++) {
                                                jobTitleAdapter.add((String) data.get(i));
                                            }
                                            jobTitleAdapter.getFilter().filter(inputJobTitle.getText(), null);
                                        } catch (Exception E) {
                                            E.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }

                    }, 1000);
                }
            }
        });
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

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.cv_search_history_item_list, null);
                TextView tv = (TextView)convertView.findViewById(R.id.textView1);
                tv.setText(SearchHistoryModel.get(position).getSummary());
            }
            return convertView;
        }

    }

}
