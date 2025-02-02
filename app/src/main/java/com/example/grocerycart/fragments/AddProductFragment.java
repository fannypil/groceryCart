package com.example.grocerycart.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.grocerycart.R;
import com.example.grocerycart.activities.MainActivity;
import com.example.grocerycart.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_product, container, false);

        // Initialize Spinner (Dropdown) for product selection
        Spinner spinnerProductName=view.findViewById(R.id.spinnerProductName);

        // Set up Spinner adapter with predefined product options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_items, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductName.setAdapter(adapter);

        // Extract UI elements from layout
        EditText editTextQuantity =view.findViewById(R.id.editTextQuantity);
        Button addProductButton=view.findViewById(R.id.buttonAddProduct);
        Button backToCartButton=view.findViewById(R.id.buttonBackToCart);

        // Underline text inside the "Back to Cart" button
        backToCartButton.setPaintFlags(backToCartButton.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        // Handle "Add Product" button click
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantityStr = editTextQuantity.getText().toString().trim();
                String productName = spinnerProductName.getSelectedItem().toString();
                MainActivity mainActivity = (MainActivity) getActivity();
                // Validate quantity input
                if(quantityStr.isEmpty()){
                    mainActivity.showFailedAlertDialog("","Please Enter quantity");
                    return;
                }
                // Validate product selection
                if(productName.isEmpty()){
                    mainActivity.showFailedAlertDialog("","Please Choose product");
                    return;
                }
                // Add product to Firebase database
                mainActivity.addProductToFireBase(productName,quantityStr);
            }
        });
        // Handle "Back to Cart" button click
        backToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_addProductFragment_to_cartFragment);
            }
        });
        return view;
    }

}