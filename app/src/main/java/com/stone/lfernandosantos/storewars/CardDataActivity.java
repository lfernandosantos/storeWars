package com.stone.lfernandosantos.storewars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stone.lfernandosantos.storewars.models.Card;
import com.stone.lfernandosantos.storewars.models.CardDAO;

public class CardDataActivity extends AppCompatActivity {

    private Button btnDadosCard;
    private TextView bandeira;
    private TextView numCard;
    private TextView nome;
    private TextView validade;
    private TextView cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);

        btnDadosCard = (Button) findViewById(R.id.btnSaveCard);

        numCard = (TextView) findViewById(R.id.edtCard);
        nome = (TextView) findViewById(R.id.edtNomeCard);
        validade = (TextView) findViewById(R.id.edtValidadeCard);
        cvv = (TextView) findViewById(R.id.edtCVVCard);


        btnDadosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fazer validação dos campos

                Card card = new Card();
                CardDAO dao = new CardDAO(CardDataActivity.this);

                card.bandeira = "VISA";
                card.nome = nome.getText().toString();
                card.numCard = numCard.getText().toString();
                card.validade = validade.getText().toString();
                card.cvv = cvv.getText().toString();

                dao.saveCard(card);
                dao.close();

                 finish();
            }
        });

    }
}
