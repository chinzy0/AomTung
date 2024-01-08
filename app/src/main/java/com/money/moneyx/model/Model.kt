package com.money.moneyx.model

class ListBookingStatus : ArrayList<ListBookingStatusItem>()

data class ListBookingStatusItem(
    val date_str: String,
    val listAdvisorDtos: List<AdvisorDtos>
)

data class AdvisorDtos(
    val advisor_member_id: Int,
    val advisor_type_name: String,
    val advisor_type_name_display: String,
    val attention_tag: String,
    val firstname: String,
    val image_profile: String,
    val lastname: String,
    val nickname: String,
    val price_per_minutes: Int,
    val price_per_minutes_display: String,
    val rating_status: String,
    val reserve_lfp_date: Int,
    val reserve_lfp_date_str: String,
    val reserve_lfp_id: Int,
    val reserve_lfp_time_str: String,
    val reserve_ref_no: String,
    val status: String
)

