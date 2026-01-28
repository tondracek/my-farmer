package com.tondracek.myfarmer.ui.common.sample

import androidx.core.graphics.toColorInt
import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.common.color.fromArgb
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.review.domain.model.Rating
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.review.domain.model.ReviewId
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.ui.common.review.ReviewUiState
import com.tondracek.myfarmer.ui.common.review.toUiState
import java.time.DayOfWeek

val shop0 = Shop(
    id = ShopId.fromString("ded3f207-09d3-47e7-94d0-1f48cb10ef12"),
    name = "Domácí včelařství v Jundrově",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    ownerId = user0.id,
    images = listOf(
        ImageResource("https://justbeehoney.co.uk/cdn/shop/articles/104740553_l2_5000x.jpg"),
        ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Beekeeper_with_moveable_comb_hive.jpg/1024px-Beekeeper_with_moveable_comb_hive.jpg"),
        ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Western_honey_bee_on_a_honeycomb.jpg/1280px-Western_honey_bee_on_a_honeycomb.jpg"),
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
    location = Location(49.209166, 16.556608),
    categories = listOf(
        ShopCategory(name = "Med", color = RgbColor.fromArgb(0xFFFFC107.toColorInt())),
        ShopCategory(name = "Včelařství", color = RgbColor.fromArgb(0xFFFF9800.toColorInt())),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Med",
                description = "Kvalitní med z naší domácí produkce.",
                price = PriceLabel("150 CZK / kg"),
                inStock = true,
            ),
            MenuItem(
                name = "Včelí vosk",
                description = "Přírodní včelí vosk pro různé využití.",
                price = PriceLabel("200 CZK / kg"),
                inStock = true,
            ),
            MenuItem(
                name = "Propolis",
                description = "Léčivá pryskyřice sbíraná včelami.",
                price = PriceLabel("300 CZK / 100 g"),
                inStock = false,
            ),
            MenuItem(
                name = "Rámky na plástve",
                description = "Dřevěné rámky pro včelí plástve.",
                price = PriceLabel("50 CZK / kus"),
                inStock = true,
            )
        )
    ),
    openingHours = OpeningHours.Message(
        message = "Po domluvě",
    )
)

val shop0reviews = listOf(
    Review(
        ReviewId.fromString("ef142e15-0e63-4e58-b8dc-2d05e00a47c7"),
        shop0.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("2c658b43-07f6-4b0e-aded-61a6595df277"),
        shop0.id,
        user2.id,
        Rating(1),
        "I didn't like it"
    ),
    Review(
        ReviewId.fromString("898f403f-3abb-4c98-a7bb-5ce2483e26b6"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("64e80795-45a7-4972-ad11-8cd1cb298307"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("d7b90437-f337-4f0b-9983-08f16f6dd70f"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("add3fb4d-e2d8-4364-8ef5-ddc0c585349f"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("7b4ad3ff-77d7-4bc8-8941-4a649886e9c9"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
    Review(
        ReviewId.fromString("61f14112-5f1a-4385-aa0c-b9dd34aeb56e"),
        shop0.id,
        user1.id,
        Rating(2),
        "ExampleReview"
    ),
)

val shop1 = Shop(
    id = ShopId.fromString("295d5f90-b45a-4df4-8785-fd2b6db4160e"),
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
    location = Location(49.209806, 16.598833),
    categories = listOf(
        ShopCategory(name = "Zelenina", color = RgbColor.fromArgb(0xFF067C06.toColorInt())),
        ShopCategory(name = "Ovoce", color = RgbColor.fromArgb(0xFFE4560B.toColorInt())),
        ShopCategory(name = "Maso", color = RgbColor.fromArgb(0xFF642000.toColorInt())),
        ShopCategory(name = "Mléčné výrobky", color = RgbColor.fromArgb(0xFFCFCECA.toColorInt())),
        ShopCategory(name = "Pečivo", color = RgbColor.fromArgb(0xFFCA6207.toColorInt())),
        ShopCategory(name = "Vejce", color = RgbColor.fromArgb(0xFFBE9329.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF0B5DE4.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF067C06.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFE4560B.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF642000.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFCFCECA.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFCA6207.toColorInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFBE9329.toColorInt())),
    ),
    menu = ProductMenu(listOf()),
    ownerId = user1.id,
    openingHours = OpeningHours.Message(""),
)

val shop1reviews = emptyList<Review>()

val shop2 = Shop(
    id = ShopId.fromString("88a35777-c323-416f-8de5-265392eb1227"),
    name = "Obchod s vejci",
    images = listOf(
        ImageResource("https://picsum.photos/400/300"),
        ImageResource("https://picsum.photos/200/400"),
        ImageResource("https://picsum.photos/600/100"),
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    location = Location(49.205778, 16.593361),
    categories = listOf(
        ShopCategory(name = "Vejce", color = RgbColor.fromArgb(0xFFBE9329.toColorInt())),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Vejce",
                price = PriceLabel("30 CZK / 10 ks"),
                description = "Otevřený výběh slepic",
                inStock = true,
            ),
        )
    ),
    ownerId = user2.id,
    openingHours = OpeningHours.Time(
        mapOf(
            DayOfWeek.MONDAY to "08:00 - 11:00, 13:00 - 17:00",
            DayOfWeek.TUESDAY to "16:00 - 19:00",
            DayOfWeek.WEDNESDAY to "zavřeno",
            DayOfWeek.THURSDAY to "zavřeno",
            DayOfWeek.FRIDAY to "po domluvě",
            DayOfWeek.SATURDAY to "09:00 - 12:00",
            DayOfWeek.SUNDAY to "zavřeno",
        )
    ),
)

val shop2reviews = listOf(
    Review(
        ReviewId.fromString("37d40e5f-fa17-4c96-8fb6-4a8b38324403"),
        shop2.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("578c8b8c-6dac-452a-a901-de0fbdf0cf37"),
        shop2.id,
        user1.id,
        Rating(2),
        "Nuh, average"
    ),
)

val shop3 = Shop(
    id = ShopId.fromString("7892234a-f7dd-40dc-a528-7eec23e90589"),
    name = "Obchod s masem a mnohem více, především s extra dlouhým názvem, který jen tak nekončí, takže se musíme připravit na to, že se nám to nevejde do komponent a může nadělat paseku, když si na to nedáme pozor",
    images = listOf(
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    location = Location(49.205778, 16.59),
    categories = listOf(
        ShopCategory(name = "Maso", color = RgbColor.fromArgb(0xFF642000.toColorInt())),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Vepřové maso",
                description = "Z domácího chovu",
                price = PriceLabel("200 CZK / kg"),
                inStock = false,
            ),
        )
    ),
    ownerId = user2.id,
    openingHours = OpeningHours.Message(
        message = "Po domluvě, telefon nebo sms",
    )
)

val shop3reviews = listOf(
    Review(
        ReviewId.fromString("59a0d8c9-4257-4407-9674-8689fe2ca903"),
        shop3.id,
        user0.id,
        Rating(5),
        "Amazing"
    ),
    Review(
        ReviewId.fromString("f83e60d3-3296-4d90-ba6b-c424e890f00b"),
        shop3.id,
        user1.id,
        Rating(2),
        "Nuh, average"
    ),
)

val sampleShops: List<Shop> by lazy {
    listOf(shop0, shop1, shop2, shop3)
}

val sampleReviews: List<Review> by lazy {
    listOf(shop0reviews, shop1reviews, shop2reviews, shop3reviews).flatten()
}
val sampleReviewsUI: List<ReviewUiState> = sampleReviews.mapNotNull { review ->
    val author = sampleUsers.find { it.id == review.userId } ?: return@mapNotNull null
    review.toUiState(author)
}