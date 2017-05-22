package com.stone.lfernandosantos.storewars;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stone.lfernandosantos.storewars.models.Product;
import com.stone.lfernandosantos.storewars.models.ProductDAO;

public class DetailActivity extends AppCompatActivity {

    Product product;

    private ImageView imgProduct;
    private TextView txtTitle;
    private TextView txtSeller;
    private TextView txtPrice;
    private Button btnComprar;
    private Button btnCarrinho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        imgProduct = (ImageView) findViewById(R.id.imgProductDetail);
        txtTitle = (TextView) findViewById(R.id.txtViewTitleDetail);
        txtSeller = (TextView) findViewById(R.id.txtViewSellerDetail);
        txtPrice = (TextView) findViewById(R.id.txtViewPriceDetail);
        btnComprar = (Button) findViewById(R.id.btnComprar);
        btnCarrinho = (Button) findViewById(R.id.btnAddCarrinho);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null){

            Glide.with(this).load(product.thumbnailHd).into(imgProduct);
            txtTitle.setText(product.title);
            txtSeller.setText(product.seller);
            txtPrice.setText("R$ "+ product.getPrice());

        }


        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDAO dao = new ProductDAO(DetailActivity.this);
                product.carrinho = "1";
                dao.saveProduct(product);
                dao.close();

                Intent goBuyIntent = new Intent(DetailActivity.this, CarrinhoActivity.class);
                startActivity(goBuyIntent);
            }
        });

        btnCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDAO dao = new ProductDAO(DetailActivity.this);
                product.carrinho = "1";
                dao.saveProduct(product);
                dao.close();
                Snackbar.make(btnCarrinho, "Item adcionado!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}
