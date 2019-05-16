package com.example.alua.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alua.Activities.LoginActivity;
import com.example.alua.Activities.MainActivity;
import com.example.alua.R;

import io.paperdb.Paper;

public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    Spinner changeLanguage;
    TextView changeLanguageTV;
    Button changeLanguageButton;
    String language;
    public SettingsFragment() {}

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Paper.init(getActivity());
        changeLanguage = getView().findViewById(R.id.spinner_language);
        changeLanguageTV = getView().findViewById(R.id.change_language_TV);
        changeLanguageButton = getView().findViewById(R.id.change_language_button);
        setLanguage();
        if (Paper.book().contains("language")) {
            if (Paper.book().read("language").equals("kz")) {
                changeLanguageTV.setText(getString(R.string.change_language_button_kz));
                changeLanguageButton.setText(getString(R.string.accept_kz));
            } else if (Paper.book().read("language").equals("ru")) {
                changeLanguageTV.setText(getString(R.string.change_language_button_ru));
                changeLanguageButton.setText(getString(R.string.accept_ru));
            } else {
                changeLanguageTV.setText(getString(R.string.change_language_button_en));
                changeLanguageButton.setText(getString(R.string.accept_en));
            }
        }
    }

    public void setLanguage(){
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        changeLanguage.setAdapter(genderSpinnerAdapter);
        changeLanguage.setSelection(2);

        changeLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.language_kz))) {
                        language = "kz";
                    } else if (selection.equals(getString(R.string.language_ru))) {
                        language = "ru";
                    } else {
                        language = "en";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        changeLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (language.equals("kz")) {
                    Paper.book().write("language", "kz");
                } else if (language.equals("ru")) {
                    Paper.book().write("language", "ru");
                } else {
                    Paper.book().write("language", "en");
                }
                intentToMainMenu();
            }
        });
    }

    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void intentToMainMenu(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("intent", 1);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
