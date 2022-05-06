package com.starsolns.e_shop.ui.fragments.auth

import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
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

        /** set status bar property */
        val windowInsetsController = ViewCompat.getWindowInsetsController(requireActivity().window.decorView)
        windowInsetsController?.systemBarsBehavior =  WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController?.hide(WindowInsetsCompat.Type.statusBars())

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