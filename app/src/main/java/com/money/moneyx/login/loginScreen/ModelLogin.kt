package com.money.moneyx.login.loginScreen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

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
): Parcelable
@Parcelize
data class ConfirmOTP(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
) : Parcelable

@Parcelize

data class Data(
    val is_Success: Boolean,
    val message: String
) : Parcelable

data class CreateAccount(
    val `data`: CreateAccountData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class CreateAccountData(
    val is_Success: Boolean,
    val message: String
)
data class MemberLogin(
    val `data`: MemberLoginData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class MemberLoginData(
    val idmember: Int,
    val image: String,
    val is_Verified: Boolean,
    val username: String
)

@Parcelize

data class OTPForgotPassword(
    val `data`: OTPForgotPasswordData,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class OTPForgotPasswordData(
    val codeotp: String,
    val expired: Int,
    val is_seccess: Boolean,
    val refCode: String,
    val sendTO: String
): Parcelable

data class ForgotPassword(
    val `data`: ForgotPasswordData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ForgotPasswordData(
    val idmember: Int,
    val image: String,
    val is_Seccess: Boolean,
    val message: String,
    val username: String
)
data class ResetPhone(
    val `data`: ResetPhoneData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ResetPhoneData(
    val is_Seccess: Boolean,
    val message: String,
    val phone: String
)