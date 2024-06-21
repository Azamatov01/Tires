package com.example.final_project.activities;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.final_project.R;
import com.example.final_project.adapters.ProductAdapter;
import com.example.final_project.databinding.ActivityCategoryBinding;
import com.example.final_project.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding; // Binding для активности
    ProductAdapter productAdapter; // Адаптер для списка продуктов
    ArrayList<Product> products; // Список продуктов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater()); // Инициализация Binding
        setContentView(binding.getRoot()); // Установка корневого элемента Binding

        products = new ArrayList<>(); // Инициализация списка продуктов

        try {
            // Чтение данных из файла JSON в ресурсах приложения
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            // Получение ID категории из intent
            int catId = getIntent().getIntExtra("categoryId", 0);

            // Обработка JSON массива
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int categoryId = jsonObject.getInt("categoryId");

                // Проверка, принадлежит ли продукт к заданной категории
                if (categoryId == catId) {
                    // Извлечение данных о продукте из JSON
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getString("image");
                    double price = jsonObject.getDouble("price");
                    String description = jsonObject.getString("description");
                    int id = jsonObject.getInt("id");
                    String categoryName = jsonObject.getString("categoryName");
                    String status = jsonObject.getString("status");
                    double discount = jsonObject.getDouble("discount");
                    int stock = jsonObject.getInt("stock");

                    // Создание объекта продукта и добавление в список
                    Product product = new Product(name, image, price, description, id, categoryId, categoryName, status, discount, stock);
                    products.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Инициализация адаптера для списка продуктов
        productAdapter = new ProductAdapter(this, products);

        // Установка заголовка активности (название категории)
        String categoryName = getIntent().getStringExtra("categoryName");
        getSupportActionBar().setTitle(categoryName);

        // Включение кнопки "Назад" в ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Настройка LayoutManager для RecyclerView (сетка из двух колонок)
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);

        // Установка адаптера для RecyclerView
        binding.productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Обработка нажатия кнопки "Назад" в ActionBar
        finish(); // Завершение активности
        return super.onSupportNavigateUp();
    }

}
