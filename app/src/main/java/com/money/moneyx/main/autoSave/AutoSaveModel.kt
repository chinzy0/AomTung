package com.money.moneyx.main.autoSave

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetListAuto(
    val `data`: List<GetListAutoData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class GetListAutoData(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val currency_symbol: String,
    val dataType: String,
    val description: String,
    val save_auto_id: Int,
    val save_auto_name: String,
    val symbol_math: String,
    val timestamp: Int,
    val transaction_id: Int,
    val type_id: Int,
    val type_name: String
): Parcelable
