package com.sachin.population.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("measures") var measures: List<String>,
    @SerializedName("annotations") var annotations: Annotations? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("substitutions") var substitutions: List<String>
)