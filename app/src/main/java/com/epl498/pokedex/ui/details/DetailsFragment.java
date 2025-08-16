package com.epl498.pokedex.ui.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.epl498.pokedex.R;
import com.squareup.picasso.Picasso;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class DetailsFragment extends Fragment {

    private ImageView pokemonImageFront;
    private ImageView pokemonImageBack;
    private TextView pokemonName;
    private TextView pokemonId;
    private TextView pokemonTypes;
    private TextView pokemonWeight;
    private TextView pokemonHeight;
    private TextView pokemonBaseExperience;
    private TextView pokemonAbilities;
    private TextView pokemonHeldItems;
    private TextView pokemonMoves;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        pokemonImageFront = view.findViewById(R.id.pokemon_image_front);
        pokemonImageBack = view.findViewById(R.id.pokemon_image_back);
        pokemonName = view.findViewById(R.id.pokemon_name);
        pokemonId = view.findViewById(R.id.pokemon_id);
        pokemonTypes = view.findViewById(R.id.pokemon_types);
        pokemonWeight = view.findViewById(R.id.pokemon_weight);
        pokemonHeight = view.findViewById(R.id.pokemon_height);
        pokemonBaseExperience = view.findViewById(R.id.pokemon_base_experience);
        pokemonAbilities = view.findViewById(R.id.pokemon_abilities);
        pokemonHeldItems = view.findViewById(R.id.pokemon_held_items);
        pokemonMoves = view.findViewById(R.id.pokemon_moves);

        Bundle args = getArguments();
        if (args != null) {
            String frontGifUrl = args.getString("frontGifUrl");
            String frontShinyGifUrl = args.getString("frontShinyGifUrl");
            String backGifUrl = args.getString("backGifUrl");
            String backShinyGifUrl = args.getString("backShinyGifUrl");

            // Load default GIFs initially
            Glide.with(this).asGif().load(frontGifUrl).into(pokemonImageFront);
            Glide.with(this).asGif().load(backGifUrl).into(pokemonImageBack);

            // Set Pokémon name
            pokemonName.setText(args.getString("name", "No Name"));

            // Set Pokémon ID
            pokemonId.setText(String.format("#%03d", args.getInt("id", 0)));

            // Set Types (check for null or empty list)
            String types = args.getString("types");
            if (types == null || types.isEmpty()) {
                pokemonTypes.setText("No Types");
            } else {
                pokemonTypes.setText("Types: " + types);
            }

            // Set Weight
            int weight = args.getInt("weight", -1);
            if (weight == -1) {
                pokemonWeight.setText("No Weight Info");
            } else {
                pokemonWeight.setText("Weight: " + weight);
            }

            // Set Height
            int height = args.getInt("height", -1);
            if (height == -1) {
                pokemonHeight.setText("No Height Info");
            } else {
                pokemonHeight.setText("Height: " + height);
            }

            // Set Base Experience
            int baseExperience = args.getInt("baseExperience", -1);
            if (baseExperience == -1) {
                pokemonBaseExperience.setText("No Base Experience Info");
            } else {
                pokemonBaseExperience.setText("Base Experience: " + baseExperience);
            }

            // Set Abilities (check for null or empty list)
            String abilities = args.getString("abilities");
            if (abilities == null || abilities.isEmpty()) {
                pokemonAbilities.setText("No Abilities");
            } else {
                pokemonAbilities.setText("Abilities: " + abilities);
            }

            // Set Held Items (check for null or empty list)
            String heldItems = args.getString("heldItems");
            if (heldItems == null || heldItems.isEmpty()) {
                pokemonHeldItems.setText("No Held Items");
            } else {
                pokemonHeldItems.setText("Held Items: " + heldItems);
            }

            // Set Moves (check for null or empty list)
            String moves = args.getString("moves");
            if (moves == null || moves.isEmpty()) {
                pokemonMoves.setText("No Moves");
            } else {
                pokemonMoves.setText("Moves: " + moves);
            }

            // Handle tab switching for Default and Shiny images
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) { // Default
                        Glide.with(DetailsFragment.this).asGif().load(frontGifUrl).into(pokemonImageFront);
                        Glide.with(DetailsFragment.this).asGif().load(backGifUrl).into(pokemonImageBack);
                    } else if (tab.getPosition() == 1) { // Shiny
                        Glide.with(DetailsFragment.this).asGif().load(frontShinyGifUrl).into(pokemonImageFront);
                        Glide.with(DetailsFragment.this).asGif().load(backShinyGifUrl).into(pokemonImageBack);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        return view;
    }
}