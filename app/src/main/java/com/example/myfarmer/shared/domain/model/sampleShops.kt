package com.example.myfarmer.shared.domain.model

import androidx.compose.ui.graphics.Color

val sampleShops: List<Shop> by lazy {
    listOf(
        Shop(
            id = "1",
            name = "FI MUNI",
            images = listOf(
                ImageResource("https://picsum.photos/400/300"),
                ImageResource("https://picsum.photos/200/400"),
                ImageResource("https://picsum.photos/600/100"),
                ImageResource("https://picsum.photos/400/301"),
                ImageResource("https://picsum.photos/200/401"),
                ImageResource("https://picsum.photos/600/101"),
                ImageResource("https://picsum.photos/400/302"),
                ImageResource("https://picsum.photos/200/402"),
                ImageResource("https://picsum.photos/600/102"),
                ImageResource("https://picsum.photos/400/303"),
                ImageResource("https://picsum.photos/200/403"),
                ImageResource("https://picsum.photos/600/103"),
            ),
            description = "Na FI je všechno",
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
            id = "2",
            name = "Domácí včelařství v Jundrově",
            images = listOf(
                ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Beekeeper_with_moveable_comb_hive.jpg/1024px-Beekeeper_with_moveable_comb_hive.jpg"),
                ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Western_honey_bee_on_a_honeycomb.jpg/1280px-Western_honey_bee_on_a_honeycomb.jpg"),
                ImageResource("https://justbeehoney.co.uk/cdn/shop/articles/104740553_l2_5000x.jpg")
            ),
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
            location = ShopLocation(49.209166, 16.556608),
            categories = listOf(
                Category("Med", Color(0xFFFFC107)),
                Category("Včelařství", Color(0xFFFF9800)),
            ),
            userRatings = listOf(
                UserRating(5.0, "Amazing"),
                UserRating(4.5, "Very good"),
            ),
            averageRating = (5 + 4.5) / 2,
            menu = ProductMenu(
                listOf(
                    Product(
                        name = "Med",
                        pricePerUnit = PricePerUnit("150 CZK", UnitType.KG),
                    ),
                )
            )
        ),
        Shop(
            id = "3",
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
            id = "4",
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