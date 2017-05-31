package tw.realtime.project.lativtest;

import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tw.realtime.project.baseframework.utils.CodeUtils;
import tw.realtime.project.baseframework.widgets.LogWrapper;

public class TabHolderActivity extends AppCompatActivity {

    private static final int TAB_COUNT = 4;
    private MyViewPagerAdapter mViewPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int mCurrentPage;


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_holder);

        uiInitialization();
    }

    private void uiInitialization () {

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getFragmentManager());
        mViewPagerAdapter = adapter;

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        if (null != mTabLayout) {
            setupTabLayout();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(new MyPagerPageChangeHandler());
        mViewPager = viewPager;
    }

    private class MyViewPagerAdapter extends BaseFragmentPagerAdapter<Integer> {

        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            ArrayList<Integer> itemSet = new ArrayList<>();
            for (int i = 0; i < TAB_COUNT; i++) {
                itemSet.add(i + 1);
            }
            setItemSet(itemSet);
        }

        @Override
        public Fragment getItem(int position) {
            HolderFragment fragment = new HolderFragment();
            fragment.setPosition(position + 1);
            fragment.setBackgroundColor(getColorByPosition(position));
            return fragment;
        }

        private int getColorByPosition (int position) {
            int resId = R.color.gray_0004;
            switch (position) {
                case 0:
                    resId = R.color.pink;
                    break;
                case 1:
                    resId = R.color.blue_001;
                    break;
                case 2:
                    resId = R.color.orange_0002;
                    break;
                case 3:
                    resId = R.color.green_0003;
                    break;
                case 4:
                    resId = R.color.yellow_0001;
                    break;
            }
            return CodeUtils.getColorFromResourceId(getApplicationContext(), resId);
        }
    }

    private class MyPagerPageChangeHandler implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            LogWrapper.showLog(Log.INFO, getLogTag(), "onPageSelected - position: " + position);

            if ((position < 0) || (position >= TAB_COUNT)) {
                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - position is invalid!");
                return;
            }
            mCurrentPage = position;

            if (null != mViewPager) {
                //mViewPager.setCurrentItem(position);
                mViewPager.setCurrentItem(position, true);
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - mViewPager is null!");
            }

            if (null != mTabLayout) {
                TabLayout.Tab tabView = mTabLayout.getTabAt(position);
                if (null != tabView) {
                    tabView.select();
                }
                else {
                    LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - tabView in "
                            + position + " is null!");
                }
            }
            else {
                LogWrapper.showLog(Log.WARN, getLogTag(), "onPageSelected - TabLayout is null!");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /*
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_IDLE");
            }
            else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_DRAGGING");
            }
            else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_SETTLING");
            }
            */
        }
    }

    private void setupTabLayout () {

        if (mTabLayout.getTabCount() > 0) {
            mTabLayout.removeAllTabs();
        }
        /*
         * set up the 'tabMode' - This sets the mode to use for the TabLayout.
         * This can either be fixed (all tabs are shown concurrently) or
         * scrollable (show a subset of tabs that can be scrolled through).
         */
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        /*
         * set up the 'tabGravity' - This sets the Gravity of the tabs,
         * which can be either 'fill'
         * (distribute all available space between individual tabs)
         * or 'centre' (position tabs in the center of the TabLayout).
         */
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < TAB_COUNT; i++) {
            //View tabView = inflater.inflate(R.layout.activity_tab_item, null);
            View tabView = inflater.inflate(R.layout.activity_tab_item, mTabLayout, false);
            TabLayout.Tab tabCell = mTabLayout.newTab();
            tabCell.setCustomView(tabView);
            mTabLayout.addTab(tabCell);

            String text = "Tab " + i;
            TextView labelView = (TextView) tabView.findViewById(R.id.label);
            labelView.setText(text);
        }

        if (Build.VERSION.SDK_INT < 24) {
            mTabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener());
        }
        else {
            mTabLayout.addOnTabSelectedListener(new MyOnTabSelectedListener());
            //mTabLayout.removeOnTabSelectedListener(new MyOnTabSelectedListener());
        }
    }

    private class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabSelected");
            int position = tab.getPosition();
            LogWrapper.showLog(Log.INFO, getLogTag(), "onTabSelected - position: " + position);
            if (mViewPager.getCurrentItem() != position) {
                mViewPager.setCurrentItem(position, true);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabUnselected");
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //LogWrapper.showLog(Log.INFO, getLogTag(), "onTabReselected");
        }
    }


    @Override
    public void onBackPressed() {
        Log.w(getLogTag(), "onBackPressed!");

        Fragment fragment = mViewPagerAdapter.getRegisteredFragment(mCurrentPage);
        if ( (null != fragment) && (fragment instanceof HolderFragment) ) {
            FragmentManager childFragManager = fragment.getChildFragmentManager();
            if (childFragManager.getBackStackEntryCount() > 0) {
                childFragManager.popBackStackImmediate();
            }
            else {
                finish();
            }
        }
        else {
            finish();
        }

//        if (fragManager.getBackStackEntryCount() <= 0) {
//            finish();
//        }
//        else {
//
//        }

    }
}
