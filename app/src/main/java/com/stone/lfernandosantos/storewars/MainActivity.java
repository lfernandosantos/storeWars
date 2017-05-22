package com.stone.lfernandosantos.storewars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stone.lfernandosantos.storewars.controlers.IProductsService;
import com.stone.lfernandosantos.storewars.controlers.ListaProductsAdapter;
import com.stone.lfernandosantos.storewars.controlers.RecyclerViewOnClickListenerHack;
import com.stone.lfernandosantos.storewars.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    SupportStatus status;
    List<Product> products;
    private Button btnHistorico;
    private Button btnCompras;

    private RecyclerView recyclerView;
    private ListaProductsAdapter productsAdapter;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        btnHistorico = (Button) findViewById(R.id.btnHistorico);
        btnCompras = (Button) findViewById(R.id.btnCompras);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm  = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoricoActivity.class));

            }
        });

        btnCompras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CarrinhoActivity.class));

            }
        });

    }

    private void doRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IProductsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IProductsService json = retrofit.create(IProductsService.class);

        Call<List<Product>> listaRequest = json.listCall();

        listaRequest.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                products = response.body();

                productsAdapter = new ListaProductsAdapter(products, MainActivity.this);
                productsAdapter.setmRecyclerViewOnClickListenerHack(MainActivity.this);
                recyclerView.setAdapter(productsAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

                progressDialog.dismiss();

                Snackbar.make(btnCompras, "Sem Conexão!", Snackbar.LENGTH_SHORT).show();

                Log.i("JSON", "ERRO" + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        status = new SupportStatus(this);
        if(!status.getStatusInternet()){

            Snackbar.make(btnCompras, "Sem Conexão!", Snackbar.LENGTH_SHORT).show();

        }else {

            progressDialog.setMessage("Aguarde...");
            progressDialog.show();

            doRequest();
        }
    }

    @Override
    public void onClickListener(View view, int position) {

        Intent intentDetails = new Intent(MainActivity.this, DetailActivity.class);

        intentDetails.putExtra("product", products.get(position));

        startActivity(intentDetails);

    }
}
