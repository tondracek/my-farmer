package com.example.myfarmer.shared.domain

import androidx.compose.ui.graphics.Color

val sampleShops: Set<Shop> by lazy {
    setOf(
        Shop(
            name = "FI MUNI",
            images = listOf(),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
            location = ShopLocation(49.209806, 16.598833),
            categories = listOf(
                Category("Zelenina", Color(0xFF067C06)),
                Category("Ovoce", Color(0xFFE4560B)),
                Category("Maso", Color(0xFF642000)),
                Category("Mléčné výrobky", Color(0xFFCFCECA)),
                Category("Pečivo", Color(0xFFCA6207)),
                Category("Vejce", Color(0xFFBE9329)),
                Category("Další", Color(0xFF0B5DE4)),
                Category("Další", Color(0xFF067C06)),
                Category("Další", Color(0xFFE4560B)),
                Category("Další", Color(0xFF642000)),
                Category("Další", Color(0xFFCFCECA)),
                Category("Další", Color(0xFFCA6207)),
                Category("Další", Color(0xFFBE9329)),
            ),
            userRatings = emptyList(),
            averageRating = 0.0,
            menu = ProductMenu(listOf())
        ),

        Shop(
            name = "Obchod s vejci",
            images = listOf(
                ImageResource("https://picsum.photos/400/300"),
                ImageResource("https://picsum.photos/200/400"),
                ImageResource("https://picsum.photos/600/100"),
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
            location = ShopLocation(49.205778, 16.593361),
            categories = listOf(
                Category("Vejce", Color(0xFFBE9329)),
            ),
            userRatings = listOf(
                UserRating(5.0, "Amazing"),
                UserRating(2.5, "Nuh, average"),
            ),
            averageRating = (5 + 2.5) / 2,
            menu = ProductMenu(
                listOf(
                    Product(
                        name = "Vejce",
                        pricePerUnit = PricePerUnit("2 CZK", UnitType.PIECE),
                    ),
                )
            )
        ),

        Shop(
            name = "Obchod s masem a mnohem více, především s extra dlouhým názvem, který jen tak nekončí, takže se musíme připravit na to, že se nám to nevejde do komponent a může nadělat paseku, když si na to nedáme pozor",
            images = listOf(
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
            location = ShopLocation(49.205778, 16.59),
            categories = listOf(
                Category("Maso", Color(0xFF642000)),
            ),
            userRatings = listOf(
                UserRating(5.0, "Amazing"),
                UserRating(2.5, "Nuh, average"),
            ),
            averageRating = (5 + 2.5) / 2,
            menu = ProductMenu(
                listOf(
                    Product(
                        name = "Vepřové maso",
                        pricePerUnit = PricePerUnit("2 CZK", UnitType.KG),
                    ),
                )
            )
        ),
    )
}