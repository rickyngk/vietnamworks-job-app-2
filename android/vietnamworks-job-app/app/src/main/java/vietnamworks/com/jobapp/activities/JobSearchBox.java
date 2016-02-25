package vietnamworks.com.jobapp.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import R.helper.BaseActivity;
import R.helper.Callback;
import R.helper.CallbackResult;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.entities.SearchHistoryEntity;
import vietnamworks.com.jobapp.models.SearchHistoryModel;
import vietnamworks.com.vnwcore.VNWAPI;

/**
 * Created by duynk on 2/25/16.
 *
 */
public class JobSearchBox extends FrameLayout {
    int UNLIMITED_SALARY;
    public static interface JobSearchBoxDelegate {
        void onSearch(SearchHistoryEntity entity);
    }


    @Bind(R.id.btn_advance_search) ImageButton btnAdvanceSearch;
    @Bind(R.id.btn_search) ImageButton btnSearch;
    @Bind(R.id.select_industry) Spinner selectIndustry;
    @Bind(R.id.select_working_place) Spinner selectWorkingPlace;
    @Bind(R.id.select_min_salary) Spinner selectMinSalary;
    @Bind(R.id.select_position) Spinner selectPosition;
    @Bind(R.id.view_search_extra) View searchExtraView;
    @Bind(R.id.input_job_title) AutoCompleteTextView inputJobTitle;
    @Bind(R.id.btn_clear_search_input) ImageButton btnClearSearch;
    private JobSearchBoxDelegate delegate;


    public JobSearchBox(Context context) {
        super(context);
        initializeViews(context);
    }

    public JobSearchBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public JobSearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cv_job_search_box, this);
        ButterKnife.bind(this, this);

        importSelect(selectIndustry, R.array.array_industry);
        importSelect(selectWorkingPlace, R.array.array_locations);
        importSelect(selectMinSalary, R.array.array_min_salary);
        importSelect(selectPosition, R.array.array_job_level);

        searchExtraView.setVisibility(View.GONE);

        btnClearSearch.setVisibility(inputJobTitle.getText().length() > 0?View.VISIBLE:View.GONE);
        btnClearSearch.setOnClickListener(new View.OnClickListener() {
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.hideKeyboard();
                String jobTitle = inputJobTitle.getText().toString().trim();
                if (jobTitle.length() > 0) {
                    SearchHistoryEntity entity = new SearchHistoryEntity();
                    entity.setJobTitle(jobTitle);
                    entity.setCategory(getResources().getStringArray(R.array.array_industry_code)[selectIndustry.getSelectedItemPosition()]);
                    entity.setLocation(getResources().getStringArray(R.array.array_location_code)[selectWorkingPlace.getSelectedItemPosition()]);
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

                    if (delegate != null) {
                        delegate.onSearch(entity);
                        timer.cancel();
                    }
                } else {
                    BaseActivity.toast(R.string.validate_job_title_is_require);
                }
            }
        });


        UNLIMITED_SALARY = getResources().getInteger(R.integer.unlimited_salary);
        setupJobTitleSearchBox();
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

    private Drawable getDrawableResource(int resId) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
        {
            return getContext().getDrawable(resId);
        }
        else
        {
            return getResources().getDrawable(resId);
        }
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
                            VNWAPI.jobTitleSuggestion(getContext(), inputJobTitle.getText().toString(), new Callback<ArrayList<String>>() {
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

    public void setDelegate(JobSearchBoxDelegate delegate) {
        this.delegate = delegate;
    }

    private boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    private boolean isEmpty(Integer s) {
        return s == null || s == UNLIMITED_SALARY;
    }

    public void importData(SearchHistoryEntity entity) {
        inputJobTitle.setText(entity.getJobTitle());
        btnClearSearch.setVisibility(inputJobTitle.getText().length() > 0 ? View.VISIBLE : View.GONE);

        searchExtraView.setVisibility(GONE);
        resetExtraForm();
        if (!isEmpty(entity.getLevel()) || !isEmpty(entity.getLocation()) || !isEmpty(entity.getMinSalary()) || !isEmpty(entity.getCategory())) {
            searchExtraView.setVisibility(VISIBLE);

            if (!isEmpty(entity.getLevel())) {
                String [] arr = getResources().getStringArray(R.array.array_job_level_value);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equalsIgnoreCase(entity.getLevel())) {
                        selectPosition.setSelection(i);
                    }
                }
            }

            if (!isEmpty(entity.getLocation())) {
                String [] arr = getResources().getStringArray(R.array.array_location_code);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equalsIgnoreCase(entity.getLocation())) {
                        selectWorkingPlace.setSelection(i);
                    }
                }
            }

            if (!isEmpty(entity.getMinSalary())) {
                int[] arr = getResources().getIntArray(R.array.array_min_salary_value);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] == entity.getMinSalary()) {
                        selectMinSalary.setSelection(i);
                    }
                }
            }

            if (!isEmpty(entity.getCategory())) {
                String [] arr = getResources().getStringArray(R.array.array_industry_code);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equalsIgnoreCase(entity.getCategory())) {
                        selectIndustry.setSelection(i);
                    }
                }
            }
        }
    }
}
