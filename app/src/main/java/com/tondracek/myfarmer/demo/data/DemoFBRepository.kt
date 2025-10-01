package com.tondracek.myfarmer.demo.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.firebase.FirebaseRepository
import com.tondracek.myfarmer.demo.domain.Demo
import javax.inject.Inject

class DemoFBRepository @Inject constructor() :
    FirebaseRepository<Demo, DemoFbDto>(DemoFbDto::class.java) {

    override val mapper: EntityMapper<Demo, DemoFbDto> = DemoEntityMapper
}
