package com.example.final_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapters.CartAdapter;
import com.example.final_project.databinding.ActivityCartBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Используем ViewBinding для удобного доступа к элементам разметки
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        // Получаем текущую корзину из TinyCart
        Cart cart = TinyCartHelper.getCart();
        // Проходимся по всем товарам в корзине и добавляем их в список products
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }
        // Инициализируем адаптер для RecyclerView
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                // Обновляем отображение итоговой стоимости при изменении количества товаров
                binding.subtotal.setText(String.format("$ %.0f CAD", (cart.getTotalPrice())));
            }
        });
        // Настройка LayoutManager и Divider для RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        // Устанавливаем итоговую стоимость корзины
        binding.subtotal.setText(String.format("$ %.0f CAD", cart.getTotalPrice()));

        // Обработчик кнопки "Продолжить"
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переходим к CheckoutActivity
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
                finish();// Закрываем текущую активность после перехода
            }
        });
        // Включаем кнопку "Вверх" в ActionBar для возможности возврата на предыдущий экран
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();// Закрываем текущую активность при нажатии на кнопку "Вверх"
        return super.onSupportNavigateUp();
    }
}