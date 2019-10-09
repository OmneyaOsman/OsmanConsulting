package com.omni.osmanconsulting.feature

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.omni.osmanconsulting.feature.auth.AuthState


@BindingAdapter("status")
fun ProgressBar.updateProgress(status: AuthState?) {
    when (status) {
        AuthState.LOADING -> visibility = View.VISIBLE
        AuthState.AUTHENTICATED, AuthState.FAILED, AuthState.NOT_VERIFIED -> View.GONE
    }

}

@BindingAdapter("bind:onKeyListener")
fun setOnKeyListener(view: TextInputLayout, pOnKeyListener: View.OnKeyListener) {
    view.setOnKeyListener(pOnKeyListener)
}