package vietnamworks.com.jobapp.activities.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import R.helper.BaseFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;

/**
 * Created by duynk on 2/22/16.
 */
public class SearchFragment extends BaseFragment {

    @Bind(R.id.btn_advance_search)
    ImageButton btnAdvanceSearch;

    @Bind(R.id.btn_search)
    ImageButton btnSearch;

    @Bind(R.id.select_industry)
    Spinner selectIndustry;

    @Bind(R.id.select_working_place)
    Spinner selectWorkingPlace;

    @Bind(R.id.select_min_salary)
    Spinner selectMinSalary;

    @Bind(R.id.select_position)
    Spinner selectPosition;

    @Bind(R.id.view_search_extra)
    View searchExtraView;

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
}
