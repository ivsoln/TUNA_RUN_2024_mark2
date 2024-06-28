package com.inventive.tunarun

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3
public class SkipjackQueAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val context: Context) : FragmentStateAdapter(fragmentManager, lifecycle) {
    var number: Long = 0
    override fun getItemCount(): Int {
        return NUM_TABS
    }
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return SkipjackQuePropertyFragment().newInstance(number)
            1 -> return SkipjackQueTimeFragment()
            2 -> return SkipjackQueProductFragment()
        }
        return Fragment()
    }
}