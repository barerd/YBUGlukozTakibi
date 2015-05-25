package com.agrimerkezi.ybuglukoztakibi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Klavuz extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klavuz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_klavuz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.infuzyonDozu) {
            Intent infuzyonDozu = new Intent(this, InfuzyonDozu.class);
            startActivity(infuzyonDozu);
        } else if (id == R.id.bolusDoz) {
            Intent bolusDoz = new Intent(this, BolusDoz.class);
            startActivity(bolusDoz);
        } else if (id == R.id.tedaviBaslangici) {
            Intent tedaviBaslangici = new Intent(this, TedaviBaslangici.class);
            startActivity(tedaviBaslangici);
        }

        return super.onOptionsItemSelected(item);
    }
}
