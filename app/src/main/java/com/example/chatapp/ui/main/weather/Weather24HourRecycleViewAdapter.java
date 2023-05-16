package com.example.chatapp.ui.main.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;

import com.example.chatapp.databinding.FragmentWeather24HourCardItemBinding;

import java.util.ArrayList;

/**
 * Recycleview adapter for 24 hour weather cards
 * @author Xavier Hines
 */
public class Weather24HourRecycleViewAdapter extends RecyclerView.Adapter<Weather24HourRecycleViewAdapter.Weather24HourViewHolder> {
    /**
     * An arraylist containing 24 hours worth of weather cards
     */
    private final ArrayList<Weather24HourCardItem> mWeather24HourCardFragments;

    public Weather24HourRecycleViewAdapter(ArrayList<Weather24HourCardItem> weather24HourCardFragments) {
        this.mWeather24HourCardFragments = weather24HourCardFragments;
    }

    @NonNull
    @Override
    public Weather24HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Weather24HourViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather24_hour_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Weather24HourViewHolder holder, int position) {
        //TODO images and text for conditions
        holder.mBinding.idCardTime.setText(mWeather24HourCardFragments.get(position).getmTime());
        holder.mBinding.idCardTemperature.setText(mWeather24HourCardFragments.get(position).getmTemperature());
        holder.mBinding.idCardConditionIcon.setImageDrawable(mWeather24HourCardFragments.get(position).getmIcon());
        holder.mBinding.idCardPrecipitation.setText(mWeather24HourCardFragments.get(position).getmPrecipitation());

    }

    @Override
    public int getItemCount() {
        return mWeather24HourCardFragments.size();
    }

    public static class Weather24HourViewHolder extends RecyclerView.ViewHolder{
        public @NonNull FragmentWeather24HourCardItemBinding mBinding;
        public Weather24HourViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentWeather24HourCardItemBinding.bind(itemView);
        }
    }
}
