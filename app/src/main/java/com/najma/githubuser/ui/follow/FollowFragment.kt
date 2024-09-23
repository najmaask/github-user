package com.najma.githubuser.ui.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.databinding.FragmentFollowBinding
import com.najma.githubuser.ui.UserAdapter
import com.najma.githubuser.ui.ViewModelFactory
import com.najma.githubuser.ui.detail.DetailViewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        val detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        val username = arguments?.getString(ARG_USERNAME)
        binding.rvFollow.layoutManager  = LinearLayoutManager(requireActivity())
        binding.rvFollow.setHasFixedSize(true)

        detailViewModel.listFollow.observe(viewLifecycleOwner) { listFollow ->
            setListFollowData(listFollow)
        }
        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            0 -> {
                detailViewModel.showListFollower(username)
            }
            1 -> {
                detailViewModel.showListFollowing(username)
            }
        }
    }

    private fun setListFollowData(listFollow: List<ListUser>) {
        val listFollowAdapter = UserAdapter(listFollow as ArrayList<ListUser>)
        binding.rvFollow.adapter = listFollowAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_SECTION_NUMBER = "arg_section_number"
    }
}