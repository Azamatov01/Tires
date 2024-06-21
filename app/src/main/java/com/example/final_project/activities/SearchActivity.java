package com.example.final_project.activities;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.final_project.R;
import com.example.final_project.adapters.ProductAdapter;
import com.example.final_project.databinding.ActivitySearchBinding;
import com.example.final_project.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding; // Binding объекта разметки активности
    ProductAdapter productAdapter; // Адаптер для списка продуктов
    ArrayList<Product> products; // Список всех продуктов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>(); // Инициализация списка продуктов

        // Чтение данных о продуктах из ресурса JSON
        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Извлечение свойств продукта из JSON
                String name = jsonObject.getString("name");
                String image = jsonObject.getString("image");
                double price = jsonObject.getDouble("price");
                String description = jsonObject.getString("description");
                int id = jsonObject.getInt("id");
                int categoryId = jsonObject.getInt("categoryId");
                String categoryName = jsonObject.getString("categoryName");
                String status = jsonObject.getString("status");
                double discount = jsonObject.getDouble("discount");
                int stock = jsonObject.getInt("stock");
                // Создание объекта продукта и добавление в список
                Product product = new Product(name, image, price, description, id, categoryId, categoryName, status,  discount, stock);
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Получение строки поискового запроса из Intent
        String query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query); // Установка заголовка ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Отображение кнопки "Назад" в ActionBar

        // Фильтрация продуктов по запросу
        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())
                    || product.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        // Инициализация и настройка адаптера для отображения отфильтрованных продуктов
        productAdapter = new ProductAdapter(this, filteredProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // Использование двух столбцов в RecyclerView
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Завершение текущей активности при нажатии на кнопку "Назад" в ActionBar
        return super.onSupportNavigateUp();
    }

}