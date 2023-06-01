package com.example.chatapp.ui.main.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding mBinding;

    public SharedPreferences mSharedPreferences;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSettingsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroup = mBinding.radioGroup;
        MainActivity mainActivity = (MainActivity) getActivity();
        int selectedTheme = mSharedPreferences.getInt("selectedTheme", R.style.Theme_ChatApp);
        //System.out.println(selectedTheme);
        //selects the button with th current theme
        if (selectedTheme == R.style.Theme_ChatApp) {
            radioGroup.check(R.id.radio_button_purple);
        }else if (selectedTheme == R.style.Theme_Light) {
            radioGroup.check(R.id.radio_button_light);
        }else if (selectedTheme == R.style.Theme_Dark) {
            radioGroup.check(R.id.radio_button_dark);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int theme = R.style.Theme_ChatApp;
                if (checkedId == R.id.radio_button_purple) {
                    theme = R.style.Theme_ChatApp;
                    mBinding.settingsTextTheme.setTextColor(getResources().getColor(R.color.accent_color, getActivity().getTheme()));
                }
                if (checkedId == R.id.radio_button_light) {
                    theme = R.style.Theme_Light;
                }
                if (checkedId == R.id.radio_button_dark) {
                    theme = R.style.Theme_Dark;
                }
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("selectedTheme", theme);
                editor.commit();

                mainActivity.changeTheme(theme);
            }
        });
    }

    public int getSelectedTheme() {
        return mSharedPreferences.getInt("selectedTheme", R.style.Theme_ChatApp);
    }

}