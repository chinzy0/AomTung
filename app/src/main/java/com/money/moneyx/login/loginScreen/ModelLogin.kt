package com.money.moneyx.login.loginScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
data class PostGenerateOTP(
    val phone: String
)

data class ResultOTP(
    val `data`: DataOTP,
    val message: String,
    val status: Int,
    val success: Boolean
)
@Parcelize
data class DataOTP(
    val codeotp: String,
    val expired: Int,
    val is_Duplicate: Boolean,
    val refCode: String,
    val sendTO: String,
    val validateLegthPhone: Boolean
) : Parcelable
