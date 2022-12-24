package com.nandaadisaputra.github.ui.activity.login

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
import com.nandaadisaputra.github.R
import com.nandaadisaputra.github.base.activity.BaseActivity
import com.nandaadisaputra.github.databinding.ActivityLoginBinding
import com.nandaadisaputra.github.ui.activity.home.HomeActivity
import com.nandaadisaputra.github.ui.activity.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        observe()
        darkMode()
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
                                loadingDialog.setResponse("Login Sukses", R.drawable.ic_check)
                                openActivity<HomeActivity>()
                                finish()
                            }
                            ApiStatus.ERROR -> {
                                it.message?.let { msg -> loadingDialog.setResponse(msg) }
                            }
                            else -> {
                                loadingDialog.dismiss()
                                loadingDialog.setResponse("Error")
                            }
                        }
                    }
                }
            }
        }
    }
    private fun validateForm() {
        if (listOf(binding.edtEmail, binding.edtPasswordLogin)
                .isEmptyRequired(R.string.label_must_fill)
        ) else {
            loadingDialog.show(R.string.logging_in)
            viewModel.login(
                binding.edtEmail.textOf(),
                binding.edtPasswordLogin.textOf()
            )
        }
    }
    override fun onClick(v: View?) {
        when (v) {
            binding.btnSignIn -> validateForm()
            binding.tvSignUp -> openActivity<RegisterActivity>()
        }
        super.onClick(v)
    }

}