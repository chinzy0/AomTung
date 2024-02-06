package com.money.moneyx.main.addListPage.addExpends

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetAllTypeExpenses(
    val `data`: List<GetAllTypeExpensesData>,
    val message: String,
    val status: Int,
    val success: Boolean
)
@Parcelize

data class GetAllTypeExpensesData(
    val id: Int,
    val type: String
): Parcelable

@Parcelize

data class GetAllCategoryExpenses(
    val `data`: List<GetAllCategoryExpensesData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize

data class GetAllCategoryExpensesData(
    val category: String,
    val id: Int,
    val image: String
): Parcelable


@Parcelize
data class CreateListExpenses(
    val `data`: CreateListExpensesData,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize
data class CreateListExpensesData(
    val createSuccess: Boolean
): Parcelable
data class UpdateExpenses(
    val `data`: UpdateExpensesData,
    val message: String,
    val status: Int,
    val success: Boolean
)

data class UpdateExpensesData(
    val amount: String,
    val category_id: Int,
    val category_name: String,
    val createdateTime: Int,
    val description: String,
    val expenses_id: Int,
    val is_Updated: Boolean,
    val save_auto_id: Int,
    val save_auto_name: String,
    val type_id: Int,
    val type_name: String
)