import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.View;
import java.util.HashSet;
import java.util.Set;


/** https://github.com/qvga/RecyclerViewCursorAdapter */
public abstract class RecyclerViewCursorAdapter<T, VH extends RecyclerViewCursorAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    private SortedList<T> sortedList;
    private Set<T> previousCursorContent = new HashSet<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public RecyclerViewCursorAdapter() {
        super();
    }

    /** If you just need a plain sortedlist */
    public RecyclerViewCursorAdapter(@NonNull Class<T> klass, @Nullable Cursor cursor) {

        setSortedList(new SortedList<>(klass, new SortedListAdapterCallback<T>(this) {
            @Override
            public int compare(T o1, T o2) {
                return 0;
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return false;
            }
        }));

        setCursor(cursor);

    }


    public void setCursor(Cursor cursor) {
        switchCursor(cursor);
    }

    public void switchCursor(Cursor cursor) {

        if (cursor == null) {
            return;
        }

        Set<T> currentCursorContent = new HashSet<>();

        sortedList.beginBatchedUpdates();

        cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {

            T item = fromCursorRow(cursor);

            currentCursorContent.add(item);

            sortedList.add(item);

        }

        for (T item : previousCursorContent) {

            if (!currentCursorContent.contains(item)) {
                sortedList.remove(item);
            }

        }

        sortedList.endBatchedUpdates();

        previousCursorContent = currentCursorContent;

    }


    abstract T fromCursorRow(Cursor cursor);

    void setSortedList(SortedList<T> list) {
        this.sortedList = list;
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public T getItem(int position) {
        return sortedList.get(position);
    }

}
