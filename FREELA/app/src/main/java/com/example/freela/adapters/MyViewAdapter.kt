import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.freela.OrderDetailsFragment
import com.example.freela.OrderProposalsFragment
import com.example.freela.model.Order

class MyViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private var order: Order? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = OrderDetailsFragment()
                fragment.arguments = Bundle().apply {
                    putParcelable("order", order)
                }
                fragment
            }
            1 -> {
                val fragment = OrderProposalsFragment()
                fragment.arguments = Bundle().apply {
                    putParcelable("order", order)
                }
                fragment
            }
            else -> OrderDetailsFragment()
        }
    }
    fun setOrder(order: Order) {
        this.order = order
        notifyDataSetChanged()
    }
}
