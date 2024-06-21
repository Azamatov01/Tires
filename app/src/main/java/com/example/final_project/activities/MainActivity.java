package com.example.final_project.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.final_project.R;
import com.example.final_project.adapters.CategoryAdapter;
import com.example.final_project.adapters.ProductAdapter;
import com.example.final_project.databinding.ActivityMainBinding;
import com.example.final_project.model.Category;
import com.example.final_project.model.Product;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding; // Binding для активности
    CategoryAdapter categoryAdapter; // Адаптер для списка категорий
    ArrayList<Category> categories; // Список категорий
    ProductAdapter productAdapter; // Адаптер для списка продуктов
    ArrayList<Product> products; // Список продуктов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Инициализация Binding
        setContentView(binding.getRoot()); // Установка корневого элемента Binding

        // Обработчик для поисковой строки
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {}

            @Override
            public void onSearchConfirmed(CharSequence text) {
                // Переход к активности поиска с передачей запроса
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("query", text.toString());
                startActivity(intent);
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        initCategories(); // Инициализация списка категорий
        initProducts(); // Инициализация списка продуктов
        getRecentOffers(); // Получение последних предложений для карусели
    }

    // Метод для инициализации списка категорий
    void initCategories() {
        categories = new ArrayList<>(); // Инициализация списка категорий

        // Чтение и парсинг JSON данных из ресурсов
        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.categories);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String image = jsonObject.getString("image");
                String color = jsonObject.getString("color");
                String data = jsonObject.getString("data");
                int id = jsonObject.getInt("id");

                Category category = new Category(name, image, color, data, id);
                categories.add(category); // Добавление категории в список
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        categoryAdapter = new CategoryAdapter(this, categories); // Инициализация адаптера для списка категорий

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4); // LayoutManager для RecyclerView с категориями
        binding.categoriesList.setLayoutManager(layoutManager); // Установка LayoutManager для RecyclerView с категориями
        binding.categoriesList.setAdapter(categoryAdapter); // Установка адаптера для RecyclerView с категориями
    }

    // Метод для получения последних предложений для карусели
    void getRecentOffers() {
        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.offers);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String image = jsonObject.getString("image");
                String title = jsonObject.getString("title");
                binding.carousel.addData(new CarouselItem(image, title)); // Добавление элемента карусели
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для инициализации списка продуктов
    void initProducts() {
        products = new ArrayList<>(); // Инициализация списка продуктов

        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
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

                Product product = new Product(name, image, price, description, id, categoryId, categoryName, status, discount, stock);
                products.add(product); // Добавление продукта в список
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        productAdapter = new ProductAdapter(this, products); // Инициализация адаптера для списка продуктов

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2); // LayoutManager для RecyclerView с продуктами
        binding.productList.setLayoutManager(layoutManager); // Установка LayoutManager для RecyclerView с продуктами
        binding.productList.setAdapter(productAdapter); // Установка адаптера для RecyclerView с продуктами
    }

}
