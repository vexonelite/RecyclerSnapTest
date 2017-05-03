package tw.realtime.project.lativtest;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by per-erik on 14/11/14.
 */
public abstract class AbstractRecycleListAdapter<V, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

    private final byte[] mLock = new byte[0];
    private List<V> mData = new ArrayList<V>();
    private int mNumberOfTypes = 1;


    @Override
    public abstract K onCreateViewHolder(ViewGroup viewGroup, int i);

    @Override
    public abstract void onBindViewHolder(K k, int i);

    @Override
    public int getItemCount() {
        return getRealDataCount();
    }


    public int getNumberOfTypes () {
        return mNumberOfTypes;
    }

    public void setNumberOfTypes (int number) {
        if (number > 1) {
            mNumberOfTypes = number;
        }
    }

    public boolean isEmpty () {
        if (mNumberOfTypes == 1) {
            return (getRealDataCount() <= 0);
        }
        else if (mNumberOfTypes > 1) {
            return (getItemCount() <= 0);
        }
        else {
            return true;
        }
    }

    public int getRealDataCount () {
        if ( (null == mData) || (mData.isEmpty()) ) {
            return 0;
        }
        return mData.size();
    }

    public V getObjectAtPosition (int position) {

        if ((null == mData) || (mData.isEmpty())
                || (position < 0) ||
                (position >= mData.size()) ) {
            return null;
        }
        return mData.get(position);
    }

    public void appendNewDataSet(final List<V> data, boolean defaultNotify) {
        if ( (null == mData) || (null == data) ) {
            return;
        }
        final int size = data.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = mData.size();
        synchronized (mLock) {
            mData.addAll(data);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, size);
        }
    }

    public void appendNewDataSetToTheTop (final List<V> data, boolean defaultNotify) {
        if ( (null == mData) || (null == data) ) {
            return;
        }
        final int size = data.size();
        //final int start = (mData.isEmpty()) ? 0 : (mData.size() - 1);
        final int start = 0;
        synchronized (mLock) {
            mData.addAll(start, data);
        }
        if (defaultNotify) {
            notifyItemRangeInserted(start, (size - 1));
        }
    }





    public void removeAllExistingData(boolean defaultNotify) {
        if ( (null == mData) || (mData.isEmpty()) ) {
            return;
        }
        final int size = mData.size();
        synchronized (mLock) {
            mData.clear();
        }
        if (defaultNotify) {
            notifyItemRangeRemoved(0, size);
        }
    }

    public void appendNewDataToTheEnd(V entity, boolean defaultNotify) {
        if ( (null == mData) || (null == entity) ) {
            return;
        }
        final int position = mData.size();
        synchronized (mLock) {
            mData.add(entity);
        }
        if (defaultNotify) {
            notifyItemInserted(position);
        }
    }

    public void appendNewDataAtPosition(int position, V entity, boolean defaultNotify) {
        if ( (null == mData) || (null == entity) ) {
            return;
        }
        synchronized (mLock) {
            mData.add(position, entity);
        }
        if (defaultNotify) {
            notifyItemInserted(position);
        }
    }

    public void appendNewDataAtPosition(int position, int positionOffset, V entity, boolean defaultNotify) {

        if (positionOffset <= 0) {
            appendNewDataAtPosition(position, entity, defaultNotify);
        } else {
            if ( (null == mData) || (null == entity) ) {
                return;
            }
            synchronized (mLock) {
                mData.add(position, entity);
            }
            if (defaultNotify) {
                notifyItemInserted(position + positionOffset);
            }
        }
    }

    public void removeSpecifiedData (V entity, boolean defaultNotify) {
        if ( (null == mData) || (mData.isEmpty())
                || (null == entity) ) {
            return;
        }
        final int position = mData.indexOf(entity);
        if (position < 0) {
            return;
        }
        removeData(position, defaultNotify);
    }

    private void removeData(final int position, boolean defaultNotify) {
        synchronized (mLock) {
            mData.remove(position);
        }
        if (defaultNotify) {
            notifyItemRemoved(position);
        }
    }

    /**
     * Move the object from the position 'fromPosition' to the position 'toPosition'.
     */
    public void moveDataFromAtoB (final int fromPosition,
                                  final int toPosition) {

        if ( (null == mData) || (mData.isEmpty()) ) {
            return;
        }
        synchronized (mLock) {
            moveData(mData, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        }
    }

    /**
     * Move the object from the position 'a' to the position 'b'.
     */
    private void moveData (List<V> data,
                           final int fromPosition,
                           final int toPosition) {

        V temp = data.remove(fromPosition);
        data.add(toPosition, temp);
    }

    public List<V> CloneCurrentDataSet () {
        List<V> newData = new ArrayList<V>(mData);
        return newData;
    }

    public int getIndexOfObject(V entity) {
        if ( (null == mData) || (mData.isEmpty()) ) {
            return -1;
        }
        return mData.indexOf(entity);
    }
}
