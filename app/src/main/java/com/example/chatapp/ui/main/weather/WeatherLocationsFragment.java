package com.example.chatapp.ui.main.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentWeatherBinding;
import com.example.chatapp.databinding.FragmentWeatherLocationsBinding;
import com.example.chatapp.model.WeatherInfoViewModel;

import org.json.JSONArray;
import org.json.JSONObject;


public class WeatherLocationsFragment extends Fragment {

    private WeatherInfoViewModel mModel;
    private FragmentWeatherLocationsBinding mBinding;

    public WeatherLocationsFragment() {
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
        mBinding = FragmentWeatherLocationsBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rootRecycler.setAdapter(new WeatherLocationsRecyclerViewAdapter(mModel.mPastLocations));
        mModel.addLocationResponseObserver(
                getViewLifecycleOwner(),
                this::observeData

        );


    }

    private void observeData(JSONObject data) {

        String City = "";
        String State = "";
        String Country = "";
        String Zipcode = "";

        try {
            JSONArray address_components = data.getJSONArray("address_components");
            for (int i = 0; i < address_components.length(); i++) {
                JSONObject zero2 = address_components.getJSONObject(i);
                String long_name = zero2.getString("long_name");
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);
                if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                    if (Type.equalsIgnoreCase("locality")) {
                        City = long_name;
                    } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                        State = long_name;
                    } else if (Type.equalsIgnoreCase("country")) {
                        Country = long_name;
                    } else if (Type.equalsIgnoreCase("postal_code")) {
                        Zipcode = long_name;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("LOCATION", "Local Lookup Details: " +
                "City: " + City + "\n" +
                "State: " + State + "\n" +
                "Country: " + Country + "\n" +
                "Zipcode: " + Zipcode
        );

        //Check for duplicates
        //boolean noDuplicates = true;

        for (int i = 0; i < mModel.mPastLocations.size(); i++) {
            WeatherLocationsCardItem card = mModel.mPastLocations.get(i);
            if (card.getZipCode().equals(Zipcode)) {
                //noDuplicates = false;
                mModel.mPastLocations.remove(i);
                break;
            }
        }

        if (!City.equals("")) {
            WeatherLocationsCardItem newCard = new WeatherLocationsCardItem(City, State, Zipcode, Country);
            mModel.mPastLocations.add(0, newCard);
            Log.d("LOCATION", mModel.mPastLocations.toString());
        }

        mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }


}