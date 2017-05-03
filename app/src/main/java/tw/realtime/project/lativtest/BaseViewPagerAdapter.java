package tw.realtime.project.lativtest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseViewPagerAdapter<T> extends FragmentStatePagerAdapter {

    private final byte[] mLock = new byte[0];

    private List<T> mItemSet;

    protected SparseArrayCompat<Fragment> mFragmentHolder;


    protected String getLogTag () {
        return BaseViewPagerAdapter.class.getSimpleName();
    }

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mItemSet = new ArrayList<>();
        mFragmentHolder = new SparseArrayCompat<>();
    }

    public void addItemSet (List<T> itemSet) {
        if ( (null != itemSet) && (!itemSet.isEmpty()) ) {
            synchronized (mLock) {
                mItemSet.addAll(itemSet);
            }
        }
    }

    public void setItemSet (List<T> itemSet) {
        if ( (null != itemSet) && (!itemSet.isEmpty()) ) {
            synchronized (mLock) {
                mItemSet = itemSet;
            }
        }
    }

    public T getItemSetElement (int position) {
        if ( (position >= 0) && (position < mItemSet.size()) ) {
            return mItemSet.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        if (null == mItemSet) {
            return 0;
        }
        return mItemSet.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Object holder = super.instantiateItem(container, position);
        if (holder instanceof Fragment) {
            Fragment fragment = (Fragment)holder;
            synchronized(mLock) {
                mFragmentHolder.put(position, fragment);
            }
            LogWrapper.showLog(Log.INFO, getLogTag(), "instantiateItem - position: " + position
            		+ ", tag: " + fragment.getClass().getSimpleName() );
        }
        return holder;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        synchronized(mLock) {
            mFragmentHolder.remove(position);
            LogWrapper.showLog(Log.INFO, getLogTag(), "destroyItem - position: " + position);
        }
    }


    public Fragment getRegisteredFragment(int position) {
        LogWrapper.showLog(Log.INFO, getLogTag(), "getRegisteredFragment");
        try {
            return mFragmentHolder.get(position);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

