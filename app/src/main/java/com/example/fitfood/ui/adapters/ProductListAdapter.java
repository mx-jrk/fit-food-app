package com.example.fitfood.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.repositories.ProductRepository;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private AlertDialog.Builder builder;

    private final ProductRepository productRepository;
    private List<ProductEntity> products;

    public ProductListAdapter(Context context){
        productRepository = new ProductRepository((Application) context.getApplicationContext());
    }

    @NonNull
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recyclerview_item, parent, false);
        builder = new AlertDialog.Builder(parent.getContext());
        return new ProductHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ProductHolder holder, int position) {
        ProductEntity currentProduct = products.get(position);
        holder.name.setText(currentProduct.name);
        holder.count.setText(currentProduct.count + " шт.");

        //Changing the color of the text based on the fact of its selection
        if (currentProduct.selected) {
            holder.name.setTextColor(Color.parseColor("#C9D6DF"));
            holder.count.setTextColor(Color.parseColor("#C9D6DF"));
        }
        else {
            holder.name.setTextColor(Color.parseColor("#FF000000"));
            holder.count.setTextColor(Color.parseColor("#FF000000"));
        }

        holder.selected.setChecked(currentProduct.selected);

        holder.selected.setOnClickListener(view -> {
            currentProduct.selected = !currentProduct.selected;
            productRepository.update(currentProduct);
        });

        //Deleting an element
        holder.delete.setOnClickListener(view -> {
            builder.setMessage("Вы уверены, что хотите удалить этот элемент?").setCancelable(false).setPositiveButton("Нет", (dialog, id) -> dialog.cancel()).setNegativeButton("Да", (dialog, id) -> {
               products.remove(currentProduct);
               productRepository.delete(currentProduct);
                dialog.dismiss();
            });
            builder.create().show();
        });
    }

    public int getItemCount() {
        if (products == null) return 0;
        return products.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(List<ProductEntity> productEntities) {
        products = productEntities;
        notifyDataSetChanged();
    }

    static class ProductHolder extends RecyclerView.ViewHolder {
        private final TextView count;
        private final ImageView delete;
        private final TextView name;
        private final RadioButton selected;

        public ProductHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            selected = itemView.findViewById(R.id.selected);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}