package com.veselovvv.pizzas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

interface PizzasDatabaseHelper {
    void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion);
    void insertPizzas(SQLiteDatabase db);
    void insertPizza(SQLiteDatabase db, int nameResId, int descriptionResId, int resourceId);

    class Base extends SQLiteOpenHelper implements PizzasDatabaseHelper {
        private static final String DB_NAME = "pizzas";
        private static final int DB_VERSION = 2;
        private static final String NAME_KEY = "NAME";
        private static final String DESCRIPTION_KEY = "DESCRIPTION";
        private static final String IMAGE_RESOURCE_ID_KEY = "IMAGE_RESOURCE_ID";
        private static final String TABLE_NAME = "PIZZA";

        private final Context context;

        Base(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            updateMyDatabase(db, 0, DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            updateMyDatabase(db, oldVersion, newVersion);
        }

        @Override
        public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 1) {
                insertPizzas(db);
                if (oldVersion < 2) db.execSQL("ALTER TABLE PIZZA ADD COLUMN FAVORITE NUMERIC;");
            }
        }

        @Override
        public void insertPizzas(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE PIZZA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");

            insertPizza(
                    db,
                    R.string.neapolitan_pizza,
                    R.string.neapolitan_pizza_description,
                    R.drawable.neapolitan_pizza
            );

            insertPizza(
                    db,
                    R.string.chicago_pizza,
                    R.string.chicago_pizza_description,
                    R.drawable.chicago_pizza
            );

            insertPizza(
                    db,
                    R.string.new_york_style_pizza,
                    R.string.new_york_style_pizza_description,
                    R.drawable.new_york_style_pizza
            );

            insertPizza(
                    db,
                    R.string.sicilian_pizza,
                    R.string.sicilian_pizza_description,
                    R.drawable.sicilian_pizza
            );

            insertPizza(
                    db,
                    R.string.greek_pizza,
                    R.string.greek_pizza_description,
                    R.drawable.greek_pizza
            );

            insertPizza(
                    db,
                    R.string.california_pizza,
                    R.string.california_pizza_description,
                    R.drawable.california_pizza
            );

            insertPizza(
                    db,
                    R.string.detroit_pizza,
                    R.string.detroit_pizza_description,
                    R.drawable.detroit_pizza
            );

            insertPizza(
                    db,
                    R.string.st_louis_pizza,
                    R.string.st_louis_pizza_description,
                    R.drawable.st_louis_pizza
            );
        }

        @Override
        public void insertPizza(SQLiteDatabase db, int nameResId, int descriptionResId, int resourceId) {
            ContentValues pizzaValues = new ContentValues();
            pizzaValues.put(NAME_KEY, context.getString(nameResId));
            pizzaValues.put(DESCRIPTION_KEY, context.getString(descriptionResId));
            pizzaValues.put(IMAGE_RESOURCE_ID_KEY, resourceId);
            db.insert(TABLE_NAME, null, pizzaValues);
        }
    }
}
