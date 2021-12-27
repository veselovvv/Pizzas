package com.veselovvv.pizzas;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PizzaActivity extends Activity {

    public static final String EXTRA_PIZZAID = "pizzaId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza);

        int pizzaId = (Integer) getIntent().getExtras().get(EXTRA_PIZZAID);

        SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper(this);
        
        try {
            SQLiteDatabase db = pizzasDatabaseHelper.getReadableDatabase();
            
            Cursor cursor = db.query("PIZZA",
                    new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVORITE"},
                    "_id = ?", new String[]{Integer.toString(pizzaId)}, null, null, null);

            if (cursor.moveToFirst()) {
                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
                boolean isFavorite = (cursor.getInt(3) == 1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);

                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                CheckBox favorite = (CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);
            }
            
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onFavoriteClicked(View view) {
        int pizzaId = (Integer) getIntent().getExtras().get(EXTRA_PIZZAID);

        new UpdatePizzaTask().execute(pizzaId);
    }

    private class UpdatePizzaTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues pizzaValues;

        protected void onPreExecute() {
            CheckBox favorite = (CheckBox) findViewById(R.id.favorite);
            pizzaValues = new ContentValues();
            pizzaValues.put("FAVORITE", favorite.isChecked());
        }

        protected Boolean doInBackground(Integer... pizzas) {
            int pizzaId = pizzas[0];
            SQLiteOpenHelper pizzasDatabaseHelper = new PizzasDatabaseHelper(PizzaActivity.this);
            
            try {
                SQLiteDatabase db = pizzasDatabaseHelper.getWritableDatabase();
                db.update("PIZZA", pizzaValues, "_id = ?", new String[] {Integer.toString(pizzaId)});
                db.close();
                
                return true;
            } catch(SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(PizzaActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
