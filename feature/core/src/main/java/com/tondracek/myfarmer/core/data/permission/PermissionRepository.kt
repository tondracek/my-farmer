package com.tondracek.myfarmer.core.data.permission

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionRepository @Inject constructor() {

    private val _locationPermission: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val locationPermission: StateFlow<Boolean> = _locationPermission

    fun setLocationPermission(value: Boolean) = _locationPermission.update { value }
}