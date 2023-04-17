package com.example.fitfood.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Application;
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

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private AlertDialog.Builder builder;
    private ProductRepository productRepository;
    private List<ProductEntity> products = new ArrayList();

    @NonNull
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_recyclerview_item, parent, false);
        builder = new AlertDialog.Builder(parent.getContext());
        productRepository = ProductRepository.getInstance((Application) parent.getContext().getApplicationContext());
        return new ProductHolder(itemView);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void onBindViewHolder(ProductHolder holder, int position) {
        final ProductEntity currentProduct = this.products.get(position);
        holder.name.setText(currentProduct.name);
        holder.count.setText(String.valueOf(currentProduct.count) + " шт.");
        holder.selected.setChecked(currentProduct.selected);
        holder.selected.setOnClickListener(view -> {
            currentProduct.selected = !currentProduct.selected;
            productRepository.update(currentProduct);
            notifyDataSetChanged();
        });
        holder.delete.setOnClickListener(view -> {
            builder.setMessage("Вы уверены, что хотите удалить этот элемент?").setCancelable(false).setPositiveButton("Нет", (dialog, id) -> dialog.cancel()).setNegativeButton("Да", (dialog, id) -> {
               products.remove(currentProduct);
               productRepository.delete(currentProduct);
               notifyDataSetChanged();
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

    @SuppressLint("NotifyDataSetChanged")
    public void setProducts(List<ProductEntity> products2) {
        products = products2;
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