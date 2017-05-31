package tw.realtime.project.lativtest;

import android.app.Fragment;
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

public class ChildFragment extends Fragment {

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    private int mLayerId = -1;
    private int mPosition = -1;

    public void setLayerId (int layerId) {
        if (layerId > 0) {
            mLayerId = layerId;
        }
    }

    public void setPosition (int position) {
        if (position > 0) {
            mPosition = position;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_child, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);


        Integer[] idArray = {R.id.shroudButton, R.id.label};
        for (Integer viewId : idArray) {
            View view = rootView.findViewById(viewId);
            if (null != view) {

                switch (viewId) {
                    case R.id.shroudButton: {
                        view.setOnClickListener(new MyViewsClickCallback(viewId));
                        break;
                    }
                    case R.id.label: {
                        String text = getLogTag() + ": (" + mPosition + ", " + mLayerId + ")";
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
        childFragment.setLayerId(mLayerId + 1);
        childFragment.setPosition(mPosition);
        replaceOrShroudFragment(childFragment, false, R.id.childContainer);
    }

    private HolderFragment getHolderParentFragment () {
        Fragment fragment = getParentFragment();
        if ( (null != fragment) && (fragment instanceof HolderFragment) ) {
            HolderFragment holderFragment = (HolderFragment) fragment;
            Log.i(getLogTag(), "getHolderParentFragment: Yes with position: " + holderFragment.getPosition());
            return holderFragment;
        }
        else {
            Log.w(getLogTag(), "getHolderParentFragment: false");
            return null;
        }
    }

    private void replaceOrShroudFragment (final Fragment targetFragment,
                                         boolean doesReplace,
                                         final int containerResId) {

        HolderFragment holderFragment = getHolderParentFragment();
        if (null != holderFragment) {
            holderFragment.replaceOrShroudFragment(targetFragment, doesReplace, containerResId);
        }
    }

    private void pop () {
        HolderFragment holderFragment = getHolderParentFragment();
        if (null != holderFragment) {
            holderFragment.pop();
        }
    }
}
