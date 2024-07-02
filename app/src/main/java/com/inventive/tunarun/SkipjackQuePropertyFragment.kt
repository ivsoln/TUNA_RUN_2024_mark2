package com.inventive.tunarun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
class SkipjackQuePropertyFragment : Fragment() {
    private var queId: Long = 0
    private lateinit var textType: TextView
    fun newInstance(queId: Long): SkipjackQuePropertyFragment {
        val fragment = SkipjackQuePropertyFragment()
        val args = Bundle()
        args.putLong("QUE_ID", queId)
        fragment.setArguments(args)
        return fragment
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { 
        val args = arguments
        if (args != null) {
            queId = args.getLong("QUE_ID")
        }
        val view =  inflater.inflate(R.layout.fragment_skipjack_que_property, container, false)
        textType = view.findViewById(R.id.text_type)
        textType.text = "NUM->$queId"
        return view
    }
}