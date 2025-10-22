package com.tondracek.myfarmer.shop.data

import androidx.compose.ui.graphics.Color
import com.tondracek.myfarmer.common.ImageResource
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.productmenu.domain.model.ProductPrice
import com.tondracek.myfarmer.review.domain.model.Review
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.shoplocation.domain.model.ShopLocation
import com.tondracek.myfarmer.systemuser.data.user0
import com.tondracek.myfarmer.systemuser.data.user1
import com.tondracek.myfarmer.systemuser.data.user2
import java.time.DayOfWeek
import java.util.UUID

val shop0 = Shop(
    id = UUID.fromString("295d5f90-b45a-4df4-8785-fd2b6db4160e"),
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
        ShopCategory("Zelenina", Color(0xFF067C06)),
        ShopCategory("Ovoce", Color(0xFFE4560B)),
        ShopCategory("Maso", Color(0xFF642000)),
        ShopCategory("Mléčné výrobky", Color(0xFFCFCECA)),
        ShopCategory("Pečivo", Color(0xFFCA6207)),
        ShopCategory("Vejce", Color(0xFFBE9329)),
        ShopCategory("Další", Color(0xFF0B5DE4)),
        ShopCategory("Další", Color(0xFF067C06)),
        ShopCategory("Další", Color(0xFFE4560B)),
        ShopCategory("Další", Color(0xFF642000)),
        ShopCategory("Další", Color(0xFFCFCECA)),
        ShopCategory("Další", Color(0xFFCA6207)),
        ShopCategory("Další", Color(0xFFBE9329)),
    ),
    reviews = emptyList(),
    menu = ProductMenu(listOf()),
    owner = user0,
    openingHours = OpeningHours.Message(""),
)

val shop1 = Shop(
    id = UUID.fromString("ded3f207-09d3-47e7-94d0-1f48cb10ef12"),
    name = "Domácí včelařství v Jundrově",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    owner = user1,
    images = listOf(
        ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Beekeeper_with_moveable_comb_hive.jpg/1024px-Beekeeper_with_moveable_comb_hive.jpg"),
        ImageResource("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Western_honey_bee_on_a_honeycomb.jpg/1280px-Western_honey_bee_on_a_honeycomb.jpg"),
        ImageResource("https://justbeehoney.co.uk/cdn/shop/articles/104740553_l2_5000x.jpg")
    ),
    location = ShopLocation(49.209166, 16.556608),
    categories = listOf(
        ShopCategory("Med", Color(0xFFFFC107)),
        ShopCategory("Včelařství", Color(0xFFFF9800)),
    ),
    reviews = listOf(
        Review(user0, 5, "Amazing"),
        Review(user2, 4, "Very good"),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Med",
                description = "Kvalitní med z naší domácí produkce.",
                price = ProductPrice("150 CZK / kg"),
                inStock = true,
            )
        )
    ),
    openingHours = OpeningHours.Message(
        message = "Po domluvě",
    )
)

val shop2 = Shop(
    id = UUID.fromString("88a35777-c323-416f-8de5-265392eb1227"),
    name = "Obchod s vejci",
    images = listOf(
        ImageResource("https://picsum.photos/400/300"),
        ImageResource("https://picsum.photos/200/400"),
        ImageResource("https://picsum.photos/600/100"),
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    location = ShopLocation(49.205778, 16.593361),
    categories = listOf(
        ShopCategory("Vejce", Color(0xFFBE9329)),
    ),
    reviews = listOf(
        Review(user0, 5, "Amazing"),
        Review(user1, 2, "Nuh, average"),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Vejce",
                price = ProductPrice("30 CZK / 10 ks"),
                description = "Otevřený výběh slepic",
                inStock = true,
            ),
        )
    ),
    owner = user2,
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

val shop3 = Shop(
    id = UUID.fromString("7892234a-f7dd-40dc-a528-7eec23e90589"),
    name = "Obchod s masem a mnohem více, především s extra dlouhým názvem, který jen tak nekončí, takže se musíme připravit na to, že se nám to nevejde do komponent a může nadělat paseku, když si na to nedáme pozor",
    images = listOf(
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    location = ShopLocation(49.205778, 16.59),
    categories = listOf(
        ShopCategory("Maso", Color(0xFF642000)),
    ),
    reviews = listOf(
        Review(user0, 5, "Amazing"),
        Review(user1, 2, "Nuh, average"),
    ),
    menu = ProductMenu(
        listOf(
            MenuItem(
                name = "Vepřové maso",
                description = "Z domácího chovu",
                price = ProductPrice("200 CZK / kg"),
                inStock = false,
            ),
        )
    ),
    owner = user2,
    openingHours = OpeningHours.Message(
        message = "Po domluvě, telefon nebo sms",
    )
)

val sampleShops: List<Shop> by lazy {
    listOf(shop0, shop1, shop2, shop3)
}