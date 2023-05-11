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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitfood.R;
import com.example.fitfood.data.data_sources.room.entites.ProductEntity;
import com.example.fitfood.data.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private AlertDialog.Builder builder;
    private ProductRepository productRepository;
    private List<ProductEntity> products = new ArrayList();
    private Context context;

    public ProductListAdapter(Context context){
        this.context = context;
        productRepository = new ProductRepository((Application) context.getApplicationContext());

    }

    @NonNull
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recyclerview_item, parent, false);
        builder = new AlertDialog.Builder(parent.getContext());
               return new ProductHolder(itemView);
    }

    public void onBindViewHolder(ProductHolder holder, int position) {
        ProductEntity currentProduct = products.get(position);
        holder.name.setText(currentProduct.name);
        holder.count.setText(String.valueOf(currentProduct.count) + " шт.");
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
        holder.delete.setOnClickListener(view -> {
            builder.setMessage("Вы уверены, что хотите удалить этот элемент?").setCancelable(false).setPositiveButton("Нет", (dialog, id) -> dialog.cancel()).setNegativeButton("Да", (dialog, id) -> {
               products.remove(currentProduct);
               productRepository.delete(currentProduct);
                dialog.dismiss();
            });
            builder.create().show();
        });
    }

    public int getSelectedItemCount() {
        int count = 0;
        for (ProductEntity product : this.products) {
            if (product.selected) {
                count++;
            }
        }
        return count;
    }

    public int getItemCount() {
        return this.products.size();
    }

    public void setProducts(List<ProductEntity> productEntities) {
        products = productEntities;
        notifyDataSetChanged();
    }

    static class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView count;
        private final ImageView delete;
        private final TextView name;
        private final RadioButton selected;

        public ProductHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.count);
            selected = (RadioButton) itemView.findViewById(R.id.selected);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

        public void onClick(View view) {
        }
    }
}