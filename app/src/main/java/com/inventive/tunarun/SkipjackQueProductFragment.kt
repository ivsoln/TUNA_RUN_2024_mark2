package com.inventive.tunarun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

class SkipjackQueProductFragment : Fragment() {
    private var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()
    fun getQueue(): Fish.Skipjack.Queue {
        return queue
    }
    fun newInstance(queue: Fish.Skipjack.Queue): SkipjackQueProductFragment {
        val fragment = SkipjackQueProductFragment()
        fragment.queue = queue
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skipjack_que_product, container, false)
    }
}