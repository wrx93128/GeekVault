# GeekVault

Aplikacja mobilna na Androida będąca katalogiem postaci z serialu **Rick & Morty**. Pozwala przeglądać postacie pobierane z publicznego REST API, dodawać je do lokalnych ulubionych z własną notatką oraz wyświetla ekran kontaktowy z mapą i przyciskami akcji systemowych.

---

## Wymagania

- **Android Studio** Hedgehog lub nowszy
- **Android SDK** — minSdk 31 (Android 12), targetSdk 36
- **Połączenie z internetem** przy pierwszym uruchomieniu (lista postaci)
- Plik `local.properties` z kluczem API do Map Google:
  ```
  MAPS_API_KEY=TWOJ_KLUCZ
  ```

---

## Uruchomienie

```bash
git clone https://github.com/wrx93128/GeekVault.git
cd GeekVault
```

1. Otwórz projekt w Android Studio (`File → Open`)
2. Poczekaj na synchronizację Gradle (`Sync Now`)
3. Uruchom na emulatorze lub fizycznym urządzeniu z API 31+

Alternatywnie z wiersza poleceń:
```bash
./gradlew assembleDebug
```

---

## Architektura

Projekt stosuje wzorzec **MVVM** (Model–View–ViewModel) z Jetpack Compose jako warstwą UI.

```
com.example.geekvault
├── data/
│   ├── model/          ← Data classy API (Character, ApiResponse)
│   ├── remote/         ← Retrofit: RickAndMortyApi, RetrofitInstance
│   ├── AppDatabase.kt  ← Room — singleton bazy lokalnej
│   ├── FavoriteCharacter.kt  ← Entity Room
│   └── FavoriteDao.kt  ← DAO: insert, delete, updateNote, getAll
├── navigation/
│   ├── AppDestinations.kt  ← Stałe tras nawigacji
│   └── AppNavigation.kt    ← NavHost z całym grafem nawigacji
└── ui/
    ├── auth/           ← LoginScreen, RegisterScreen, AuthViewModel
    ├── home/           ← HomeScreen, HomeViewModel
    ├── favorites/      ← FavoritesScreen, FavoritesViewModel
    ├── contact/        ← ContactScreen
    ├── main/           ← MainScaffold, BottomNavBar, AppDrawer
    └── theme/          ← Paleta kolorów, typografia, motyw
```

**Źródła danych:**
- `Retrofit` — pobieranie listy postaci z [Rick and Morty API](https://rickandmortyapi.com/) (tylko odczyt)
- `Room` — lokalna baza SQLite dla ulubionych (zapis, odczyt, edycja, usuwanie)
- `Firebase Auth` — uwierzytelnianie email + hasło

---

## Funkcjonalności

### Uwierzytelnianie (Firebase Auth)
- Ekran logowania i rejestracji z walidacją (zgodność haseł)
- Po zalogowaniu użytkownik trafia na ekran główny; po wylogowaniu wraca do ekranu logowania z wyczyszczonym back stackiem
- E-mail zalogowanego użytkownika widoczny w nagłówku szuflady nawigacyjnej

### Nawigacja
- **Single Activity** z Compose Navigation
- **Bottom Navigation Bar** — 3 zakładki: Dom, Ulubione, Profil
- **Navigation Drawer** — dostęp do wszystkich sekcji + przycisk Wyloguj

### Ekran główny — lista postaci
- Pobieranie danych z `https://rickandmortyapi.com/api/character`
- Stany UI: ładowanie (spinner), sukces (lista), błąd (komunikat + przycisk "Odśwież")
- Każda karta: zdjęcie, imię, status, gatunek oraz ikona serduszka
- **Tryb offline** — brak sieci = komunikat błędu z możliwością ponowienia
- Ikona serduszka jest wypełniona dla postaci już dodanych do ulubionych (stan zachowany po zmianie ekranu)

### Ulubione (Room — offline)
- Dodawanie postaci do ulubionych przez kliknięcie serduszka na liście
- Ponowne kliknięcie serduszka przy już ulubionej postaci → dialog potwierdzenia usunięcia ("Tak" / "Nie")
- Zakładka Ulubione: lista zapisanych postaci ze zdjęciem, nazwą i edytowalną notatką
- Notatka zapisywana przyciskiem lub po utracie focusu
- Usuwanie postaci z ulubionych ikoną kosza
- Działa w pełni **bez dostępu do internetu**

### Ekran kontaktowy
- Mapa Google z markerem „Nasze biuro" (Warszawa Centrum)
- Przycisk „Moja lokalizacja" — prosi o uprawnienia GPS i centruje mapę na użytkowniku
- Przyciski akcji systemowych (Implicit Intents):
  - **Zadzwoń do nas** — otwiera dialer z numerem `+48 123 456 789`
  - **Strona WWW** — otwiera przeglądarkę
  - **Nawiguj** — otwiera Google Maps z trasą do Warszawy
- Obsługa `ActivityNotFoundException` gdy brak odpowiedniej aplikacji

---

## Stos technologiczny

| Biblioteka | Wersja | Zastosowanie |
|---|---|---|
| Jetpack Compose + Material 3 | BOM 2025.03.00 | UI |
| Compose Navigation | 2.9.0 | Nawigacja |
| Firebase Authentication | BOM 33.10.0 | Logowanie |
| Room | 2.7.0 | Lokalna baza danych |
| Retrofit + Gson | 2.9.0 | REST API |
| Coil | 2.7.0 | Ładowanie obrazków |
| Maps Compose | 8.2.2 | Google Maps |
| Accompanist Permissions | 0.37.3 | Uprawnienia runtime |
| Play Services Location | 21.3.0 | GPS / lokalizacja |

---

## Podział pracy

| Osoba | Obszar |
|---|---|
| Osoba 1 | Konfiguracja projektu, Firebase Auth, szkielet nawigacji (NavHost, BottomNav, Drawer) |
| Osoba 2 | Warstwa sieciowa Retrofit, ekran główny (HomeScreen + HomeViewModel) |
| Osoba 3 | Room DB, ekran Ulubionych, ekran Kontakt z mapą i Implicit Intents |

---

## Stabilność

- **Obrót ekranu / zabicie procesu** — ViewModels + Room jako źródło prawdy; stan aplikacji przetrwa obie sytuacje
- **Tryb offline** — zakładka Ulubione działa bez sieci; Home informuje o błędzie z przyciskiem "Odśwież"
- **Puste stany** — lista ulubionych wyświetla komunikat gdy jest pusta; ekran Home obsługuje błędy API
