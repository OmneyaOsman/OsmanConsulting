package com.omni.osmanconsulting.feature

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.omni.domain.AuthState


@BindingAdapter("status")
fun ProgressBar.updateProgress(status: AuthState?) {
    visibility = if (status == AuthState.LOADING) View.VISIBLE else View.GONE
}


//@BindingAdapter("{app:errorText}")
//fun TextInputLayout.setErrorMsg(errorMsg: String?) {
//    error = "$errorMsg"
//    Log.d("LoginBin" ,"${status?.name}")
//    this.error = when (status) {
//        Status.VALID -> null
//        Status.INVALID -> {
//            if (hint == resources.getString(R.string.e_mail))
//                resources.getString(R.string.required_valid_email)
//            else
//                resources.getString(R.string.password_error_msg)
//        }
//        Status.EMPTY -> resources.getString(R.string.required)
//        else -> null
//    }

//}


