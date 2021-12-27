package com.veselovvv.pizzas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class PizzasDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "pizzas";
    private static final int DB_VERSION = 2;

    PizzasDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertPizza(SQLiteDatabase db, String name, String description, int resourceId) {
        ContentValues pizzaValues = new ContentValues();
        pizzaValues.put("NAME", name);
        pizzaValues.put("DESCRIPTION", description);
        pizzaValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("PIZZA", null, pizzaValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE PIZZA (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");

            insertPizza(db, "Neapolitan Pizza", "Neapolitan is the original pizza. " +
                    "This delicious pie dates all the way back to 18th century in Naples, Italy. " +
                    "During this time, the poorer citizens of this seaside city frequently purchased " +
                    "food that was cheap and could be eaten quickly. Luckily for them, " +
                    "Neapolitan pizza was affordable and readily available through numerous street vendors.",
                    R.drawable.neapolitan_pizza);

            insertPizza(db, "Chicago Pizza", "Chicago pizza, also commonly referred " +
                    "to as deep-dish pizza, gets its name from the city it was invented in. " +
                    "During the early 1900’s, Italian immigrants in the windy city were searching for " +
                    "something similar to the Neapolitan pizza that they knew and loved. " +
                    "Instead of imitating the notoriously thin pie, Ike Sewell had something else in mind. " +
                    "He created a pizza with a thick crust that had raised edges, similar to a pie, " +
                    "and ingredients in reverse, with slices of mozzarella lining the dough followed " +
                    "by meat, vegetables, and then topped with a can of crushed tomatoes. " +
                    "This original creation led Sewell to create the now famous chain restaurant, " +
                    "Pizzeria Uno.", R.drawable.chicago_pizza);

            insertPizza(db, "New York-Style Pizza", "With its characteristic large, " +
                    "foldable slices and crispy outer crust, New York-style pizza is one of America’s " +
                    "most famous regional pizza types. Originally a variation of Neapolitan-style pizza, " +
                    "the New York slice has taken on a fame all its own, with some saying its unique " +
                    "flavor has to do with the minerals present in New York’s tap water supply.",
                    R.drawable.new_york_style_pizza);

            insertPizza(db, "Sicilian Pizza", "Sicilian pizza, also known as \"sfincione,\" " +
                    "provides a thick cut of pizza with pillowy dough, a crunchy crust, and robust tomato sauce. " +
                    "This square-cut pizza is served with or without cheese, and often with " +
                    "the cheese underneath the sauce to prevent the pie from becoming soggy. " +
                    "Sicilian pizza was brought to America in the 19th century by Sicilian immigrants " +
                    "and became popular in the United States after the Second World War.",
                    R.drawable.sicilian_pizza);

            insertPizza(db, "Greek Pizza", "Greek pizza was created by Greek " +
                    "immigrants who came to America and were introduced to Italian pizza. Greek-style pizza, " +
                    "especially popular in the New England states, features a thick and chewy crust " +
                    "cooked in shallow, oiled pans, resulting in a nearly deep-fried bottom. " +
                    "While this style has a crust that is puffier and chewier than thin crust pizzas, " +
                    "it’s not quite as thick as a deep-dish or Sicilian crust.", R.drawable.greek_pizza);

            insertPizza(db, "California Pizza", "California pizza, or gourmet pizza, " +
                    "is known for its unusual ingredients. This pizza got its start back in the late " +
                    "1970’s when Chef Ed LaDou began experimenting with pizza recipes in the classic " +
                    "Italian restaurant, Prego. He created a pizza with mustard, ricotta, pate, and " +
                    "red pepper, and by chance, served it to Wolfgang Puck. Impressed with LaDou’s " +
                    "innovative pie, Puck invited him to be a head pizza chef at his restaurant. " +
                    "It was here that LaDou came up with over 250 unique pizza recipes that eventually " +
                    "formed the menu of the chain restaurant California Pizza Kitchen.",
                    R.drawable.california_pizza);

            insertPizza(db, "Detroit Pizza", "Reflecting the city’s deep ties to the " +
                    "auto industry, Detroit-style pizza was originally baked in a square automotive " +
                    "parts pan in the 1940’s. Detroit pizza is first topped with pepperoni, followed " +
                    "by brick cheese which is spread to the very edges of the pan, yielding a caramelized " +
                    "cheese perimeter. Sauce is then spooned over the pizza, an order similar to " +
                    "Chicago-style pizza. This pizza features a thick, extra crispy crust that is " +
                    "tender and airy on the inside.", R.drawable.detroit_pizza);

            insertPizza(db, "St. Louis Pizza", "St. Louis pizza features a thin crust " +
                    "with a cracker-like consistency that is made without yeast. Due to the crispy crust, " +
                    "St. Louis pizza is usually cut into three- or four-inch rectangles, known as \"party\" " +
                    "or \"tavern\" cut. This pizza features Provel processed cheese, which is a gooey " +
                    "combination of cheddar, Swiss, and provolone cheeses. St. Louis received an influx " +
                    "of Italian immigrants in the 19th century who were looking for employment opportunities. " +
                    "The Italian community, largely from Milan and Sicily, created the St. Louis-style pizza. " +
                    "Its sweet sauce is reminiscent of the Sicilian influence.", R.drawable.st_louis_pizza);
        }
        
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE PIZZA ADD COLUMN FAVORITE NUMERIC;");
        }
    }
}
