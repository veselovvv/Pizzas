package com.veselovvv.pizzas;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
    protected static final String EXTRA_PIZZA_ID = "pizzaId";

    protected void showDatabaseUnavailableToast(Context context) {
        Toast.makeText(context, R.string.database_unavailable, Toast.LENGTH_SHORT).show();
    }
}
