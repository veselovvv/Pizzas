package com.veselovvv.pizzas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class PizzaActivity extends BaseActivity {
    private static final String FAVORITE_KEY = "FAVORITE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        PizzasDatabaseHelper.Base pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(this); // TODO dry

        try {
            SQLiteDatabase database = pizzasDatabaseHelper.getReadableDatabase(); // TODO dry
            Cursor cursor = pizzasDatabaseHelper.getPizzaCursor(database, getPizzaId());

            if (cursor.moveToFirst()) updateUI(cursor);
            cursor.close();
            database.close();
        } catch (SQLiteException e) {
            showDatabaseUnavailableToast(this);
        }
    }

    Integer getPizzaId() {
        return (Integer) getIntent().getExtras().get(EXTRA_PIZZA_ID);
    }

    void updateUI(Cursor cursor) {
        String nameText = cursor.getString(0);
        String descriptionText = cursor.getString(1);
        int photoId = cursor.getInt(2);
        boolean isFavorite = (cursor.getInt(3) == 1);

        TextView nameTextView = findViewById(R.id.name);
        nameTextView.setText(nameText);

        TextView descriptionTextView = findViewById(R.id.description);
        descriptionTextView.setText(descriptionText);

        ImageView photoImageView = findViewById(R.id.photo);
        photoImageView.setImageResource(photoId);
        photoImageView.setContentDescription(nameText);

        CheckBox favoriteCheckBox = findViewById(R.id.favorite);
        favoriteCheckBox.setChecked(isFavorite);
    }

    public void onFavoriteClicked(View view) {
        new UpdatePizzaTask().execute(getPizzaId());
    }

    private class UpdatePizzaTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues pizzaValues;

        protected void onPreExecute() {
            CheckBox favorite = findViewById(R.id.favorite);
            pizzaValues = new ContentValues();
            pizzaValues.put(FAVORITE_KEY, favorite.isChecked());
        }

        protected Boolean doInBackground(Integer... pizzas) {
            int pizzaId = pizzas[0];
            PizzasDatabaseHelper.Base pizzasDatabaseHelper = new PizzasDatabaseHelper.Base(PizzaActivity.this); // TODO dry

            try {
                SQLiteDatabase database = pizzasDatabaseHelper.getWritableDatabase();
                pizzasDatabaseHelper.updateDatabase(database, pizzaValues, pizzaId);
                database.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) showDatabaseUnavailableToast(PizzaActivity.this);
        }
    }
}
