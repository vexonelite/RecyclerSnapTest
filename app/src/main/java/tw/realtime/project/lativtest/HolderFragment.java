package tw.realtime.project.lativtest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vexonelite on 2017/5/29.
 */

public class HolderFragment extends Fragment {

    private View shroudButton;
    private int mPosition = -1;
    private int mBackgroundColor;


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public void setPosition (int position) {
        if (position > 0) {
            mPosition = position;
        }
    }

    public int getPosition () {
        return mPosition;
    }

    public void setBackgroundColor (int color) {
        mBackgroundColor = color;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_holder, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        rootView.setBackgroundColor(mBackgroundColor);

        Integer[] idArray = {R.id.shroudButton, R.id.label};
        for (Integer viewId : idArray) {
            View view = rootView.findViewById(viewId);
            if (null != view) {

                switch (viewId) {
                    case R.id.shroudButton: {
                        shroudButton = view;
                        view.setOnClickListener(new MyViewsClickCallback(viewId));
                        break;
                    }
                    case R.id.label: {
                        String text = getLogTag() + ": " + mPosition;
                        ((TextView) view).setText(text);
                        break;
                    }
                }
            }
        }
    }

    private class MyViewsClickCallback implements View.OnClickListener {
        private int mViewId;

        private MyViewsClickCallback (int viewId) {
            mViewId = viewId;
        }

        @Override
        public void onClick(View view) {
            Log.i(getLogTag(), "MyViewsClickCallback: " + mViewId);
            view.setEnabled(false);
            switch (mViewId) {

                case R.id.shroudButton: {
                    addChildFragment();
                    break;
                }
            }
            view.setEnabled(true);
        }
    }

    private void addChildFragment () {
        ChildFragment childFragment = new ChildFragment();
        childFragment.setLayerId(1);
        childFragment.setPosition(mPosition);
        replaceOrShroudFragment(childFragment, false, R.id.childContainer);
    }

    public void replaceOrShroudFragment (final Fragment targetFragment,
                                         boolean doesReplace,
                                         final int containerResId) {

        if (null == targetFragment) {
            return;
        }
        FragmentManager fragManager = getChildFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);

        if (doesReplace) {
            fragTransaction.replace(containerResId, targetFragment);
        }
        else {
            fragTransaction.add(containerResId, targetFragment);
            fragTransaction.addToBackStack(null);
        }

        if (doesReplace) {
            try {
                int len = fragManager.getBackStackEntryCount();
                if (len > 0) {
                    for(int i = 0; i < len; ++i) {
                        //fragManager.popBackStack();
                        fragManager.popBackStackImmediate();
                    }
                }
            }
            catch (Exception e) {
                Log.e(getLogTag(), "Exception on FragmentManager.popBackStackImmediate()", e);
                return;
            }
        }


        try {
            Log.i(getLogTag(), "replaceOrShroudFragment - getBackStackEntryCount: " + fragManager.getBackStackEntryCount());
            fragTransaction.commit();
            shroudButton.setVisibility(View.GONE);
        }
        catch (Exception e) {
            Log.e(getLogTag(), "Exception on FragmentTransaction.commit()", e);
        }
    }

    public void pop () {
        FragmentManager fragManager = getChildFragmentManager();
        if (fragManager.getBackStackEntryCount() <= 0) {
            return;
        }
        try {
            //fragManager.popBackStack();
            fragManager.popBackStackImmediate();
        }
        catch (Exception e) {
            Log.e(getLogTag(), "Exception on FragmentManager.popBackStackImmediate()", e);
            e.printStackTrace();
        }
    }
}
