package com.money.moneyx.main.profile.editProfile


data class ResetUsernameAccount(
    val `data`: ResetUsernameAccountData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class ResetUsernameAccountData(
    val is_Reseted: Boolean
)