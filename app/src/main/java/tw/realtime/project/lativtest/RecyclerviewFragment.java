package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    private int mPosition;

    private RecyclerView mRecyclerView;
    private AbstractRecycleListAdapter mAdapter;
    //private int mScrollPosition;

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    public void setPosition (int position) {
        mPosition = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

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
        ((MyListAdapter)mAdapter).allNewDataSet(dataList, true);

        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);
        //recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, this));
        //optimize();
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

        private View mContainer;
        private TextView mLabelView;

        private MyListItem (View itemView) {
            super(itemView);
            mContainer = itemView;
            mContainer.setBackgroundColor(getColorByPosition());
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
            mContainer.setOnClickListener(null);
            mLabelView.setText("");//
        }
    }




    /**
     * ref: https://github.com/nostra13/Android-Universal-Image-Loader/wiki/Useful-Info
     *
    private void optimize() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        imageLoader.resume();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        imageLoader.pause();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        imageLoader.pause();
                        break;
                }
            }
            /*
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                boolean enabled = manager.findFirstCompletelyVisibleItemPosition() == 0;
                //s.setEnabled((ViewCompat.canScrollVertically(recyclerView, -1) ));
                mSwipeRefresh.setEnabled(enabled);
            }
            * /
        });
    }
    */


    /*
    protected void scrollToSpecifiedPosition (int position) {
        try {
            //Log.i(getLogTag(), "scrollToSpecifiedPosition: " + position);
            //mRecyclerView.scrollToPosition(position);
            LogWrapper.showLog(Log.INFO, getLogTag(), "scrollToSpecifiedPosition: " + mScrollPosition);
            mRecyclerView.scrollToPosition(mScrollPosition);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int getScrollPosition () {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mScrollPosition = layoutManager.findFirstVisibleItemPosition();
        LogWrapper.showLog(Log.INFO, getLogTag(), "getScrollPosition: " + mScrollPosition);
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }
    */

    private int getColorByPosition () {
        int resId = R.color.gray_0004;
        switch (mPosition) {
            case 0:
                resId = R.color.pink_002;
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
        return CodeUtils.getColorFromResourceId(getContext(), resId);
    }

}
