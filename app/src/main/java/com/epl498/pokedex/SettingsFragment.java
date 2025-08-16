package com.epl498.pokedex;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private Button downloadButton;
    private Button deleteButton;
    private EditText limitInput;
    private FirebaseFirestore firestore;
    private static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize buttons, input field, and Firestore
        downloadButton = view.findViewById(R.id.button_download);
        deleteButton = view.findViewById(R.id.button_delete);
        limitInput = view.findViewById(R.id.input_limit);
        firestore = FirebaseFirestore.getInstance();

        // Set up button click listeners
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPokemonData();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePokemonData();
            }
        });

        return view;
    }

    private void downloadPokemonData() {
        Toast.makeText(getActivity(), "Downloading Pokémon data...", Toast.LENGTH_SHORT).show();

        // Get the limit value from EditText; default to 20 if empty
        String limitText = limitInput.getText().toString();
        int limit = limitText.isEmpty() ? 20 : Integer.parseInt(limitText);

        // Start new thread for network operation
        new Thread(() -> {
            try {
                // Fetch list of Pokémon with custom limit
                URL url = new URL("https://pokeapi.co/api/v2/pokemon?limit=" + limit + "&offset=0");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray pokemonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < pokemonArray.length(); i++) {
                    JSONObject pokemonObject = pokemonArray.getJSONObject(i);
                    String pokemonName = pokemonObject.getString("name");
                    fetchAndSavePokemonDetails(pokemonName);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error downloading Pokémon data", e);
            }
        }).start();
    }

    private void fetchAndSavePokemonDetails(String pokemonName) {
        try {
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemonName);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONObject pokemonData = new JSONObject(result.toString());

            // Basic data
            String name = pokemonData.getString("name");
            int id = pokemonData.getInt("id");
            String frontImageUrl = pokemonData.getJSONObject("sprites").getString("front_default");

            // Showdown GIF URLs
            JSONObject showdownSprites = pokemonData.getJSONObject("sprites")
                    .getJSONObject("other")
                    .getJSONObject("showdown");

            String frontGifUrl = showdownSprites.optString("front_default", null);
            String frontShinyGifUrl = showdownSprites.optString("front_shiny", null);
            String backGifUrl = showdownSprites.optString("back_default", null);
            String backShinyGifUrl = showdownSprites.optString("back_shiny", null);

            // Types
            JSONArray typesArray = pokemonData.getJSONArray("types");
            List<String> types = new ArrayList<>();
            for (int i = 0; i < typesArray.length(); i++) {
                String type = typesArray.getJSONObject(i).getJSONObject("type").getString("name");
                types.add(type);
            }

            // Abilities
            JSONArray abilitiesArray = pokemonData.getJSONArray("abilities");
            List<String> abilities = new ArrayList<>();
            for (int i = 0; i < abilitiesArray.length(); i++) {
                String ability = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name");
                abilities.add(ability);
            }

            // Held items
            JSONArray heldItemsArray = pokemonData.getJSONArray("held_items");
            List<String> heldItems = new ArrayList<>();
            for (int i = 0; i < heldItemsArray.length(); i++) {
                String item = heldItemsArray.getJSONObject(i).getJSONObject("item").getString("name");
                heldItems.add(item);
            }

            // Moves
            JSONArray movesArray = pokemonData.getJSONArray("moves");
            List<String> moves = new ArrayList<>();
            for (int i = 0; i < movesArray.length(); i++) {
                String move = movesArray.getJSONObject(i).getJSONObject("move").getString("name");
                moves.add(move);
            }

            // Other attributes
            int weight = pokemonData.optInt("weight", 0);
            int height = pokemonData.optInt("height", 0);
            int baseExperience = pokemonData.optInt("base_experience", 0);

            // Create a map to store all the data in Firestore
            Map<String, Object> pokemonMap = new HashMap<>();
            pokemonMap.put("name", name);
            pokemonMap.put("id", id);
            pokemonMap.put("types", types);
            pokemonMap.put("abilities", abilities);
            pokemonMap.put("heldItems", heldItems);
            pokemonMap.put("moves", moves);
            pokemonMap.put("weight", weight);
            pokemonMap.put("height", height);
            pokemonMap.put("baseExperience", baseExperience);

            // Store URLs
            pokemonMap.put("ImageFront", frontImageUrl);
            pokemonMap.put("frontGifUrl", frontGifUrl);
            pokemonMap.put("frontShinyGifUrl", frontShinyGifUrl);
            pokemonMap.put("backGifUrl", backGifUrl);
            pokemonMap.put("backShinyGifUrl", backShinyGifUrl);

            // Save data to Firestore
            CollectionReference pokemonsCollection = firestore.collection("pokemons");
            pokemonsCollection.document(name).set(pokemonMap)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully added " + name))
                    .addOnFailureListener(e -> Log.e(TAG, "Error adding Pokémon data", e));

        } catch (Exception e) {
            Log.e(TAG, "Error fetching and saving Pokémon details", e);
        }
    }


    private void deletePokemonData() {
        CollectionReference pokemonsCollection = firestore.collection("pokemons");
        pokemonsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Loop through and delete each document in the collection
                task.getResult().forEach(documentSnapshot -> {
                    pokemonsCollection.document(documentSnapshot.getId()).delete()
                            .addOnSuccessListener(aVoid -> Log.d(TAG, "Deleted Pokémon: " + documentSnapshot.getId()))
                            .addOnFailureListener(e -> Log.e(TAG, "Error deleting Pokémon data", e));
                });
                Toast.makeText(getActivity(), "Deleted all Pokémon data", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Error fetching Pokémon data for deletion", task.getException());
            }
        });
    }
}
