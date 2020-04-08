import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagination2.Model
import com.example.pagination2.R
import com.example.pagination2.databinding.ItemListBinding
import java.util.*

class PaginationAdapter(private val context: Context) :

    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var userList: MutableList<Model>?
    private var isLoadingAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null

        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {

            ITEM -> viewHolder = getViewHolder(parent, inflater)

            LOADING -> {
                var v2 = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }

        return viewHolder!!
    }

    @NonNull
    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.item_list, parent, false)
        viewHolder = PaginationVH(v1)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model: Model = userList!![position]

        when (getItemViewType(position)) {
            ITEM -> {
                val modelVH = holder as PaginationVH

                modelVH.bindView(model)
            }
            LOADING -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (userList == null) 0 else userList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == userList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

 //-------------------------------------------------------------

    fun add(mc: Model) {
        userList!!.add(mc)
        notifyItemInserted(userList!!.size - 1)
    }

    fun addAll(mcList: List<Model>) {

            for (mc in mcList) {
                add(mc)
            }
    }

    fun remove(city: Model?) {
        val position = userList!!.indexOf(city)
        if (position > -1) {
            userList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Model())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = userList!!.size - 1
        val item: Model = getItem(position)
        if (item != null) {
            userList!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Model {
        return userList!![position]
    }

    //----------------------------------------------------------------------------------


     inner class PaginationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var lbinding: ItemListBinding?=null

        init {
            lbinding= DataBindingUtil.bind(itemView)
        }
        fun bindView(model:Model){
            this.lbinding!!.model=model
            lbinding!!.executePendingBindings()
        }
    }

    inner class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    init {
        userList = ArrayList<Model>()
    }
}