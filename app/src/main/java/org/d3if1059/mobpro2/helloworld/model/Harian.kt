package org.d3if1059.mobpro2.helloworld.model

import com.squareup.moshi.Json

data class Harian(
    val key: Long,
    @Json(name = "jumlah_positif") val jumlahPositif: Value
)
