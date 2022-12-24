package com.nandaadisaputra.github.ui.activity.register

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.databinding.ActivityRegisterBinding
import com.nandaadisaputra.github.ui.activity.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity :
    BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        darkMode()
        //set toolbar
        setSupportActionBar(binding.toolbarRegister)

        //transparent status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        observe()

    }

    private fun darkMode() {
        viewModel.getTheme.observe(this) { isDarkMode ->
            checkDarkMode(isDarkMode)
        }
    }

    private fun checkDarkMode(isDarkMode: Boolean) {
        when (isDarkMode) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> {
                                it.message?.let { msg -> loadingDialog.show(msg) }
                            }
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                loadingDialog.setResponse("Register Sukses", R.drawable.ic_check)
                                openActivity<LoginActivity>()
                                finishAffinity()
                            }
                            ApiStatus.ERROR -> {
                                it.message?.let { msg -> loadingDialog.setResponse(msg) }
                            }
                            else -> {
                                loadingDialog.dismiss()
                                tos(R.string.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateForm() {
        if (listOf(
                binding.edtNameRegister,
                binding.edtEmailRegister,
                binding.edtPasswordRegister
            )
                .isEmptyRequired(R.string.label_must_fill)
        ) else {
            loadingDialog.show(R.string.registering)
            viewModel.register(
                binding.edtNameRegister.textOf(),
                binding.edtEmailRegister.textOf(),
                binding.edtPasswordRegister.textOf()
            )
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRegister -> validateForm()
        }
        super.onClick(v)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}