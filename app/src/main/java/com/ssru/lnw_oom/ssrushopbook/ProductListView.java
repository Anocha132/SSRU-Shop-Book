package com.ssru.lnw_oom.ssrushopbook;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ProductListView extends AppCompatActivity {

    // Explicit
    private TextView nameTextViwe, surnameTextView, moneyTextView;
    private ListView listView;
    private String[] loginStrings, nameStrings, priceStrings, coverStrings, eBookStrings;
    private String urlJSON = "http://swiftcodingthai.com/ssru/get_product.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_view);

        // Bind Widget
        nameTextViwe = (TextView) findViewById(R.id.textView);
        surnameTextView = (TextView) findViewById(R.id.textView2);
        moneyTextView = (TextView) findViewById(R.id.textView9);
        listView = (ListView) findViewById(R.id.listView);

        // Receive Value From Intent
        loginStrings = getIntent().getStringArrayExtra("Login");

        // Show View
        nameTextViwe.setText(loginStrings[1]);
        surnameTextView.setText(loginStrings[2]);
        moneyTextView.setText(loginStrings[5] + " THB.");

        SynchronizeProduct synchronizeProduct = new SynchronizeProduct(this, urlJSON);
        synchronizeProduct.execute();

    }   // Main Method

    private class SynchronizeProduct extends AsyncTask<Void, Void, String> {

        private Context context;
        private String urlString;
        private ProgressDialog progressDialog;

        public SynchronizeProduct(Context context,
                                  String urlString) {
            this.context = context;
            this.urlString = urlString;
        }   // Constructor

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(context, "Load Product", "Load Product Process ...");

        }   // OnPre

        @Override
        protected String doInBackground(Void... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlString).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("1JuneV1", "doIn e ==> " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!(s.equals(""))) {
                progressDialog.dismiss();
                Log.d("1JuneV1", "s ==> " + s);
            }

            try {

                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                priceStrings = new String[jsonArray.length()];
                coverStrings = new String[jsonArray.length()];
                eBookStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    nameStrings[i] = jsonObject.getString("Name");
                    priceStrings[i] = jsonObject.getString("Price");
                    coverStrings[i] = jsonObject.getString("Cover");
                    eBookStrings[i] = jsonObject.getString("Ebook");

                }   // for

                final MyAdapter myAdapter = new MyAdapter(context, nameStrings,
                        priceStrings, coverStrings);
                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        if (checkMoney(priceStrings[i])) {

                            confirmDialog(nameStrings[i], priceStrings[i], eBookStrings[i]);

                        } else {

                            MyAlert myAlert = new MyAlert();
                            myAlert.myDialog(context, "เงินไม่พอ", "กรุณาเลือกเล่มใหม่");

                        }

                    }   // onItemClick
                });

            } catch (Exception e) {
                Log.d("1JuneV2", "onPost e ==> " + e.toString());
            }

        }   // onPost

        private boolean checkMoney(String priceString) {

            int intMyMoney = Integer.parseInt(loginStrings[5]);
            int intPrice = Integer.parseInt(priceString);

            if (intMyMoney >= intPrice) {
                return true;
            } else {
                return false;
            }

        }

    }   //Syn Class

    private void confirmDialog(final String nameString,
                               final String priceString,
                               final String eBookString) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_myaccount);
        builder.setCancelable(false);
        builder.setTitle("Confirm Order");
        builder.setMessage(nameString + " ราคา " + priceString + " THB." + "\n" + "จริงๆ หรือ ?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                Intent intent = new Intent(ProductListView.this, ReadPDF.class);
                intent.putExtra("Login", loginStrings);
                intent.putExtra("NameBook", nameString);
                intent.putExtra("PriceBook", priceString);
                intent.putExtra("urlEbook", eBookString);
                startActivity(intent);

                dialog.dismiss();
            }
        });
        builder.show();

    }   // confirm


}   // Main Class
