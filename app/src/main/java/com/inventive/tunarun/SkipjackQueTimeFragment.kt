package com.inventive.tunarun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SkipjackQueTimeFragment : Fragment() {
    fun newInstance(queId: Long): SkipjackQueTimeFragment {
        val fragment = SkipjackQueTimeFragment()
        val args = Bundle()
        args.putLong("QUE_ID", queId)
        fragment.setArguments(args)
        return fragment
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skipjack_que_time, container, false)
    }

}