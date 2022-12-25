package com.nandaadisaputra.github.ui.fragment.following

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.crocodic.core.extension.openActivity
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.fragment.BaseFragment
import com.nandaadisaputra.github.data.room.user.UsersEntity
import com.nandaadisaputra.github.databinding.FragmentFollowingBinding
import com.nandaadisaputra.github.databinding.ItemUserBinding
import com.nandaadisaputra.github.ui.activity.detail.DetailActivity
import com.nuryazid.core.base.adapter.CoreListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : BaseFragment<FragmentFollowingBinding>(R.layout.fragment_following) {
    private val followingUser = ArrayList<UsersEntity?>()
    private lateinit var viewModel: FollowingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this@FollowingFragment)[FollowingViewModel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.viewModel = viewModel
        val extraUser =
            activity?.intent?.getParcelableExtra<UsersEntity>(DetailActivity.EXTRA_USER) as UsersEntity
        extraUser.login?.let { setupViewModel(it) }
        setupRecyclerView()
        initAdapter()
    }

    private fun initAdapter() {
        viewModel.following.observe(viewLifecycleOwner) {
            binding?.adapter = CoreListAdapter<ItemUserBinding, UsersEntity>(
                R.layout.item_user
            )
                .initItem(followingUser) { position, data ->
                    context?.openActivity<DetailActivity> {
                        putExtra(DetailActivity.EXTRA_USER, data)
                    }
                }
        }
    }

    private fun setupRecyclerView() {
        binding?.apply {
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.setHasFixedSize(true)
        }
    }

    private fun setupViewModel(username: String) {
        viewModel.apply {
            setListFollowing(username)
            fun setList(user: ArrayList<UsersEntity>) {
                followingUser.clear()
                followingUser.addAll(user)
                binding?.rvFollowing?.adapter?.notifyDataSetChanged()
            }
            getListFollowing().observe(viewLifecycleOwner) {
                if (it != null) {
                    setList(it)
                }
                if (it.isEmpty()) {
                    showEmpty(true)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun showEmpty(state: Boolean) {
        binding?.vEmpty?.isVisible = state
        binding?.rvFollowing?.isGone = state
    }
}