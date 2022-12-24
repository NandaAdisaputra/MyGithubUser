package com.nandaadisaputra.github.base.fragment

import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.fragment.CoreFragment


open class BaseFragment<VB: ViewDataBinding>(layoutRes: Int): CoreFragment<VB>(layoutRes)