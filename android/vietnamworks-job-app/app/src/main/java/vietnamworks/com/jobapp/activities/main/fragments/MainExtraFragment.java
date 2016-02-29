package vietnamworks.com.jobapp.activities.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import R.helper.BaseFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;

/**
 * Created by duynk on 2/29/16.
 */
public class MainExtraFragment extends BaseFragment {

    @Bind(R.id.extra_view_profile)
    View viewProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_extra, container, false);
        ButterKnife.bind(this, rootView);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}