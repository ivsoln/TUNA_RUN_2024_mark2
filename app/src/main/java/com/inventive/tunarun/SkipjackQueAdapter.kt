package com.inventive.tunarun

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM_TABS = 3

public class SkipjackQueAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val context: Context, queue: Fish.Skipjack.Queue
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()

    private lateinit var fragmentProperty: SkipjackQuePropertyFragment
    private lateinit var fragmentTime: SkipjackQueTimeFragment
    private lateinit var fragmentProduct: SkipjackQueProductFragment

    init {
        this.queue = queue
    }

    fun getQueue(): Fish.Skipjack.Queue {
        return queue
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                fragmentProperty = SkipjackQuePropertyFragment().newInstance(queue)
                return fragmentProperty
            }

            1 -> {
                fragmentTime = SkipjackQueTimeFragment().newInstance(queue)
                return fragmentTime
            }

            2 -> {
                fragmentProduct = SkipjackQueProductFragment().newInstance(queue)
                return fragmentProduct
            }
        }
        return Fragment()
    }
}