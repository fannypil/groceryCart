package com.example.grocerycart.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerycart.R;
import com.example.grocerycart.activities.MainActivity;
import com.example.grocerycart.models.Product;

import java.util.ArrayList;

public class CustomeAdapter extends RecyclerView.Adapter<CustomeAdapter.myViewHolder> {
    private ArrayList<Product> productList;
    private Context context;

    public CustomeAdapter(ArrayList<Product> productList,Context context) {
        this.productList = productList;
        this.context = context;
    }
    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productQuantity;
        ImageButton imageButtonAdd,imageButtonSub;
        public myViewHolder(View itemView){
            super(itemView);
            productName= itemView.findViewById(R.id.tvProductName);
            productQuantity =itemView.findViewById(R.id.tvQuantity);
            imageButtonAdd = itemView.findViewById(R.id.imageButtonAdd);
            imageButtonSub = itemView.findViewById(R.id.imageButtonSub);
        }
    }

    @NonNull
    @Override
    public CustomeAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.productview, parent,false);
        myViewHolder myViewHolder=new myViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter.myViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;

            // Listen to Add Button
            holder.imageButtonAdd.setOnClickListener(v -> {
                product.setQuantity(product.getQuantity() + 1);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                mainActivity.updateProductQuantityInFirebase(product.getProductName(), true);
            });

            // Listen to Substruct Button
            holder.imageButtonSub.setOnClickListener(v -> {
                if (product.getQuantity() > 0) {
                    product.setQuantity(product.getQuantity() - 1);
                    holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                    mainActivity.updateProductQuantityInFirebase(product.getProductName(), false);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void updateData(ArrayList<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

}
