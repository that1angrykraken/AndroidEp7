package seamonster.kraken.androidep7.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import seamonster.kraken.androidep7.ui.checkin.CheckInFragment
import seamonster.kraken.androidep7.ui.notifications.NotificationFragment
import seamonster.kraken.androidep7.ui.tracking.TrackingFragment
import seamonster.kraken.androidep7.ui.user.UserFragment

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrackingFragment()
            1 -> UserFragment()
            2 -> NotificationFragment()
            3 -> CheckInFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}