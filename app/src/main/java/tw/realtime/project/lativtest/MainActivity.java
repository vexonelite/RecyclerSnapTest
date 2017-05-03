package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyViewPagerAdapter mViewPagerAdapter;
    private int mCurrentPage;

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiInitialization();
    }

    private void uiInitialization () {

        Integer[] idArray = {
                R.id.button1,
                R.id.button2,
                R.id.button3
        };
        for (Integer viewId : idArray) {
            View view = findViewById(viewId);
            if (null != view) {
                view.setOnClickListener(new MyViewsClickCallback(viewId));
            }
        }


        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter = adapter;

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.addOnPageChangeListener(new MyPagerPageChangeHandler());
    }

    private class MyViewsClickCallback implements View.OnClickListener {
        private int mViewId;

        MyViewsClickCallback (int viewId) {
            mViewId = viewId;
        }

        @Override
        public void onClick(View view) {
            view.setEnabled(false);
            switch (mViewId) {
                case R.id.button1: {
                    scrollToSpecifiedPosition(2);
                    break;
                }
                case R.id.button2: {
                    scrollToSpecifiedPosition(4);
                    break;
                }
                case R.id.button3: {
                    scrollToSpecifiedPosition(6);
                    break;
                }
            }
            view.setEnabled(true);
        }
    }

    private class MyViewPagerAdapter extends BaseViewPagerAdapter<Integer> {

        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
            ArrayList<Integer> itemSet = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                itemSet.add(i + 1);
            }
            setItemSet(itemSet);
        }

        @Override
        public Fragment getItem(int position) {
            RecyclerviewFragment fragment = new RecyclerviewFragment();
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
            mCurrentPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_IDLE");
            }
            else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_DRAGGING");
            }
            else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                LogWrapper.showLog(Log.INFO, getLogTag(), "onPageScrollStateChanged - SCROLL_STATE_SETTLING");
            }
        }
    }

    private void scrollToSpecifiedPosition (int position) {
        Fragment fragment = mViewPagerAdapter.getRegisteredFragment(mCurrentPage);
        if ( (null != fragment) && (fragment instanceof RecyclerviewFragment) ) {
            ((RecyclerviewFragment) fragment).scrollToSpecifiedPosition(position);
        }
    }
}
