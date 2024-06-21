package com.example.final_project.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapters.CartAdapter;
import com.example.final_project.databinding.ActivityCheckoutBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding; // Binding для активности
    CartAdapter adapter; // Адаптер для списка продуктов в корзине
    ArrayList<Product> products; // Список продуктов в корзине
    double totalPrice = 0; // Общая стоимость заказа
    final int tax = 11; // Налоговая ставка
    ProgressDialog progressDialog; // Диалоговое окно для отображения процесса обработки
    Cart cart; // Объект корзины

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater()); // Инициализация Binding
        setContentView(binding.getRoot()); // Установка корневого элемента Binding

        progressDialog = new ProgressDialog(this); // Инициализация диалогового окна
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing..."); // Установка текста в диалоговом окне

        products = new ArrayList<>(); // Инициализация списка продуктов

        cart = TinyCartHelper.getCart(); // Получение текущей корзины

        // Заполнение списка продуктов данными из корзины
        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);
            products.add(product);
        }

        // Инициализация адаптера для списка продуктов в корзине
        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("CAD %.2f", cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // LayoutManager для RecyclerView
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager); // Установка LayoutManager для RecyclerView
        binding.cartList.addItemDecoration(itemDecoration); // Добавление разделителей между элементами списка
        binding.cartList.setAdapter(adapter); // Установка адаптера для RecyclerView

        binding.subtotal.setText(String.format("CAD %.2f", cart.getTotalPrice())); // Установка общей стоимости заказа

        // Рассчет общей стоимости с учетом налогов
        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("CAD " + totalPrice); // Установка общей стоимости в текстовое поле

        // Обработчик нажатия на кнопку "Оформить заказ"
        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Переход к активности оплаты и передача общей стоимости заказа
                Intent intent = new Intent(CheckoutActivity.this, PaymentGateway.class);
                intent.putExtra("TotalPrice", totalPrice);
                startActivity(intent);
                finish(); // Закрытие текущей активности
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Отображение кнопки "Назад" в ActionBar
    }

}
