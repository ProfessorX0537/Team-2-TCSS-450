package com.example.chatapp.ui.main.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeatherLocationsCardItemBinding;

import java.util.ArrayList;

/**
 * Recycler view for weather location cards
 * @author Luca Smith
 */
public class WeatherLocationsRecyclerViewAdapter extends RecyclerView.Adapter<WeatherLocationsRecyclerViewAdapter.WeatherLocationsViewHolder> {

    /**
     * An arraylist containing location cards
     */
    private final ArrayList<WeatherLocationsCardItem> mWeatherLocationCardFragments;

    public WeatherLocationsRecyclerViewAdapter(ArrayList<WeatherLocationsCardItem> weatherLocationCardFragments) {
        this.mWeatherLocationCardFragments = weatherLocationCardFragments;
    }


    @NonNull
    @Override
    public WeatherLocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherLocationsViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_locations_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherLocationsViewHolder holder, int position) {
        //@TODO bind to stuff
        holder.mBinding.idCardZipcode.setText(mWeatherLocationCardFragments.get(position).getZipCode());
        holder.mBinding.idCardCity.setText(mWeatherLocationCardFragments.get(position).getCity());

    }

    @Override
    public int getItemCount() {
        return mWeatherLocationCardFragments.size();
    }

    public static class WeatherLocationsViewHolder extends RecyclerView.ViewHolder {
        public @NonNull FragmentWeatherLocationsCardItemBinding mBinding;
        public WeatherLocationsViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentWeatherLocationsCardItemBinding.bind(itemView);
        }
    }
}
