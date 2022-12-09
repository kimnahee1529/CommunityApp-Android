package com.example.communityapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.communityapp.R
import com.example.communityapp.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private lateinit var binding : FragmentStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store, container, false)

        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_homeFragment)
        }

        binding.tipTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_tipFragment)
        }

        binding.bookmarkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_bookmarkFragment)
        }

        binding.talkTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_storeFragment_to_talkFragment)
        }

        val view = inflater.inflate(R.layout.fragment_store, container, false)
        val webView: WebView = view.findViewById(R.id.storeWebView)
        webView.loadUrl("https://www.inflearn.com/")
//        return binding.root

        return view
    }


}