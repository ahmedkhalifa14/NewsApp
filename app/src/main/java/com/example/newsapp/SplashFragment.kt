package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.ui.NewsActivity

class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_splash, container, false)
        Handler(Looper.myLooper()!!).postDelayed({
           val intent= Intent(requireContext(), NewsActivity::class.java)
            startActivity(intent)

        },3000)
        return view
    }

}