package vietnamworks.com.jobapp.activities.main;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import R.helper.BaseActivity;
import R.helper.BaseFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import vietnamworks.com.jobapp.R;
import vietnamworks.com.jobapp.activities.main.fragments.ExploreFragment;
import vietnamworks.com.jobapp.activities.main.fragments.SearchFragment;
import vietnamworks.com.jobapp.activities.main.fragments.UserJobsFragment;

public class MainActivity extends BaseActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.container) ViewPager mViewPager;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void hideTabs() {
        tabLayout.setVisibility(View.GONE);
    }

    public void showTabs() {
        tabLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSearchFragment() {
        BaseFragment tmp = mSectionsPagerAdapter.getFragment(0);
        if (tmp != null) {
            SearchFragment f = (SearchFragment)tmp;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[] sections;
        Context ctx;
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        private int[] imageResId = {
                R.drawable.ic_tab_search,
                R.drawable.ic_tab_explore,
                R.drawable.ic_tab_my_job
        };
        public SectionsPagerAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            this.ctx = ctx;
            sections = getResources().getStringArray(R.array.sections);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            switch (position) {
                case 0:
                    f = BaseFragment.newInstance(SearchFragment.class);
                    break;
                case 1:
                    f = BaseFragment.newInstance(ExploreFragment.class);
                    break;
                case 2:
                    f = BaseFragment.newInstance(UserJobsFragment.class);
                    break;
                case 3:
                    f = BaseFragment.newInstance(SearchFragment.class);
                    break;
                default:
                    break;
            }
            registeredFragments.append(position, f);
            return f;
        }

        @Override
        public int getCount() {
            return sections.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(ctx, imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth()*3/4, image.getIntrinsicHeight()*3/4);
            // Replace blank spaces with image icon
            SpannableString sb = new SpannableString(" \n" + sections[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        public BaseFragment getFragment(int position) {
            return (BaseFragment)registeredFragments.get(position);
        }
    }

    public void onLayoutChanged(Rect r, boolean isSoftKeyShown, boolean lastState) {
        if (isSoftKeyShown != lastState) {
            BaseFragment tmp = mSectionsPagerAdapter.getFragment(mViewPager.getCurrentItem());
            if (tmp != null) {
                if (tmp instanceof SearchFragment) {
                    SearchFragment f = (SearchFragment) tmp;
                    f.onLayoutChanged(isSoftKeyShown);
                }
            }
        }
    }
}
