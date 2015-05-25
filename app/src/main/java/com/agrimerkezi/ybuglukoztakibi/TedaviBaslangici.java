package com.agrimerkezi.ybuglukoztakibi;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;


public class TedaviBaslangici extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tedavi_baslangici);
    }

    public void tedaviyiBelirle(View v) {
        EditText simdikiKS = (EditText)findViewById(R.id.simdikiKS);
        EditText boy = (EditText)findViewById(R.id.boy);
        EditText kilo = (EditText)findViewById(R.id.kilo);
        Switch cinsiyet = (Switch)findViewById(R.id.cinsiyet);
        Switch travma = (Switch)findViewById(R.id.travma);
        RadioButton tip_1_dm = (RadioButton)findViewById(R.id.tip_1_dm);
        RadioButton tip_2_dm = (RadioButton)findViewById(R.id.tip_2_dm);
        RadioButton dm_yok = (RadioButton)findViewById(R.id.dm_yok);

        int iSimdikiKS = Integer.parseInt(simdikiKS.getText().toString());
        int iBoy = Integer.parseInt(boy.getText().toString());
        int iKilo = Integer.parseInt(kilo.getText().toString());

        String tedavi_onerisi = "";

        if (tip_1_dm.isChecked()) {
//        total insülin dozu = düzeltilmiş kilo * 0.4
//        saatlik insülin dozu = total insülin dozu / 24
            double saatlik_insulin_dozu = ((duzeltilmisKiloHesapla(idealKiloHesapla(iBoy, cinsiyet), iKilo) * 0.55) / 24);
            int gik_dozu = gikDozuHesapla(saatlik_insulin_dozu);
            int int_saatlik_insulin_dozu = (int) saatlik_insulin_dozu;
            double sonuc = gerekenDozuHesapla(duzeltilmisKiloHesapla(idealKiloHesapla(iBoy, cinsiyet), iKilo), (iSimdikiKS - 110));
            int iSonuc = (int) sonuc;
            tedavi_onerisi = "Hasta beslenmiyorsa " + Integer.toString(int_saatlik_insulin_dozu) + " ml/saat insülin infüzyonu ve\n" + gik_dozu + " ml/saat GİK solüsyonu başla,\nve 6x1 kan şekeri takibine başla\n\n" +
            "Hasta besleniyorsa yukarıdaki tedaviye ek olarak " + Integer.toString(iSonuc) + " IU Humulin-R IV bolus yap";
        } else if (tip_2_dm.isChecked()) {
            if (iSimdikiKS >= 117 && iSimdikiKS < 144) {
                tedavi_onerisi = "Hasta besleniyorsa 1 ml/saat insülin infüzyonu başla,\n4x1 kan şekeri takibine başla";
            } else if (iSimdikiKS >= 143 && iSimdikiKS < 180) {
                tedavi_onerisi = "Hasta besleniyorsa 1 ml/saat insülin infüzyonu başla,\n5 IU Humulin-R IV bolus yap,\n4x1 kan şekeri takibine başla";
            } else if (iSimdikiKS >= 180 && iSimdikiKS < 234) {
                tedavi_onerisi = "Hasta besleniyorsa 2 ml/saat insülin infüzyonu başla,\n7 IU Humulin-R IV bolus yap,\n4x1 kan şekeri takibine başla";
            } else if (iSimdikiKS >= 234 && iSimdikiKS < 288) {
                tedavi_onerisi = "Hasta besleniyorsa 3 ml/saat insülin infüzyonu başla,\n10 IU Humulin-R IV bolus yap,\n4x1 kan şekeri takibine başla";
            } else if (iSimdikiKS >= 288 && iSimdikiKS < 360) {
                tedavi_onerisi = "Hasta besleniyorsa 4 ml/saat insülin infüzyonu başla,\n13 IU Humulin-R IV bolus yap,\n4x1 kan şekeri takibine başla";
            } else if (iSimdikiKS >= 360) {
                tedavi_onerisi = "Hasta besleniyorsa 5 ml/saat insülin infüzyonu başla,\n15 IU Humulin-R IV bolus yap,\n4x1 kan şekeri takibine başla";
            }
        } else if (dm_yok.isChecked()) {
            double sonuc = gerekenDozuHesapla(duzeltilmisKiloHesapla(idealKiloHesapla(iBoy, cinsiyet), iKilo), (iSimdikiKS - 110));
            int iSonuc = (int) sonuc;
            if (travma.isChecked()) {
                tedavi_onerisi = Integer.toString(iSonuc) + " IU Humulin-R IV bolus yap, 4x1 kan şekeri takibine başla";
            } else {
                tedavi_onerisi = Integer.toString(iSonuc) + " IU Humulin-R IV bolus yap,\n1 saatte 500 ml İzotonik mayi yolla,\n4x1 kan şekeri takibine başla";
            }
        }

        AlertDialog.Builder popup = new AlertDialog.Builder(TedaviBaslangici.this);
        popup.setMessage(tedavi_onerisi);
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

    public int gikDozuHesapla(double saatlik_insulin_dozu) {
        return (int) (500 / (6 / (4 * saatlik_insulin_dozu / 5)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tedavi_baslangici, menu);
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
        } else if (id == R.id.klavuz) {
            Intent klavuz = new Intent(this, Klavuz.class);
            startActivity(klavuz);
        }

        return super.onOptionsItemSelected(item);
    }
}
