package com.inventive.tunarun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SkipjackQuePropertyFragment : Fragment() {
    private var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()
    fun getQueue(): Fish.Skipjack.Queue {
        return queue
    }
    private lateinit var textType: TextView
    fun newInstance(queue: Fish.Skipjack.Queue): SkipjackQuePropertyFragment {
        val fragment = SkipjackQuePropertyFragment()
        fragment.queue = queue
        return fragment
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_skipjack_que_property, container, false)
        textType = view.findViewById(R.id.text_type)
        textType.text = "NUM->${queue.queue_no}"
        return view

    }
}