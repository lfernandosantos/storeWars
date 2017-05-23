package com.stone.lfernandosantos.storewars.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stone.lfernandosantos.storewars.R;
import com.stone.lfernandosantos.storewars.models.Card;
import com.stone.lfernandosantos.storewars.models.CardDAO;

public class CardDataActivity extends AppCompatActivity {

    private Button btnDadosCard;
    private EditText bandeira;
    private EditText numCard;
    private EditText nome;
    private EditText cardMonth;
    private EditText cardYear;
    private EditText cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);

        btnDadosCard = (Button) findViewById(R.id.btnSaveCard);

        numCard = (EditText) findViewById(R.id.edtCard);
        nome = (EditText) findViewById(R.id.edtNomeCard);
        cardMonth = (EditText) findViewById(R.id.edtCardMonth);
        cardYear = (EditText) findViewById(R.id.edtCardYear);
        cvv = (EditText) findViewById(R.id.edtCVVCard);

        btnDadosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fazer validação dos campos

                Card card = new Card();
                CardDAO dao = new CardDAO(CardDataActivity.this);

                card.bandeira = "VISA";
                card.nome = nome.getText().toString();
                String stringNumCard = numCard.getText().toString();

                String  lastFour = stringNumCard.substring(stringNumCard.length() - 4);
                card.numCard = lastFour;

                card.validade = cardMonth.getText().toString() + "/" + cardYear.getText().toString();
                card.cvv = cvv.getText().toString();

                dao.saveCard(card);
                dao.close();

                finish();
            }
        });

    }
}
