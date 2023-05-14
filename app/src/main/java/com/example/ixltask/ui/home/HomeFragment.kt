package com.example.ixltask.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.ixltask.data.local.model.UserEntity
import com.example.ixltask.databinding.FragmentHomeBinding
import com.example.ixltask.ui.MainViewModel
import com.example.ixltask.utils.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentHomeBinding::inflate


    private val viewModel by activityViewModels<MainViewModel>()
    private val homeAdapter = HomeAdapter(::onHomeItemClick,::onDeleteClick)

    private fun onDeleteClick(userEntity: UserEntity) {
        viewModel.deleteItem(userEntity)
    }

    private fun onHomeItemClick(userEntity: UserEntity) {
        val action = HomeFragmentDirections.actionHomeFragmentToUserDetailsFragment()
        viewModel.setSelectedUser(userEntity)
        findNavControllerSafely()?.navigate(action)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeRcv.adapter = homeAdapter
        binding.fab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToUserDetailsFragment()
            viewModel.setSelectedUser(null)
            findNavControllerSafely()?.navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allUsers.collectLatest {
                homeAdapter.submitList(it)
                binding.emptyTv.isVisible = it.isEmpty()
            }
        }
    }
}
