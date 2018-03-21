package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";

    private EditText name;
    private EditText price;
    private Button addBtn;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        addBtn = findViewById(R.id.add_btn);

        type = getIntent().getStringExtra(ItemsFragment.TYPE_KEY);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(price.getText()) && !price.getText().toString().endsWith(getString(R.string.ruble))) {
                    String priceFormat = getString(R.string.price, price.getText().toString());
                    price.setText(priceFormat);
                    price.setSelection(price.length() - 1);
                }
                if (price.getText().toString().equals(getString(R.string.ruble))) {
                    price.setText("");
                }

                addBtn.setEnabled(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(price.getText()));
            }
        };

        name.addTextChangedListener(textWatcher);
        price.addTextChangedListener(textWatcher);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = name.getText().toString();
                String itemPrice = price.getText().toString();

                Item item = new Item(itemName, itemPrice, type);
                Intent intent = new Intent();
                intent.putExtra("item", item);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
