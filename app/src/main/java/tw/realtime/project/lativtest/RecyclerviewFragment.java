package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vexonelite on 2017/5/3.
 */
public class RecyclerviewFragment extends Fragment {


    private int mBackgroundColor;

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private AbstractRecycleListAdapter mAdapter;
    //private int mScrollPosition;

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public void setBackgroundColor (int color) {
        mBackgroundColor = color;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        setupRecyclerView(recyclerView);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

//    @Override
//    public void onResume () {
//        super.onResume();
//    }

    private void setupRecyclerView (RecyclerView recyclerView) {

        if (null == recyclerView) {
            return;
        }

        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(i + 1);
        }

        mAdapter = new MyListAdapter();
        ((MyListAdapter)mAdapter).appendNewDataSet(dataList, true);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);

        //Page mode, but it can turn multiple pages at once when user is flipping
        //new LinearSnapHelper().attachToRecyclerView(recyclerView);

        // Page mode, but has the same effect as using ViewPager
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        mRecyclerView.setHasFixedSize(true);

    }

    private class MyListAdapter extends AbstractRecycleListAdapter<Integer, RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new MyListItem( inflater.inflate(
                    R.layout.fragment_recyclerview_item, parent, false) );
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (viewHolder instanceof MyListItem) {
                MyListItem cell = (MyListItem) viewHolder;
                cell.onBind(getObjectAtPosition(position), position);
            }
        }
    }

    private class MyListItem extends RecyclerView.ViewHolder {

        private TextView mLabelView;

        private MyListItem (View itemView) {
            super(itemView);
            itemView.setBackgroundColor(mBackgroundColor);
            mLabelView = (TextView) itemView.findViewById(R.id.label);
        }

        public void onBind (Integer item, int position) {
            resetItemViewLooks();
            if (null == item) {
                return;
            }

            String text = "";
            text = text + item;
            mLabelView.setText(text);
        }

        private void resetItemViewLooks () {
            mLabelView.setText("");
        }
    }

    public void scrollToSpecifiedPosition (int position) {
        try {
            LogWrapper.showLog(Log.INFO, getLogTag(), "scrollToSpecifiedPosition: " + position);
            mRecyclerView.scrollToPosition(position);
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on scrollToSpecifiedPosition!", e);
        }
    }
}
