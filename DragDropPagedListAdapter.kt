import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import androidx.core.view.MotionEventCompat
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.*


abstract class DragDropPagedListAdapter<T, VH : DragDropPagedListAdapter.ViewHolder> :
    RecyclerView.Adapter<VH>() {

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    var wasRecentlyDragged = false
    var isDragging = false


    var differ: AsyncPagedListDiffer<T> = AsyncPagedListDiffer(
        Callback(this),
        AsyncDifferConfig.Builder<T>(object : DiffUtil.ItemCallback<T>() {


            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return this@DragDropPagedListAdapter.areItemsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return this@DragDropPagedListAdapter.areContentsTheSame(oldItem, newItem)

            }

            override fun getChangePayload(oldItem: T, newItem: T): Any? {
                return this@DragDropPagedListAdapter.getChangePayload(oldItem, newItem)

            }
        }).build()
    ).also {

        it.addPagedListListener { _, _ ->

            wasRecentlyDragged = false

        }
    }


    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun getChangePayload(oldItem: T, newItem: T): Any?
    abstract fun onItemMoved(fromPosition: Int, toPosition: Int)
    abstract fun onDragStart()
    abstract fun onDragEnd()


    fun getItem(position: Int): T? = differ.getItem(position)

    override fun getItemCount(): Int = differ.itemCount

    fun submitList(pagedList: PagedList<T>) {

        differ.submitList(pagedList)

    }


    fun setItemTouchHelper(view: View, viewholder: ViewHolder) {


        view.setOnTouchListener { _: View?, event: MotionEvent? ->

            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(viewholder)
                isDragging = true
                onDragStart()
            }
            false
        }


    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private val itemTouchHelper = ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {


        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            if (!isCurrentlyActive && isDragging) {
                isDragging = false
                wasRecentlyDragged = true

                onDragEnd()
            }

        }


        override fun onMove(
            recyclerView: RecyclerView,
            dragged: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = dragged.adapterPosition
            val toPosition = target.adapterPosition
            notifyItemMoved(fromPosition, toPosition)
            onItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        override fun isLongPressDragEnabled(): Boolean = false


    })


    class Callback<T, ViewHolder : DragDropPagedListAdapter.ViewHolder>(private val adapter: DragDropPagedListAdapter<T, ViewHolder>) :
        ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            if (!adapter.wasRecentlyDragged)
                adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            if (!adapter.wasRecentlyDragged)
                adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            if (!adapter.wasRecentlyDragged)
                adapter.notifyItemRangeRemoved(position, count)
        }

    }


}

