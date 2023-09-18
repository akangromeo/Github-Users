package com.example.githubusers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusers.R
import com.example.githubusers.data.response.DetailUserResponse
import com.example.githubusers.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra(NAME)
        detailUserViewModel.getDetailUser(name.toString())

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.user.observe(this) { user ->
            if (user != null) {
                setDetailUserData(user)
            }
        }

        detailUserViewModel.toastbarText.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        val followsPagerAdapter = FollowsPagerAdapter(this)
        followsPagerAdapter.username = name.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = followsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailUserData(user: DetailUserResponse) {
        binding.apply {

            Glide.with(ivUserDetail.context)
                .load(user.avatarUrl)
                .into(
                    ivUserDetail
                )
            tvNameDetail.text = user.name

            tvUsernameDetail.text = user.login

            tvFollowersDetail.text = user.followers.toString() + " Followers"

            tvFollowingDetail.text = user.following.toString() + " Following"

            viewPager.visibility = View.VISIBLE
            tabLayout.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val NAME = ""
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tab,
            R.string.following_tab
        )
    }
}