package vietnamworks.com.jobapp.activities.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import R.helper.BaseFragment;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.MainActivity;

/**
 * Created by duynk on 2/22/16.
 */
public class UserJobsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Your Jobs");

        Button btn = (Button) rootView.findViewById(R.id.searchButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityRef(MainActivity.class).openSearchFragment();
            }
        });
        return rootView;
    }
}
