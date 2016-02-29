package vietnamworks.com.jobapp.activities.userjobs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import R.helper.BaseActivity;
import R.helper.BaseFragment;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.fragments.UserJobsFragment;

public class JobsActivity extends BaseActivity {
    public interface SECTIONS {
        final static int SAVED_JOBS = 0;
        final static int RECENT_VIEWED_JOBS = 1;
        final static int APPLIED_JOBS = 2;
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ActionBar b = getSupportActionBar();
        if (b != null) {
            b.setDisplayHomeAsUpEnabled(true);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == SECTIONS.APPLIED_JOBS) {
                    ((UserJobsFragment) mSectionsPagerAdapter.getFragment(position)).loadData();
                }
                setTitle(getResources().getStringArray(R.array.array_job_section_title)[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setTitle(getResources().getStringArray(R.array.array_job_section_title)[0]);
        setTitleBarColor(R.color.colorPrimaryDark);

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position) {
                case SECTIONS.RECENT_VIEWED_JOBS:
                    f = PlaceholderFragment.newInstance(position + 1);
                    break;
                case SECTIONS.SAVED_JOBS:
                    f = PlaceholderFragment.newInstance(position + 1);
                    break;
                case SECTIONS.APPLIED_JOBS:
                    f = BaseFragment.newInstance(UserJobsFragment.class);
                    break;
                default:
                    break;
            }
            registeredFragments.append(position, f);
            return f;
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.array_job_section_title).length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.array_job_section_title)[position];
        }

        public BaseFragment getFragment(int position) {
            return (BaseFragment)registeredFragments.get(position);
        }
    }
}
