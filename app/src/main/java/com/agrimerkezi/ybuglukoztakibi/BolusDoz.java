package com.agrimerkezi.ybuglukoztakibi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;


public class BolusDoz extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolus_doz);
    }

    public void bolusDozHesapla(View v) {
        EditText simdikiKS = (EditText)findViewById(R.id.simdikiKS);
        EditText boy = (EditText)findViewById(R.id.boy);
        EditText kilo = (EditText)findViewById(R.id.kilo);
        Switch cinsiyet = (Switch)findViewById(R.id.cinsiyet);

        int iSimdikiKS = Integer.parseInt(simdikiKS.getText().toString());
        int iBoy = Integer.parseInt(boy.getText().toString());
        int iKilo = Integer.parseInt(kilo.getText().toString());

        double sonuc = gerekenDozuHesapla(duzeltilmisKiloHesapla(idealKiloHesapla(iBoy, cinsiyet), iKilo), (iSimdikiKS - 110));
        int iSonuc = (int) sonuc;
        String aciklama = Integer.toString(iSonuc) + " IU Humulin-R IV bolus yap";

        AlertDialog.Builder popup = new AlertDialog.Builder(BolusDoz.this);
        popup.setMessage(aciklama);
        popup.show();
    }

    public double idealKiloHesapla(int boy, Switch cinsiyet) {
        double idBW;
        if (cinsiyet.isChecked() && boy > 150) {
            idBW = 50 + ((boy - 150) * 2.3);
        } else if (cinsiyet.isChecked() && boy <= 150) {
            idBW = 50;
        } else if (boy > 150) {
            idBW = 45.5 + ((boy - 150) * 2.3);
        } else {
            idBW = 45.5;
        }
        return idBW;
    }

    public double duzeltilmisKiloHesapla(double idBW, int iKilo) {
        return idBW + (0.4 * (iKilo - idBW));
    }

    public double gerekenDozuHesapla(double adjBW, int fark) {
//        fark: 110 mg/dl uzeri kan sekeri
        return ((fark * 0.55) / (1800 / adjBW));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bolus_doz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.klavuz) {
            Intent klavuz = new Intent(this, Klavuz.class);
            startActivity(klavuz);
        } else if (id == R.id.infuzyonDozu) {
            Intent infuzyonDozu = new Intent(this, InfuzyonDozu.class);
            startActivity(infuzyonDozu);
        } else if (id == R.id.tedaviBaslangici) {
            Intent tedaviBaslangici = new Intent(this, TedaviBaslangici.class);
            startActivity(tedaviBaslangici);
        }

        return super.onOptionsItemSelected(item);
    }
}
