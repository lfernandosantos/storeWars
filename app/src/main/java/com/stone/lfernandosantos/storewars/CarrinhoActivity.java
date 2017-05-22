package com.stone.lfernandosantos.storewars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stone.lfernandosantos.storewars.controlers.ListaComprasAdapter;
import com.stone.lfernandosantos.storewars.models.Card;
import com.stone.lfernandosantos.storewars.models.CardDAO;
import com.stone.lfernandosantos.storewars.models.Product;
import com.stone.lfernandosantos.storewars.models.ProductDAO;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private ListView listCompras;
    private Button btnComprar;
    private List<Product> products;

    private TextView cardSaved;
    private RelativeLayout layoutCardSaved;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        progressDialog = new ProgressDialog(this);

        products = new ArrayList<>();
        ProductDAO dao = new ProductDAO(this);
        products = dao.getProductsCarrinho();

        btnComprar = (Button) findViewById(R.id.buyBtn);
        listCompras = (ListView) findViewById(R.id.listaCompras);

        cardSaved = (TextView) findViewById(R.id.txtNumCardSaved);
        layoutCardSaved = (RelativeLayout) findViewById(R.id.layoutCardSaved);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            CardDAO daoCard = new CardDAO(CarrinhoActivity.this);
            List<Card> cards = daoCard.getCards();

            if (cards != null && cards.size() > 0){

            }else {
                startActivity(new Intent(CarrinhoActivity.this, CardDataActivity.class));
            }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog.setMessage("Aguarde...");

        if (products != null && products.size()>0) {
            ListaComprasAdapter adapter = new ListaComprasAdapter(products, this);
            listCompras.setAdapter(adapter);
        }

        CardDAO daoCard = new CardDAO(this);
        List<Card> cards = daoCard.getCards();

        if (cards != null && cards.size() > 0){
            cardSaved.setText(cards.get(cards.size()-1).numCard);
        }else {
            layoutCardSaved.setVisibility(View.INVISIBLE);
        }

        progressDialog.dismiss();
    }
}
