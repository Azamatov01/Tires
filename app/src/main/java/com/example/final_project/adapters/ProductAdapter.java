package com.example.final_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.activities.ProductDetailActivity;
import com.example.final_project.databinding.ItemProductBinding;
import com.example.final_project.model.Product;

import java.util.ArrayList;

/**
 * Адаптер для отображения списка продуктов в RecyclerView.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // Контекст активности или фрагмента
    private final Context context;
    // Список продуктов для отображения
    private final ArrayList<Product> products;

    /**
     * Конструктор адаптера.
     *
     * @param context  Контекст активности или фрагмента
     * @param products Список продуктов для отображения
     */
    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    /**
     * Создание нового элемента RecyclerView.
     */
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового представления элемента продукта
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    /**
     * Отображение данных для конкретного элемента RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position); // Получение продукта для текущего положения

        // Загрузка и отображение изображения продукта с использованием Glide
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        // Установка названия продукта
        holder.binding.label.setText(product.getName());

        // Установка цены продукта
        holder.binding.price.setText("$ " + String.format("%.0f", product.getPrice()) + " CAD");

        // Настройка слушателя для нажатия на элемент продукта
        holder.itemView.setOnClickListener(view -> {
            // Создание и запуск новой активности для отображения деталей продукта
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("image", product.getImage());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("id", product.getId());
            intent.putExtra("categoryId", product.getCategoryId());
            intent.putExtra("categoryName", product.getCategoryName());
            intent.putExtra("stock", product.getStock());
            intent.putExtra("status", product.getStatus());
            intent.putExtra("discount", product.getDescription());

            context.startActivity(intent); // Запуск активности
        });
    }

    /**
     * Получение количества элементов в списке продуктов.
     */
    @Override
    public int getItemCount() {
        return products.size();
    }

    /**
     * Внутренний класс представления для элементов RecyclerView.
     */
    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ItemProductBinding binding; // Привязка представлений элемента продукта

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView); // Привязка представлений к разметке элемента
        }
    }
}
