package com.example.final_project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.databinding.ItemCartBinding;
import com.example.final_project.databinding.QuantityDialogBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;

/**
 * Адаптер для отображения товаров в корзине покупок.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    // Контекст активности или фрагмента
    private final Context context;
    // Список продуктов, отображаемых в корзине
    private final ArrayList<Product> products;
    // Слушатель для уведомлений о изменении количества товаров
    private final CartListener cartListener;
    // Объект корзины покупок
    private final Cart cart;

    /**
     * Интерфейс слушателя для уведомлений о изменении количества товаров в корзине.
     */
    public interface CartListener {
        void onQuantityChanged();
    }

    /**
     * Конструктор адаптера.
     *
     * @param context      Контекст активности или фрагмента
     * @param products     Список продуктов
     * @param cartListener Слушатель для уведомлений о изменении количества товаров
     */
    public CartAdapter(Context context, ArrayList<Product> products, CartListener cartListener) {
        this.context = context;
        this.products = products;
        this.cartListener = cartListener;
        this.cart = TinyCartHelper.getCart(); // Получение объекта корзины
    }

    /**
     * Создание нового элемента RecyclerView.
     */
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового представления элемента корзины
        return new CartViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false));
    }

    /**
     * Отображение данных для конкретного элемента RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = products.get(position); // Получение продукта для текущего положения

        // Загрузка изображения продукта с помощью Glide
        Glide.with(context)
                .load(product.getImage())
                .into(holder.binding.image);

        // Установка текста для различных представлений элемента корзины
        holder.binding.name.setText(product.getName());
        holder.binding.price.setText("$ " + String.format("%.0f", product.getPrice()) + " CAD");
        holder.binding.quantity.setText(product.getQuantity() + " item(s)");

        // Настройка слушателя для нажатия на элемент
        holder.itemView.setOnClickListener(view -> {
            // Создание и отображение диалога для изменения количества товара
            QuantityDialogBinding quantityDialogBinding = QuantityDialogBinding.inflate(LayoutInflater.from(context));
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(quantityDialogBinding.getRoot())
                    .create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

            // Установка данных в диалоговом окне
            quantityDialogBinding.productName.setText(product.getName());
            quantityDialogBinding.productStock.setText("Stock: " + product.getStock());
            quantityDialogBinding.quantity.setText(String.valueOf(product.getQuantity()));
            int stock = product.getStock();

            // Установка слушателей для кнопок увеличения и уменьшения количества
            quantityDialogBinding.plusBtn.setOnClickListener(view1 -> {
                int quantity = product.getQuantity();
                quantity++;
                if (quantity > product.getStock()) {
                    Toast.makeText(context, "Max stock available: " + product.getStock(), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    product.setQuantity(quantity);
                    quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                }
                notifyDataSetChanged(); // Обновление данных в адаптере
                cart.updateItem(product, product.getQuantity()); // Обновление товара в корзине
                cartListener.onQuantityChanged(); // Уведомление слушателя о изменении
            });

            quantityDialogBinding.minusBtn.setOnClickListener(view12 -> {
                int quantity = product.getQuantity();
                if (quantity > 1) quantity--;
                product.setQuantity(quantity);
                quantityDialogBinding.quantity.setText(String.valueOf(quantity));
                notifyDataSetChanged(); // Обновление данных в адаптере
                cart.updateItem(product, product.getQuantity()); // Обновление товара в корзине
                cartListener.onQuantityChanged(); // Уведомление слушателя о изменении
            });

            quantityDialogBinding.saveBtn.setOnClickListener(view13 -> dialog.dismiss()); // Закрытие диалога при нажатии на кнопку сохранения

            dialog.show(); // Отображение диалога
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
    public class CartViewHolder extends RecyclerView.ViewHolder {

        ItemCartBinding binding;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCartBinding.bind(itemView); // Привязка представлений к разметке элемента
        }
    }
}
