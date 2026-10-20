# Rush Hour NxM – parancssoros játék

Programozási technológiák (BPI1116/BPI1113/BMI1303) beadandó, Nyíregyházi Egyetem.

Kétszemélyes, csúszó-blokk táblajáték Java 21-ben, Maven projektként.
A humán játékos az **A** céljárművet vezeti a tábla **jobb** szélén lévő kijárathoz (`>`),
a gépi játékos a **B** céljárművet a **bal** szélen lévő kijárathoz (`<`).

## Fordítás és futtatás

```bash
mvn clean install
java -jar target/rush-hour-1.0.0-jar-with-dependencies.jar            # board.txt-ből olvas
java -jar target/rush-hour-1.0.0-jar-with-dependencies.jar sajat.txt  # saját pálya
```

Ha a bemeneti fájl hiányzik vagy hibás, a játék a beégetett 6x12-es alapértelmezett pályán indul.

## Parancsok

| Parancs | Jelentés |
|---|---|
| `A R 2` | az A jármű 2 mezőt jobbra csúszik (irány: `U`/`D`/`L`/`R` vagy `up`/`down`/`left`/`right`) |
| `C down` | a C jármű 1 mezőt lefelé |
| `save [fájl]` | az aktuális állás mentése szövegfájlba (alapértelmezés: `savegame.txt`) |
| `load [fájl]` | pálya / állás betöltése szövegfájlból |
| `scores` | high score táblázat (adatbázisból) |
| `help`, `quit` | súgó, kilépés |

## Pályafájl formátuma

Minden sor a tábla egy sora, `.` = üres mező, betű = jármű (az azonos betűk egy járművet alkotnak).
`#`-tel kezdődő sor megjegyzés. Az orientációt a program a cellák elhelyezkedéséből határozza meg.

```
..CCC...D..F
.E......D..F
.EAA..G.D...
..H...G.BB..
..H..III....
JJ.....KK...
```

## Felépítés (package-ek)

| Package | Tartalom |
|---|---|
| `config` | `GameConfig` – beállítások (**Singleton** minta) |
| `event` | `GameEventPublisher`, `GameEventListener`, `AuditLoggerListener`, `MoveCounter` (**Observer** minta) |
| `model` | immutable value objectek: `Position`, `Vehicle`, `Board`, `Move`, `Direction`, `Orientation`, `PlayerType`, `HighScore` |
| `service` | üzleti logika: `MoveService` (lépés ellenőrzése/végrehajtása), `WinChecker`, `AiPlayer` |
| `persistence` | fájlkezelés (`BoardTextFormat`, `BoardFileRepository`, `DefaultBoard`) és JDBC (`JdbcHighScoreRepository`, H2) |
| `ui` | parancssor: `CommandParser`, `BoardRenderer`, `Game` (`start()` metódus) |

## Tervezési minták

- **Singleton** – `config.GameConfig`: egyetlen példány (initialization-on-demand holder idiom).
- **Observer** – `event.GameEventPublisher`: a `Game` eseményeket publikál (lépés, hibás lépés, mentés, győzelem), a feliratkozott listenerek (naplózás, lépésszámláló) reagálnak rá. Laza csatolás: a `Game` nem ismeri a listenereket.
- **Builder** – `Board.builder().rows(..).columns(..).vehicle(..).build()`: a tábla lépésenkénti felépítése, egyszeri validálással.
- **Immutable Value Object** – `Position`, `Vehicle`, `Board`, `Move`: minden módosítás új objektumot ad vissza.
- **Repository** – `HighScoreRepository` interfész és `JdbcHighScoreRepository` implementáció.
- **Dependency Injection (konstruktoron át)** – a `Game` minden függőségét kívülről kapja, így Mockitóval tesztelhető.

## Maven pluginek és függőségek

- `maven-jar-plugin` – belépési pont (`hu.nye.rushhour.Main`)
- `maven-assembly-plugin` – futtatható JAR a függőségekkel
- `jacoco-maven-plugin` – lefedettség mérése, a build 80% alatt elbukik
- `maven-checkstyle-plugin` – kódstílus ellenőrzése (`checkstyle.xml`)
- Függőségek: JUnit 5, Mockito, Logback, H2 (JDBC)

Jacoco riport: `target/site/jacoco/index.html`
