package com.example.final_project.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.databinding.ActivityProductDetailBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding; // Binding объекта разметки активности
    Product currentProduct; // Текущий продукт, отображаемый на экране
    TextView productName, productPrice, productCategory, productDescription; // Views для отображения информации о продукте

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация Views из разметки
        productName = binding.productName;
        productPrice = binding.productPrice;
        productCategory = binding.productCategory;
        productDescription = binding.productDescription;

        // Получение данных о продукте из Intent
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String category = getIntent().getStringExtra("categoryName");
        String description = getIntent().getStringExtra("description");
        int id = getIntent().getIntExtra("id", 0);
        double price = getIntent().getDoubleExtra("price", 0);
        int categoryId = getIntent().getIntExtra("categoryId", 0);
        String status = getIntent().getStringExtra("status");
        double discount = getIntent().getDoubleExtra("discount", 0);
        int stock = getIntent().getIntExtra("stock", 0);

        // Установка данных о продукте в соответствующие Views
        String priceofProduct = String.valueOf(price);
        productName.setText(name);
        productPrice.setText("$ " + priceofProduct + " CAD");
        productCategory.setText("Category: " + category);
        productDescription.setText(description);

        // Создание объекта текущего продукта
        currentProduct = new Product(name, image, price, description, id, categoryId, category, status, discount, stock);

        // Загрузка изображения продукта с использованием Glide
        Glide.with(this)
                .load(image)
                .into(binding.productImage);

        // Настройка ActionBar
        getSupportActionBar().setTitle(name); // Установка заголовка ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Отображение кнопки "Назад" в ActionBar

        // Получение экземпляра корзины
        Cart cart = TinyCartHelper.getCart();

        // Обработчик нажатия на кнопку "Добавить в корзину"
        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct, 1); // Добавление продукта в корзину
                binding.addToCartBtn.setEnabled(false); // Делаем кнопку неактивной после добавления
                binding.addToCartBtn.setText("Added in cart"); // Изменяем текст кнопки
                binding.addToCartBtn.setTextColor(Color.GRAY); // Изменяем цвет текста кнопки
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu); // Заполнение меню (иконка корзины)
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) { // Обработка нажатия на иконку корзины в меню
            startActivity(new Intent(this, CartActivity.class)); // Переход к активности корзины
            finish(); // Завершение текущей активности
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Завершение текущей активности при нажатии на кнопку "Назад" в ActionBar
        return super.onSupportNavigateUp();
    }

}
