package com.inventive.tunarun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
class SkipjackQuePropertyFragment : Fragment() {
    private var ARG_GAME_ID = "game_id"
    private var mGameId: Long = 0
    private lateinit var txt23: TextView
    fun newInstance(gameId: Long): SkipjackQuePropertyFragment {
        val fragment = SkipjackQuePropertyFragment()
        val args = Bundle()
        args.putLong(ARG_GAME_ID, gameId)
        fragment.setArguments(args)
        return fragment
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { 
        val args = arguments
        if (args != null) {
            mGameId = args.getLong(ARG_GAME_ID)
        }
        val view =  inflater.inflate(R.layout.fragment_skipjack_que_property, container, false)
        //this.txt23 = view.findViewById(R.id.textView23)
        //this.txt23.text = "NUM->$mGameId"
        return view
    }
}