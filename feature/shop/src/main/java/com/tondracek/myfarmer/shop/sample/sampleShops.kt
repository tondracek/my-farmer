package com.tondracek.myfarmer.shop.sample

import com.tondracek.myfarmer.common.color.RgbColor
import com.tondracek.myfarmer.common.color.fromArgb
import com.tondracek.myfarmer.image.model.ImageResource
import com.tondracek.myfarmer.location.domain.model.Location
import com.tondracek.myfarmer.openinghours.domain.model.OpeningHours
import com.tondracek.myfarmer.productmenu.domain.model.MenuItem
import com.tondracek.myfarmer.productmenu.domain.model.PriceLabel
import com.tondracek.myfarmer.productmenu.domain.model.ProductMenu
import com.tondracek.myfarmer.shop.domain.model.Shop
import com.tondracek.myfarmer.shop.domain.model.ShopId
import com.tondracek.myfarmer.shopcategory.domain.model.ShopCategory
import com.tondracek.myfarmer.user.sample.user0
import com.tondracek.myfarmer.user.sample.user1
import com.tondracek.myfarmer.user.sample.user2
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
        ShopCategory(name = "Med", color = RgbColor.fromArgb(0xFFFFC107.toInt())),
        ShopCategory(name = "Včelařství", color = RgbColor.fromArgb(0xFFFF9800.toInt())),
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
        ShopCategory(name = "Zelenina", color = RgbColor.fromArgb(0xFF067C06.toInt())),
        ShopCategory(name = "Ovoce", color = RgbColor.fromArgb(0xFFE4560B.toInt())),
        ShopCategory(name = "Maso", color = RgbColor.fromArgb(0xFF642000.toInt())),
        ShopCategory(name = "Mléčné výrobky", color = RgbColor.fromArgb(0xFFCFCECA.toInt())),
        ShopCategory(name = "Pečivo", color = RgbColor.fromArgb(0xFFCA6207.toInt())),
        ShopCategory(name = "Vejce", color = RgbColor.fromArgb(0xFFBE9329.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF0B5DE4.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF067C06.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFE4560B.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFF642000.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFCFCECA.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFCA6207.toInt())),
        ShopCategory(name = "Další", color = RgbColor.fromArgb(0xFFBE9329.toInt())),
    ),
    menu = ProductMenu(listOf()),
    ownerId = user1.id,
    openingHours = OpeningHours.Message(""),
)

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
        ShopCategory(name = "Vejce", color = RgbColor.fromArgb(0xFFBE9329.toInt())),
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


val shop3 = Shop(
    id = ShopId.fromString("7892234a-f7dd-40dc-a528-7eec23e90589"),
    name = "Obchod s masem a mnohem více, především s extra dlouhým názvem, který jen tak nekončí, takže se musíme připravit na to, že se nám to nevejde do komponent a může nadělat paseku, když si na to nedáme pozor",
    images = listOf(
    ),
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nisl quis aliquam ultricies, nunc nisl ultrices odio, vitae aliquam nunc nisl vitae nunc.",
    location = Location(49.205778, 16.59),
    categories = listOf(
        ShopCategory(name = "Maso", color = RgbColor.fromArgb(0xFF642000.toInt())),
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

val sampleShops: List<Shop> by lazy {
    listOf(shop0, shop1, shop2, shop3)
}
