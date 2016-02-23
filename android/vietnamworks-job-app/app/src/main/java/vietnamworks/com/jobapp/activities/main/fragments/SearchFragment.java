package vietnamworks.com.jobapp.activities.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import R.helper.BaseFragment;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;

/**
 * Created by duynk on 2/22/16.
 */
public class SearchFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
