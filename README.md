**Název pro práci:**

- "Vývoj aplikace pro propagaci prodeje domácích surovin pomocí Kotlin Multiplatform"

**Tech:**

- KMP (Kotlin multiplatform), Jetpack Compose, SwiftUI
- Firebase

**Zaměření práce**:

- návrh aplikace
- UX design
- architektura softwaru
- cloud-based architecture, BaaS (Backend as a Service)
- multiplatformní vývoj (Android, IOS)

**Motivace**:

- Možná využití: med/včelařství, brambory, zelenina, maso, vinařství
- V ohledu včelařství: aktuálně pouze svoz na centrální místo, výkup za relativně nízké ceny a
  přeprodej do obchodů
- Prodej zeleniny apod na vesnicích: často jen cedule s nápisem

**Funkční požadavky**

- každý uživatel může být jak zákazník, tak prodejce
- uživatel (prodejce) může vytvořit libovolný počet obchodů
- uživatel (zákazník) si může obchody zobrazit na mapě nebo seznamu
    - mapa - Google maps SDK, body na mapě
    - seznam - řazení podle vzdálenosti, názvu apod.
    - přepnutí mezi zobrazení
- každý obchod má název, popis, otevírací dobu (pravidelná/po domluvě), kontakt, nabídku (tabulka -
  název, cena, foto, počet na skladě?

**Nefunkční požadavky**:

- při zapnutí aplikace se zobrazí úvodní obrazovka pro představení a popis (pro snadnou orientaci)
- aplikace bude využívat cloudové služby: Firebase Auth, Firebase Firestore, ...

**Use Case Diagram**:

![Use Case](docs/diagram-img/use_case_diagram.svg)

**Class Diagram**:

_TODO_
