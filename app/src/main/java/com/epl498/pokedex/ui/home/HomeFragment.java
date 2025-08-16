package com.epl498.pokedex.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.epl498.pokedex.databinding.FragmentHomeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private TextView noDataMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up RecyclerView with LinearLayoutManager for a single-column layout
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Single column
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);

        // Set up noDataMessage TextView
        noDataMessage = binding.noDataMessage;

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Load Pokémon data
        loadPokemonData();

        // Handle Search functionality (using EditText)
        EditText searchBar = binding.searchBar;
        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterPokemon(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

        return root;
    }

    private void loadPokemonData() {
        CollectionReference pokemonsRef = firestore.collection("pokemons");
        pokemonsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                pokemonList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    int id = document.getLong("id").intValue();

                    // Retrieve list fields
                    List<String> types = (List<String>) document.get("types");
                    List<String> abilities = (List<String>) document.get("abilities");
                    List<String> heldItems = (List<String>) document.get("heldItems");
                    List<String> moves = (List<String>) document.get("moves");

                    // Retrieve other attributes
                    int weight = document.getLong("weight").intValue();
                    int height = document.getLong("height").intValue();
                    int baseExperience = document.getLong("baseExperience").intValue();

                    // Retrieve image URLs
                    String imageFront = document.getString("ImageFront");
                    String frontGifUrl = document.getString("frontGifUrl");
                    String frontShinyGifUrl = document.getString("frontShinyGifUrl");
                    String backGifUrl = document.getString("backGifUrl");
                    String backShinyGifUrl = document.getString("backShinyGifUrl");

                    // Create Pokemon object with all necessary fields
                    Pokemon pokemon = new Pokemon(
                            name, id, types, abilities, heldItems, moves, weight, height,
                            baseExperience, imageFront, frontGifUrl, frontShinyGifUrl, backGifUrl, backShinyGifUrl
                    );

                    pokemonList.add(pokemon);
                }

                // Sort Pokémon by ID in ascending order
                Collections.sort(pokemonList, new Comparator<Pokemon>() {
                    @Override
                    public int compare(Pokemon p1, Pokemon p2) {
                        return Integer.compare(p1.getId(), p2.getId());
                    }
                });

                // Check if data is empty and show a message if true
                if (pokemonList.isEmpty()) {
                    noDataMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noDataMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e("HomeFragment", "Error fetching Pokémon data", task.getException());
            }
        });
    }

    private void filterPokemon(String query) {
        List<Pokemon> filteredList = new ArrayList<>();
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.getName().toLowerCase().contains(query.toLowerCase()) ||
                    String.valueOf(pokemon.getId()).contains(query)) {
                filteredList.add(pokemon);
            }
        }
        adapter.updateList(filteredList);

        // Show message if no results are found
        if (filteredList.isEmpty()) {
            noDataMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
