package com.example.chatapp.ui.main.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeather10DayBinding;
import com.example.chatapp.databinding.FragmentWeather10DayCardItemBinding;

import java.util.ArrayList;

public class Weather10DayRecycleViewAdapter extends RecyclerView.Adapter<Weather10DayRecycleViewAdapter.Weather10DayViewHolder> {
    private final ArrayList<Weather10DayCardItem> mWeather10DayCardItems;

    public Weather10DayRecycleViewAdapter(ArrayList<Weather10DayCardItem> mWeather10DayCardItems) {
        this.mWeather10DayCardItems = mWeather10DayCardItems;
    }

    @NonNull
    @Override
    public Weather10DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Weather10DayViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather10_day_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Weather10DayViewHolder holder, int position) {
        //TODO images and text for conditions
        holder.mBinding.idCardDay.setText(mWeather10DayCardItems.get(position).getmDay());
        holder.mBinding.idCardTemperatureMax.setText(mWeather10DayCardItems.get(position).getmTemperatureMax());
        holder.mBinding.idCardTemperatureMin.setText(mWeather10DayCardItems.get(position).getmTemperatureMin());
        holder.mBinding.idCardConditionIcon.setImageDrawable(mWeather10DayCardItems.get(position).getmIcon());
        holder.mBinding.idCardPrecipitation.setText(mWeather10DayCardItems.get(position).getmPrecipitation());
    }

    @Override
    public int getItemCount() {
        return mWeather10DayCardItems.size();
    }

    public class Weather10DayViewHolder extends RecyclerView.ViewHolder {
        public @NonNull FragmentWeather10DayCardItemBinding mBinding;
        public Weather10DayViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = FragmentWeather10DayCardItemBinding.bind(itemView);
        }
    }
}
