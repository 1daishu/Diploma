import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.diploma.databinding.MealElementBinding
import com.dev.diploma.domain.model.MealItem

class OrderAdapter : ListAdapter<MealItem, OrderAdapter.OrderViewHolder>(OrderDiffUtil) {
    class OrderViewHolder(private val binding: MealElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: MealItem) {
            binding.apply {
                tvCategoriesElement.text = meal.name
                tvCalories.text = meal.calories
                Glide.with(itemView.context)
                    .load(meal.photo_url)
                    .into(imgCategoriesElement)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = MealElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object OrderDiffUtil : DiffUtil.ItemCallback<MealItem>() {
        override fun areItemsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MealItem, newItem: MealItem): Boolean {
            return oldItem == newItem
        }
    }

    fun submitMealItems(mealItems: List<MealItem>) {
        submitList(mealItems)
    }
}
