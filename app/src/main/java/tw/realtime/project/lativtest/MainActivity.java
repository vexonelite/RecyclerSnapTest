package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiInitialization();
    }

    private void uiInitialization () {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager() ) );
        viewPager.setOffscreenPageLimit(adapter.getCount());
        //viewPager.setCurrentItem(0);
        //viewPager.addOnPageChangeListener(new MyPagerPageChangeHandler());
    }

    private class MyViewPagerAdapter extends BaseViewPagerAdapter<Integer> {

        public MyViewPagerAdapter(FragmentManager fm) {
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
            fragment.setPosition(position);
            return fragment;
        }
    }
}
