package com.starsolns.e_shop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.starsolns.e_shop.R
import com.starsolns.e_shop.databinding.FragmentAccessBinding
import com.starsolns.e_shop.ui.adapter.PagerAdapter

class AccessFragment : Fragment() {

    private var _binding: FragmentAccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccessBinding.inflate(layoutInflater, container, false)

        val fragments = ArrayList<Fragment>()
        fragments.add(LoginFragment())
        fragments.add(RegisterFragment())

        val titles = ArrayList<String>()
        titles.add("Login")
        titles.add("Register")

        var pagerAdapter = PagerAdapter(fragments, requireActivity())
        binding.accessViewpager.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.accessTabLayout, binding.accessViewpager){tab, position->
            tab.text = titles[position]
        }.attach()


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}