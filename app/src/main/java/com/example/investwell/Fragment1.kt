package com.example.investwell

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class Fragment1 : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(ARG_PARAM1)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_1, container, false)
//        val text=view.findViewById<TextView>(R.id.tv_text)
//        text.setOnClickListener{ Navigation.findNavController(view).navigate(R.id.action_fragment1_to_fragmentNav)}
        return view
    }


    companion object {

        @JvmStatic
        fun newInstance()=Fragment1()

    }
}