package com.stone.lfernandosantos.storewars.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.lfernandosantos.storewars.R;
import com.stone.lfernandosantos.storewars.controlers.ListComprasAdapter;
import com.stone.lfernandosantos.storewars.models.Card;
import com.stone.lfernandosantos.storewars.models.CardDAO;
import com.stone.lfernandosantos.storewars.models.Order;
import com.stone.lfernandosantos.storewars.models.OrderDAO;
import com.stone.lfernandosantos.storewars.models.Product;
import com.stone.lfernandosantos.storewars.models.ProductDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartActivity extends AppCompatActivity {

    private ListView listCompras;
    private Button btnComprar;
    private List<Product> products;

    private TextView cardSaved;
    private RelativeLayout layoutCardSaved;

    private ProgressDialog progressDialog;

    private Double totalPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        progressDialog = new ProgressDialog(this);

        getProducts();

        findViews();

        layoutCardSaved.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                startActivity(new Intent(CartActivity.this, ListCardsActivity.class));
                return false;
            }
        });


        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            CardDAO daoCard = new CardDAO(CartActivity.this);
            List<Card> cards = daoCard.getCards();

            if (cards != null && cards.size() > 0){

                paymentConfirm(cards);

            }else {
                startActivity(new Intent(CartActivity.this, CardDataActivity.class));
            }

            }
        });

        runCardList();
    }

    private void getProducts() {
        products = new ArrayList<>();
        ProductDAO dao = new ProductDAO(this);
        products = dao.getProductsCarrinho();
    }

    private void findViews() {
        btnComprar = (Button) findViewById(R.id.buyBtn);
        listCompras = (ListView) findViewById(R.id.listaCompras);
        cardSaved = (TextView) findViewById(R.id.txtNumCardSaved);
        layoutCardSaved = (RelativeLayout) findViewById(R.id.layoutCardSaved);
    }

    private void paymentConfirm(List<Card> cards) {
        if (products != null && products.size() > 0) {
            totalPayment = 0.0;

            for (Product p : products) {
                totalPayment = totalPayment + Double.valueOf(p.price);
            }

            totalPayment = totalPayment / 100;

            LayoutInflater inflater = CartActivity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm_payment, null);

            ImageView imgBandeira = (ImageView) view.findViewById(R.id.imgBandeiraDialogPaymentConfirm);
            TextView txtTotalPayment = (TextView) view.findViewById(R.id.txtTotalPaymentDialog);
            TextView txtNumCard = (TextView) view.findViewById(R.id.txtCardNumPaymentDialog);
            TextView txtEndEntrega = (TextView) view.findViewById(R.id.txtEndPaymentDialog);

            Glide.with(CartActivity.this).load(R.drawable.ic_bandeira_visa).into(imgBandeira);

            txtTotalPayment.setText("R$ " + String.format("%.2f",totalPayment));
            txtNumCard.setText(" ... " + cards.get(cards.size()-1).numCard);
            txtEndEntrega.setText("Casa");

            new AlertDialog.Builder(CartActivity.this)
                    .setView(view)
                    .setTitle("CONCLUIR A COMPRA?")
                    .setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doPayment(totalPayment);
                        }
                    })
                    .setNegativeButton("cancelar", null)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    private void doPayment(Double total) {
        ProductDAO dao = new ProductDAO(CartActivity.this);
        OrderDAO orderDAO = new OrderDAO(CartActivity.this);

        Random random = new Random();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Order order = new Order();

        order.idOrder = random.nextLong();
        order.date = format.format(date);
        order.total = total;

        //editar products colocando cart = 0

        for (Product p : products){
            p.compra = String.valueOf(order.idOrder);
            dao.saveProduct(p);
        }

        orderDAO.saveOrder(order);
        dao.close();
        orderDAO.close();

    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog.setMessage("Aguarde...");

        if (products != null && products.size()>0) {
            ListComprasAdapter adapter = new ListComprasAdapter(products, this);
            listCompras.setAdapter(adapter);
        }

        runCardList();

        progressDialog.dismiss();
    }

    private void runCardList() {
        CardDAO daoCard = new CardDAO(this);
        List<Card> cards = daoCard.getCards();

        if (cards != null && cards.size() > 0){
            cardSaved.setText("..." + cards.get(cards.size()-1).numCard);
        }else {
            layoutCardSaved.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        runCardList();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        runCardList();
    }
}
