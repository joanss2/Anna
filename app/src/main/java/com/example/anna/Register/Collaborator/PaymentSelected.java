package com.example.anna.Register.Collaborator;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anna.MenuPrincipal.CollaboratorMenu;
import com.example.anna.Models.Subscription;
import com.example.anna.Models.Tariff;
import com.example.anna.Models.User;
import com.example.anna.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PaymentSelected extends AppCompatActivity {

    private final String SECRET_KEY = "sk_test_51LQx1NCNWRocLIPcI1XmpgijRgjDhl79zkmS927h2t2ru0eTzGZZpFLfgxgj2J2kSKtkH6pC5G2oNly9nrminBPw007Ey1YqTL";

    private PaymentSheet paymentSheet;

    private SharedPreferences userInfoPrefs;


    private String customerID;
    private String ephericalKey;
    private String clientSecret;

    private float price;
    private Tariff tariff;
    private boolean prefsDeletable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paynow);

        userInfoPrefs = getSharedPreferences("USERINFO",MODE_PRIVATE);

        Button payButton = findViewById(R.id.pay_button);
        price = getIntent().getFloatExtra("price",4.98f);
        tariff = getIntent().getParcelableExtra("tariff");

        String PUBLISH_KEY = "pk_test_51LQx1NCNWRocLIPcGVHVMKjlE2pvJIMQTv62kOjzcuF9KA0tk74chSMPwNpFPNk1hj1aycunBC0r6nRhMBAoclGV00Bnlb0jLZ";
        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        payButton.setOnClickListener(view -> paymentFlow());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", response -> {
            try {
                JSONObject object = new JSONObject(response);
                customerID = object.getString("id");
                Toast.makeText(getApplicationContext(),"customerID "+customerID,Toast.LENGTH_SHORT).show();

                getEphericalKey(customerID);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this,"Payment success!",Toast.LENGTH_SHORT).show();
            prefsDeletable = false;

            createSubscription();

            startActivity(new Intent(this, CollaboratorMenu.class));
            finishAffinity();
        }
    }

    private void getEphericalKey(String customerID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", response -> {
            try {
                JSONObject object = new JSONObject(response);
                ephericalKey = object.getString("id");
                Toast.makeText(getApplicationContext(),"ephericalKey "+ephericalKey,Toast.LENGTH_SHORT).show();

                getClientSecret(customerID, ephericalKey);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                header.put("Stripe-Version","2020-08-27");
                return header;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", response -> {
            try {
                JSONObject object = new JSONObject(response);
                clientSecret = object.getString("client_secret");
                Toast.makeText(getApplicationContext(),"client secret "+clientSecret,Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+SECRET_KEY);
                return header;
            }

            @NonNull
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", (int) price +"99");
                params.put("currency","eur");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void paymentFlow() {

        paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration("Instagram",
                new PaymentSheet.CustomerConfiguration(
                        customerID,
                        ephericalKey
                )));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prefsDeletable){
            userInfoPrefs.edit().clear().apply();
        }
    }

    private void createSubscription(){
        Date currentDate = new Date();
        Date dateEnd = addMonth(currentDate,1);
        User user = new User();
        user.setEmail(userInfoPrefs.getString("email",null));
        user.setUsername(userInfoPrefs.getString("username",null));
        user.setUserKey(userInfoPrefs.getString("userKey",null));
        user.setLanguage(Locale.getDefault().getLanguage());
        Subscription subscription = new Subscription(tariff,currentDate,dateEnd,user);

        Map<String,Object> fieldkey = new HashMap<>();
        fieldkey.put("key",userInfoPrefs.getString("userKey", null));
        fieldkey.put("username",userInfoPrefs.getString("username", null));

        DocumentReference subsReference = FirebaseFirestore.getInstance().collection("Subscriptions").document(userInfoPrefs.getString("userKey",null));
        subsReference.set(fieldkey)
                .addOnSuccessListener(unused -> subsReference.collection("SubsOfUser").add(subscription).addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Subscription created correctly",Toast.LENGTH_SHORT).show();
                            }
                        }
                ));

    }

    public Date addMonth(Date inputDate, int monthToAddNumber){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        // Add 'monthToAddNumber' months to inputDate
        calendar.add(Calendar.MONTH, monthToAddNumber);
        return calendar.getTime();
    }
}