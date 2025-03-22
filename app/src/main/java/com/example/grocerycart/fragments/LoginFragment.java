package com.example.grocerycart.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.grocerycart.R;
import com.example.grocerycart.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        // Initialize UI components
        Button buttonRegLoginFrag=view.findViewById(R.id.buttonRegisterLogFrag);
        Button buttonLogin=view.findViewById(R.id.buttonLogin);
        EditText email=view.findViewById(R.id.editTextEmailLoginFrag);
        EditText pass=view.findViewById(R.id.editTextPasswordLoginFrag);

        // Underline text inside Register Button
        buttonRegLoginFrag.setPaintFlags(buttonRegLoginFrag.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        // Navigate to Register Fragment on button click
        buttonRegLoginFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        // Handle login button click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailLogin= email.getText().toString().trim();
                String passLogin=pass.getText().toString().trim();
                boolean isValid=true;
                // Validate email input
                if (emailLogin.isEmpty()) {
                    email.setError("Please enter email");
                    isValid = false;
                } else if  (!emailLogin.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    email.setError("Invalid email");
                    isValid = false;
                }
                // Validate password input
                if (passLogin.isEmpty()) {
                    pass.setError("Please enter password");
                    isValid = false;
                }
                // Proceed with login if validation is successful
                if(isValid) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.login();
                }
            }
        });
        return view;
    }


}