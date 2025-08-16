# Android Pokedex App

This is a lightweight Pokédex Android application . It allows users to search, download, and view detailed information about Pokémon using data fetched from the [PokeAPI](https://pokeapi.co/), with local storage via Firebase Firestore.

## 📱 Features

### 🔍 Pokedex Home Page
- Displays a list of all downloaded Pokémon.
- Includes a search bar that supports lookup by name or ID.
- Pokémon cards show their ID, name, type, and front sprite.
- Cards use a Pokéball XML background (sourced from GitHub).

<img width="544" height="391" alt="Pokedex Home" src="https://github.com/user-attachments/assets/ff31da20-bd6b-4882-a52d-ebfe2cb212a4" />

### 📄 Details Page
- Accessed by selecting a Pokémon from the list.
- Displays:
  - ID, Name, Type, Weight, Height, Base Experience
  - Abilities, Held Items (or a "No held items" message), and Moves
- Includes two tabs to toggle between **Normal** and **Shiny** versions using animated `.gif` sprites.

<img width="620" height="434" alt="Settings Page" src="https://github.com/user-attachments/assets/1770152d-7f13-4ebf-bc39-8eacfbb8e32b" />


### ⚙️ Settings Page
- Download Pokémon data from the PokeAPI.
- Choose how many Pokémon to fetch (default is 20 if left blank).
- Option to delete the Pokémon list from Firestore.
- Automatically sorts by ascending Pokémon ID.

<img width="373" height="401" alt="Pokemon Details" src="https://github.com/user-attachments/assets/470bbbad-fd13-4009-871c-769a77cc53d5" />

### ℹ️ About Us Page
- Displays brief info about the app and its purpose.

## 🔄 Data Handling


- Data is fetched using the endpoint:
https://pokeapi.co/api/v2/pokemon?limit={limit}&offset=0



- For each Pokémon, a second API call retrieves full details.
- Stored fields:
- `Id`, `Name`, `Types`, `Abilities`, `Held Items`, `Moves`, `Weight`, `Height`, `Base Experience`
- Animated GIFs:
  - `Front Default`
  - `Back Default`
  - `Front Shiny Default`
  - `Back Shiny Default`

- Pokémon data is stored in Firebase Firestore, using the Pokémon’s name as the document ID.

## 🧱 Structure

- `Pokemon.java` — Model class with constructors, getters, and setters.
- `PokemonAdaptor.java` — Adapter for rendering the list and passing data from home to details.
- `MainActivity.java`, `DetailsActivity.java`, etc. — Handle UI logic.

## 🔧 Tech Stack

- Java (Android)
- Firebase Firestore
- PokeAPI
- XML-based UI components

## 🤓 Author

Sotiris  Meletiou  

