package com.tondracek.myfarmer.shopcategory.domain.usecase

import com.tondracek.myfarmer.common.string.fuzzyScore
import com.tondracek.myfarmer.shopcategory.domain.model.CategoryPopularity
import javax.inject.Inject

class GetCategorySuggestionsUC @Inject constructor() {

    operator fun invoke(
        input: String,
        popular: List<CategoryPopularity>
    ): List<CategoryPopularity> = when (input.isBlank()) {
        true -> popular
        false -> (popular + CategoryPopularity(input, Int.MAX_VALUE))
            .map { it to fuzzyScore(input, it.name) }
            .filter { (_, score) -> score > 0 }
            .sortedWith(
                compareByDescending<Pair<CategoryPopularity, Int>> { (popularity, _) -> popularity.count }
                    .thenByDescending { (_, score) -> score }
            )
            .map { (popularity, _) -> popularity }
            .distinctBy { it.name }
            .take(6)
    }
}
