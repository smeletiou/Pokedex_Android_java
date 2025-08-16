package com.epl498.pokedex.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.epl498.pokedex.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_item, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getName());
        holder.pokemonType.setText(pokemon.getTypesString());
        holder.pokedexId.setText(String.format("#%03d", pokemon.getId()));

        // Load the regular front image for the HomeFragment
        Picasso.get().load(pokemon.getImageFront()).into(holder.pokemonImage);

        // Set click listener to navigate to DetailsFragment and pass all data
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("name", pokemon.getName());
            bundle.putInt("id", pokemon.getId());
            bundle.putString("types", pokemon.getTypesString());
            bundle.putString("abilities", String.join(", ", pokemon.getAbilities()));
            bundle.putString("heldItems", String.join(", ", pokemon.getHeldItems()));
            bundle.putString("moves", String.join(", ", pokemon.getMoves()));
            bundle.putInt("weight", pokemon.getWeight());
            bundle.putInt("height", pokemon.getHeight());
            bundle.putInt("baseExperience", pokemon.getBaseExperience());
            bundle.putString("imageFront", pokemon.getImageFront());
            bundle.putString("frontGifUrl", pokemon.getFrontGifUrl());
            bundle.putString("frontShinyGifUrl", pokemon.getFrontShinyGifUrl());
            bundle.putString("backGifUrl", pokemon.getBackGifUrl());
            bundle.putString("backShinyGifUrl", pokemon.getBackShinyGifUrl());

            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_detailsFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void updateList(List<Pokemon> newList) {
        this.pokemonList = newList;
        notifyDataSetChanged();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        ImageView pokemonImage;
        TextView pokemonName, pokemonType, pokedexId;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            pokemonImage = itemView.findViewById(R.id.pokemon_image);
            pokemonName = itemView.findViewById(R.id.pokemon_name);
            pokemonType = itemView.findViewById(R.id.pokemon_type);
            pokedexId = itemView.findViewById(R.id.pokemon_id);
        }
    }
}
