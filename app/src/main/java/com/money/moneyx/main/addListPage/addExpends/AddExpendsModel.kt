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
