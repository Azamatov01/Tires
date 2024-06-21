package com.example.final_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.activities.CategoryActivity;
import com.example.final_project.databinding.ItemCategoriesBinding;
import com.example.final_project.model.Category;

import java.util.ArrayList;

/**
 * Адаптер для отображения категорий продуктов в RecyclerView.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    // Контекст активности или фрагмента
    private final Context context;
    // Список категорий продуктов
    private final ArrayList<Category> categories;

    /**
     * Конструктор адаптера.
     *
     * @param context    Контекст активности или фрагмента
     * @param categories Список категорий продуктов
     */
    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    /**
     * Создание нового элемента RecyclerView.
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового представления элемента категории продуктов
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false));
    }

    /**
     * Отображение данных для конкретного элемента RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position); // Получение категории для текущего положения

        // Установка названия категории с использованием HTML разметки
        holder.binding.label.setText(Html.fromHtml(category.getName()));

        // Загрузка и отображение иконки категории с помощью Glide
        Glide.with(context)
                .load(category.getIcon())
                .into(holder.binding.image);

        // Установка фона изображения с цветом, заданным в категории
        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()));

        // Настройка слушателя для нажатия на элемент категории
        holder.itemView.setOnClickListener(view -> {
            // Создание и запуск новой активности для отображения продуктов выбранной категории
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });
    }

    /**
     * Получение количества элементов в списке категорий продуктов.
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Внутренний класс представления для элементов RecyclerView.
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView); // Привязка представлений к разметке элемента
        }
    }
}
