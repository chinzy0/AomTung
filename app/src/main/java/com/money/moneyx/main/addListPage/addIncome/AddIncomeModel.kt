package com.money.moneyx.main.addListPage.addIncome

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AddIncomeModel(
    val title: String,
    val img: Int,
    val detail: String,
    val time: String
)
@Parcelize
data class GetAllTypeIncome(
    val `data`: List<GetAllTypeIncomeData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize

data class GetAllTypeIncomeData(
    val id: Int,
    val type: String
): Parcelable

@Parcelize
data class GetAllCategoryincome(
    val `data`: List<GetAllCategoryincomeData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable

@Parcelize

data class GetAllCategoryincomeData(
    val category: String,
    val id: Int,
    val image: String

): Parcelable
@Parcelize

data class ListScheduleAuto(
    val `data`: List<ListScheduleAutoData>,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable


@Parcelize
data class ListScheduleAutoData(
    val frequency: String,
    val id: Int
): Parcelable


@Parcelize
data class CreateListIncome(
    val `data`: CreateListIncomeData,
    val message: String,
    val status: Int,
    val success: Boolean
): Parcelable
@Parcelize
data class CreateListIncomeData(
    val create_success: Boolean
): Parcelable


