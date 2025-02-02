package com.example.grocerycart.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.grocerycart.R;
import com.example.grocerycart.activities.MainActivity;
import com.example.grocerycart.adapters.CustomeAdapter;
import com.example.grocerycart.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomeAdapter productAdapter;
    private ArrayList<Product> productList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        //Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Extract the username from the email
        String userName = (currentUser.getEmail()).split("@")[0];

        // Set greeting message with the username
        TextView tvUserName = view.findViewById(R.id.connectedUser);
        tvUserName.setText("Hello, "+userName);

        // Initialize UI elements
        ImageButton imageButtonAddPro=view.findViewById(R.id.imageButtonAddToCart);
        ImageButton imageButtonLogOut=view.findViewById(R.id.imageButtonLogOut);

        // Navigate to Add Product Fragment
        imageButtonAddPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_cartFragment_to_addProductFragment);
            }
        });
        // Handle Logout button
        imageButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.logout();
            }
        });
        // Load user's product list from Firebase Database
        if(currentUser!=null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userName).child("products");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productList = new ArrayList<>();
                    // Iterate through all products stored in the database
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String productName=snapshot.child("productName").getValue(String.class);
                        int quantity=snapshot.child("quantity").getValue(Integer.class);
                        if(productName!=null) {
                            Product product = new Product(productName,quantity);
                            productList.add(product);
                        }
                    }
                    // Set up RecyclerView to display the product list
                    setupRecyclerView(view);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CartFragment", "Error loading products: " + error.getMessage());
                }
            });
        }
        return view;
    }
    //Sets up the RecyclerView to display the product list.
    private void setupRecyclerView(View view) {
        recyclerView=view.findViewById(R.id.recyclerViewCart);
        // Ensure RecyclerView is not null before proceeding
        if (recyclerView == null) {
            return;
        }
        // Set up RecyclerView layout and animations
        layoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Populate RecyclerView with product data if the list is not empty
        if (productList != null && !productList.isEmpty()) {
            productAdapter = new CustomeAdapter(productList,requireActivity());
            recyclerView.setAdapter(productAdapter);
        }
    }
}