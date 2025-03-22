package com.example.grocerycart.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.grocerycart.R;
import com.example.grocerycart.fragments.LoginFragment;
import com.example.grocerycart.models.Product;
import com.example.grocerycart.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // user login authentication
    public void login() {
        String email=((EditText) findViewById(R.id.editTextEmailLoginFrag)).getText().toString();
        String password=((EditText) findViewById(R.id.editTextPasswordLoginFrag)).getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            showSuccessAlertDialog("","login Successful");
                            // Navigate to cart fragment after successful login
                            NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                            navController.navigate(R.id.action_loginFragment_to_cartFragment);
                        } else {
                            // Login Failed
                            Exception exception=task.getException();
                            handleLoginError(exception);
                        }
                    }
                });
    }
    // Handle login failure cases
    private void handleLoginError(Exception exception){
        String errorMessage="Login Failed";
        if(exception instanceof FirebaseAuthInvalidUserException){
            errorMessage="Email not registered.\n Please sign up first.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMessage="Incorrect password.\n Please try again.";
        } else if (exception==null) {
            errorMessage=exception.getMessage();
        }
        showFailedAlertDialog("Login Failed",errorMessage);
    }
    // user registration
    public void register() {
        String email=((EditText) findViewById(R.id.editTextEmailRegFrag)).getText().toString();
        String password=((EditText) findViewById(R.id.editTextPasswordRegFrag)).getText().toString();
        mAuth.fetchSignInMethodsForEmail(email.trim().toLowerCase()).addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               List<String> signInMethods = task.getResult().getSignInMethods();
               boolean emailExists = signInMethods != null && !signInMethods.isEmpty();
               if(emailExists){
                   showFailedAlertDialog("","An account with this email already exists.");
               }else{
                   //create user
                   createUser(email,password);
               }
           }else{
               showFailedAlertDialog("","task.getException().getMessage()");
           }
        });
}
    // Create new user in Firebase Authentication
    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successful Registration
                        showSuccessAlertDialog("","Account Created Successfully");
                        addDATA();
                    } else {
                        // Failed Registration
                        showFailedAlertDialog("Registration failed",task.getException().getMessage());
                    }
                });
    }
    // Adds user data to Firebase Realtime Database
    public void addDATA() {
        //Extracting data from the layout
        String email=((EditText) findViewById(R.id.editTextEmailRegFrag)).getText().toString();
        String phone=((EditText) findViewById(R.id.editTextPhone)).getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(email.split("@")[0]);

        User client=new User(email,phone);
        myRef.setValue(client);
    }
    // Logs out the user and navigates to login screen
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
        navController.popBackStack(R.id.loginFragment,false);
        showSuccessAlertDialog("","Logged out successfully!");
    }
    public void addProductToFireBase(String productName,String qtyStr) {
        // Ensure FirebaseAuth instance is initialized
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = (currentUser.getEmail()).split("@")[0];
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("products");
        } else{
            showFailedAlertDialog("","User is not logged in");
            return;
        }try {
            int quantity = Integer.parseInt(qtyStr);
            if (quantity <= 0) {
                showFailedAlertDialog("","Invalid quantity");
                return;
            }
            Product newProduct = new Product(productName, quantity);

            // Check if the product already exists in Firebase
            databaseReference.child(productName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        // Update existing product quantity
                        Product existingProduct = snapshot.getValue(Product.class);
                        if (existingProduct != null) {
                            existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                            databaseReference.child(productName).setValue(existingProduct);
                            showSuccessAlertDialog("","Product quantity updated!");
                        }
                    } else {
                        // Add new product if it doesn't exist
                        databaseReference.child(productName).setValue(newProduct);
                        showSuccessAlertDialog("","Product added successfully!");
                    }
                } else {
                    // Error while fetching data
                    showFailedAlertDialog("","Failed to add product:"+ task.getException().getMessage());
                }
            }).addOnFailureListener(e -> {
                // Handle database failure
                showFailedAlertDialog("","Failed to update product:"+ e.getMessage());

            });
        }catch (NumberFormatException e){
            // Handle invalid quantity format
            showFailedAlertDialog("","Invalid quantity format!");
        }
    }
    public void updateProductQuantityInFirebase(String productName, boolean isAdding) {
        // Ensure FirebaseAuth instance is initialized
        if (mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = (currentUser.getEmail()).split("@")[0];
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("products");
        } else {
            showFailedAlertDialog("","User is not logged in");
            return;
        }
        databaseReference.child(productName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Product existingProduct = snapshot.getValue(Product.class);
                    if (existingProduct != null) {
                        int newQuantity = existingProduct.getQuantity() + (isAdding ? 1 : -1);
                        // Prevent negative quantity
                        if (newQuantity < 0) newQuantity = 0;
                        existingProduct.setQuantity(newQuantity);
                        databaseReference.child(productName).setValue(existingProduct);
                    }
                }
            } else {
                showFailedAlertDialog("","Failed to update quantity:"+ task.getException().getMessage());
            }
        }).addOnFailureListener(e -> {
            showFailedAlertDialog("","Failed to update quantity:"+ e.getMessage());
        });
    }
    // Displays an error alert dialog
    public void showFailedAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate Custom Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.failed_dialog, null);
        TextView dialogDescription = dialogView.findViewById(R.id.failedDescription);
        Button doneButton = dialogView.findViewById(R.id.failedDone);
        dialogDescription.setText(message);
        // Connect View to dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        // Add action to Done Button
        doneButton.setOnClickListener(view -> dialog.dismiss());
        // Display Dialog
        dialog.show();
    }
    // Displays a success alert dialog
    public void showSuccessAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate Custom Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.success_dialog, null);
        TextView dialogDescription = dialogView.findViewById(R.id.successDescription);
        Button doneButton = dialogView.findViewById(R.id.successDone);
        dialogDescription.setText(message);
        // Connect View to dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        // Add action to Done Button
        doneButton.setOnClickListener(view -> dialog.dismiss());
        // Display Dialog
        dialog.show();
    }
}