package com.calyrsoft.ucbp1.features.profile.domain.repository

import com.calyrsoft.ucbp1.features.profile.domain.model.ProfileModel

interface IProfileRepository {
    suspend fun fetchData(): Result<ProfileModel>
}