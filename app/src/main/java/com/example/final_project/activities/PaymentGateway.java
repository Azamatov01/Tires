package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.final_project.R;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentGateway extends AppCompatActivity {

    Button payment;
    String PublishableKey = "pk_test_51NamUAHYesUS9MnL3WQiqpBFwxF5kuIwaxPvS8pKDOCM5OZOZGp369zHVyCJYfQQvhTRNNDcrwjVXD1ibczc27UK00NXzky3M5";
    String SecretKey = "sk_test_51NamUAHYesUS9MnLlGPy5mlO8BjxXmrvh3OKVPTwyltEGw0J3Xz8pcg3ry3Hjc54krGO7tOjN3ElGDoSsEJpKpm300Ln3wr9uy";
    String CustomerId; // Идентификатор клиента Stripe
    String EphericalKey; // Временный ключ Stripe
    String ClientSecret; // Секрет клиента Stripe
    PaymentSheet paymentSheet; // Объект для управления экраном оплаты
    Integer Value; // Сумма для оплаты
    Cart cart; // Корзина покупок
    private static final String CHANNEL_ID = "MyChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        Value = getIntent().getIntExtra("TotalPrice", 350); // Получение суммы для оплаты
        cart = TinyCartHelper.getCart(); // Получение текущей корзины

        payment = findViewById(R.id.payment); // Инициализация кнопки оплаты
        PaymentConfiguration.init(this, PublishableKey); // Инициализация Stripe SDK

        // Инициализация экрана оплаты
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> onPaymentResult(paymentSheetResult));

        // Обработчик нажатия на кнопку оплаты
        payment.setOnClickListener(view -> {
            if (CustomerId != null && EphericalKey != null && ClientSecret != null) {
                PaymentFlow(); // Если все необходимые данные инициализированы, начать процесс оплаты
            } else {
                Toast.makeText(PaymentGateway.this, "Initializing payment, please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        // Запрос на создание клиента в Stripe
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        CustomerId = object.getString("id"); // Получение идентификатора клиента Stripe
                        getEmphericalKey(new Callback() {
                            @Override
                            public void onSuccess() {
                                // Все необходимые данные инициализированы
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(PaymentGateway.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Метод для начала процесса оплаты с использованием PaymentSheet
    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Aitursun Bekboeva's", new PaymentSheet.CustomerConfiguration(
                CustomerId,
                EphericalKey
        )));
    }

    // Обработка результата оплаты
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show(); // Оплата успешно выполнена
            BuildNotification(); // Создание уведомления об успешной оплате
            cart.clearCart(); // Очистка корзины покупок
            finish(); // Завершение активности
        }
    }

    // Создание уведомления
    private void BuildNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Создание и отправка уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Payment Received")
                .setContentText("Thank you for your Order!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, builder.build());
    }

    // Получение временного ключа Stripe
    private void getEmphericalKey(final Callback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        EphericalKey = object.getString("id"); // Получение временного ключа Stripe
                        getClientSecret(CustomerId, EphericalKey, callback); // Получение секрета клиента
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(PaymentGateway.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Получение секрета клиента Stripe
    private void getClientSecret(String customerId, String ephericalKey, final Callback callback) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        ClientSecret = object.getString("client_secret"); // Получение секрета клиента Stripe
                        callback.onSuccess(); // Успешное завершение операции
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(PaymentGateway.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", String.valueOf(Value) + "00");
                params.put("currency", "CAD");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Интерфейс для обратного вызова
    interface Callback {
        void onSuccess();
    }
}
