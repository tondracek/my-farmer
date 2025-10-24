package com.tondracek.myfarmer.shop.data

import com.tondracek.myfarmer.core.repository.EntityMapper
import com.tondracek.myfarmer.core.repository.request.RepositoryRequest
import com.tondracek.myfarmer.core.repository.request.filterIn
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.systemuser.data.UserEntity
import com.tondracek.myfarmer.systemuser.data.UserRepository
import com.tondracek.myfarmer.systemuser.domain.model.SystemUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.util.UUID

class ShopMapper(
    private val userRepository: UserRepository,
) : EntityMapper<Shop, ShopEntity> {

    override fun toEntity(model: Shop): ShopEntity = model.toEntity()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun mapFlowToModel(flow: Flow<ShopEntity>): Flow<Shop> {
        val ownerFlow = flow.flatMapLatest { shopEntity ->
            val ownerId = UUID.fromString(shopEntity.ownerId)
            userRepository.getByID(ownerId)
        }

        return combine(
            flow,
            ownerFlow,
        ) { shopEntity, owner ->
            if (owner == null)
                throw IllegalStateException("Owner with ID ${shopEntity.ownerId} not found for Shop ${shopEntity.id}")

            // TODO: does this update when user stays the same but shop changes???

            println("UPDATED!!!")

            shopEntity.toModel(owner = owner)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun mapEntitiesFlowToModel(flow: Flow<List<ShopEntity>>): Flow<List<Shop>> {
        val ownerIdsFlow: Flow<List<String>> = flow.map { entities: List<ShopEntity> ->
            entities.map { it.ownerId }.distinct()
        }
        val ownersFlow: Flow<List<SystemUser>> = ownerIdsFlow.flatMapLatest { ids: List<String> ->
            val ownersRequest = RepositoryRequest.Builder()
                .addFilter(UserEntity::id filterIn ids)
                .build()
            userRepository.get(ownersRequest)
        }

        return combine(
            flow,
            ownersFlow
        ) { shopEntities, owners ->
            shopEntities.mapNotNull { shopEntity ->
                val owner = owners.find { it.id.toString() == shopEntity.ownerId }
                if (owner == null) return@mapNotNull null

                shopEntity.toModel(owner = owner)
            }
        }
    }
}