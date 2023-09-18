package com.example.githubusers.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.databinding.FragmentFollowsBinding
import com.example.githubusers.data.response.FollowsResponse


class FollowsFragment : Fragment() {

    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!

    private var position = 0
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager

        if (position == 1){
            detailUserViewModel.getUserFollowers(username)
        } else{
            detailUserViewModel.getUserFollowing(username)
        }

        detailUserViewModel.userFollowers.observe(requireActivity()){followers ->
            setFollowersData(followers)
        }

        detailUserViewModel.userFollowing.observe(requireActivity()){following->
            setFollowingData(following)
        }

        detailUserViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }
    }

    private fun setFollowersData(followers: List<FollowsResponse>){
        val adapter = UserFollowsAdapter()
        adapter.submitList(followers)
        binding.rvFollows.adapter = adapter
        binding.rvFollows.visibility = View.VISIBLE
    }

    private fun setFollowingData(following: List<FollowsResponse>){
        val adapter = UserFollowsAdapter()
        adapter.submitList(following)
        binding.rvFollows.adapter = adapter
        binding.rvFollows.visibility = View.VISIBLE
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
       const val ARG_POSITION: String = "arg_position"
        const val ARG_USERNAME: String = "arg_username"
    }
}