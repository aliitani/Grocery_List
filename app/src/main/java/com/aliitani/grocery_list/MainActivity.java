package com.aliitani.grocery_list;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    SQLiteDatabase myDataBase;
    ArrayList<String> Groceries;
    ArrayAdapter<String> mAdapater;
    ListView myListView;
    RelativeLayout addGroceriesLayout;
    EditText groceriesText;
    Button groceryPanelButtton, RemoveButton;
    InputMethodManager inputMgr;
    TextView noItemDescription;


    public void addGrocery(View view) {
        groceryPanelButtton = (Button) view;
        addGroceriesLayout = (RelativeLayout) findViewById(R.id.relativeLayoutGroceries);
        inputMgr = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        groceriesText = (EditText) findViewById(R.id.groceriesText);
        relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        RemoveButton = (Button) findViewById(R.id.clearButton);
        RemoveButton.setVisibility(View.GONE);

        if (groceryPanelButtton.getText().toString().equals("Add")) {

            if (!groceriesText.getText().toString().equals("")) {
                Groceries.add(groceriesText.getText().toString());
                String groceryname = groceriesText.getText().toString();

                myDataBase.execSQL("INSERT INTO groceries (name) VALUES ('" + groceryname + "')");

                myListView.setAdapter(mAdapater);
                addGroceriesLayout.setVisibility(View.GONE);
                groceriesText.setText("");

                inputMgr.hideSoftInputFromWindow(groceriesText.getWindowToken(), 0);
            } else {
                inputMgr.hideSoftInputFromWindow(groceriesText.getWindowToken(),0 );

                Snackbar snackbar = Snackbar.make(relativeLayout,"Please enter an item first.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }


        }
        if (groceryPanelButtton.getText().toString().equals("Cancel")) {
            addGroceriesLayout.setVisibility(View.GONE);
            if (Groceries.isEmpty()) {
                TextView noItemDescription = (TextView) findViewById(R.id.NoitemDescription);
                noItemDescription.setVisibility(View.GONE);

            }
            inputMgr.hideSoftInputFromInputMethod(groceriesText.getWindowToken(), 0);
        }

        RemoveButton.setVisibility(View.VISIBLE);

    }

    public void GroceryLayout(View view) {
        noItemDescription = (TextView) findViewById(R.id.NoitemDescription);
        RemoveButton = (Button) findViewById(R.id.clearButton);
        RemoveButton.setVisibility(View.GONE);
        noItemDescription.setVisibility(View.GONE);
        addGroceriesLayout = (RelativeLayout) findViewById(R.id.relativeLayoutGroceries);
        addGroceriesLayout.setVisibility(View.VISIBLE);
    }

    public void clearList(View view) {
        myDataBase.delete("Groceries",null,null);
        mAdapater.clear();
        Groceries.clear();
        myListView.setAdapter(mAdapater);
        if (Groceries.isEmpty()) {
            noItemDescription = (TextView) findViewById(R.id.NoitemDescription);
            noItemDescription.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myListView = (ListView) findViewById(R.id.listView);
        noItemDescription = (TextView) findViewById(R.id.NoitemDescription);
        Groceries = new ArrayList<String>();


        try {
            myDataBase = this.openOrCreateDatabase("Groceries",MODE_PRIVATE, null);

            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS groceries (name VARCHAR)");

            Cursor getbackCursor = myDataBase.rawQuery("SELECT * FROM groceries", null);

            int nameindex = getbackCursor.getColumnIndex("name");

            getbackCursor.moveToFirst();

            while (getbackCursor != null){

                Groceries.add(getbackCursor.getString(nameindex));

                getbackCursor.moveToNext();
            }


        } catch(Exception e) {
            e.printStackTrace();
        }

        if(Groceries.isEmpty()) {
            noItemDescription.setVisibility(View.VISIBLE);
        } else {
            noItemDescription.setVisibility(View.GONE);
        }
        mAdapater = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Groceries);
        myListView.setAdapter(mAdapater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
