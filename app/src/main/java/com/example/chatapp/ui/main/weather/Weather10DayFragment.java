package com.example.chatapp.ui.main.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeather10DayBinding;
import com.example.chatapp.databinding.FragmentWeather24HourBinding;
import com.example.chatapp.model.WeatherInfoViewModel;


public class Weather10DayFragment extends Fragment {
    /**
     * Field variable to access info about weather
     */
    private WeatherInfoViewModel mModel;

    /**
     * Instance field variable for binding layout and this fragment.
     */
    private FragmentWeather10DayBinding mBinding;

    public Weather10DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherInfoViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWeather10DayBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    /**
     * Fills the bound recycler view with data
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rootRecycler.setAdapter(new Weather10DayRecycleViewAdapter(mModel.mDays));
    }
}