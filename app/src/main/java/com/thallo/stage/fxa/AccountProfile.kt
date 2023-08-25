package com.thallo.stage.fxa

import com.thallo.stage.R

data class AccountProfile(
    val uid: String? = "00000",
    val email: String? = "null@Stage.com",
    val avatar: Any? = R.drawable.person_circle,
    val displayName: String? = "Stage@null",
)