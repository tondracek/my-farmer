package com.tondracek.myfarmer.demo.domain

import com.tondracek.myfarmer.demo.data.DemoRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class AddDemoUC @Inject constructor(
    private val demoRepository: DemoRepository
) {
    suspend operator fun invoke() {
        val demo = Demo(
            id = UUID.randomUUID(),
            name = "Demo ${(0..100).random()}",
            index = (0..10).random(),
            date = LocalDateTime.now()
        )

        demoRepository.create(demo)
    }
}