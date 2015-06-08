import android.database.Cursor;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.HashSet;
import java.util.Set;


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
