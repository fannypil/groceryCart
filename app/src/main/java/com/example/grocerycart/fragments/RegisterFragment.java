package com.example.grocerycart.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocerycart.R;
import com.example.grocerycart.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        mAuth=FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        // Initialize UI elements
        Button buttonRegisterRegFrag = view.findViewById(R.id.buttonRegisterRegFrag);
        Button buttonBackToLogin =view.findViewById(R.id.buttonBackToLogin);
        EditText emailInput = view.findViewById(R.id.editTextEmailRegFrag);
        EditText passInput = view.findViewById(R.id.editTextPasswordRegFrag);
        EditText passValidInput = view.findViewById(R.id.editTextRePasswordRegFrag);
        EditText phoneInput = view.findViewById(R.id.editTextPhone);

        // Underline text inside the "Back to Login" button
        buttonBackToLogin.setPaintFlags(buttonBackToLogin.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        buttonRegisterRegFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract user input
                String emailInputText = emailInput.getText().toString().trim();
                String passInputText = passInput.getText().toString().trim();
                String passValidInputText = passValidInput.getText().toString().trim();
                String phoneInputText = phoneInput.getText().toString().trim();
                boolean isValid = true;
                // Validate email input
                if (emailInputText.isEmpty()) {
                    emailInput.setError("Please enter email");
                    isValid = false;
                } else if (!emailInputText.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                    emailInput.setError("Invalid email");
                    isValid = false;
                }
                // Validate password input
                if (passInputText.isEmpty()) {
                    passInput.setError("Please enter password");
                    isValid = false;
                } else if (passInputText.length() < 6) {
                    passInput.setError("Password must be at least 6 characters long");
                    isValid = false;
                }
                // Validate password confirmation
                if (passValidInputText.isEmpty()) {
                    passValidInput.setError("Please enter password again");
                    isValid = false;
                }
                // Validate phone number
                if (phoneInputText.isEmpty()) {
                    phoneInput.setError("Please enter phone number");
                    isValid = false;
                } else if (!phoneInputText.matches("\\d+")) {
                    phoneInput.setError("Phone number must contain only digits");
                    isValid = false;
                }
                // Proceed if all inputs are valid
                if (isValid) {
                    if (passInputText.equals(passValidInputText)) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.register();
                    } else {
                        passInput.setError("Passwords do not match");
                    }

                }
            }
        });
        // Navigate back to login fragment
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }

}