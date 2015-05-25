package com.agrimerkezi.ybuglukoztakibi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


public class InfuzyonDozu extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infuzyon_dozu);
    }

    public void altiSaatlikDozHesapla(View v) {
        EditText simdikiKS = (EditText)findViewById(R.id.simdikiKS);
        EditText oncekiKS = (EditText)findViewById(R.id.oncekiKS);
        CheckBox dopamin = (CheckBox)findViewById(R.id.dopamin);
        CheckBox steroid = (CheckBox)findViewById(R.id.steroid);
        CheckBox enfeksiyon = (CheckBox)findViewById(R.id.enfeksiyon);

        int iSimdikiKS = Integer.parseInt(simdikiKS.getText().toString());
        int iOncekiKS = Integer.parseInt(oncekiKS.getText().toString());
        int fark = iSimdikiKS - iOncekiKS;

        String aciklama = tedaviNe(dozHesapla(iSimdikiKS, fark, dopamin, steroid, enfeksiyon));

        AlertDialog.Builder popup = new AlertDialog.Builder(InfuzyonDozu.this);
        popup.setMessage(aciklama);
        popup.show();
    }

    private String tedaviNe(double doz) {
        String tedavi = null;
        if (doz == 1000) {
            tedavi = "200 ml %5 dekstroz ver.\n" +
                    "30 dakika sonra kan şekerini kontrol et.";
        } else if (doz == 500) {
            tedavi = "100 ml %5 dekstroz ver.\n" +
                    "30 dakika sonra kan şekerini kontrol et.";
        } else if (doz == 0) {
            tedavi = "Ek insülin yapma, infüzyon hızını değiştirme.";
        } else if (doz < 0) {
            tedavi = "İnfüzyon hızını " + Double.toString(Math.abs(doz)) + " ml/s azalt.";
        } else if (doz > 0) {
            tedavi = "İnfüzyon hızını " + Double.toString(Math.abs(doz)) + " ml/s artır.";
        }
        return tedavi;
    }

    private double dozHesapla(int iSimdikiKS, int fark, CheckBox dopamin, CheckBox steroid, CheckBox enfeksiyon) {
        double doz = 0;
//      kan şekeri 54 mg/dl'nin altına düşmüşse
        if (iSimdikiKS < 54) {
            doz = 1000;
//      kan şekeri 72 mg/dl'nin altına düşmüşse
        } else if (iSimdikiKS < 72) {
            doz = 500;
//      kan şekeri 72-110 mg/dl arasındaysa
//      ve kan şekerini yükseltecek faktörler varsa
        } else if (dopamin.isChecked() || steroid.isChecked() || enfeksiyon.isChecked()) {
            // fark 18 mg/dl'nin altındaysa değişiklik yapma
            if (iSimdikiKS <= 110 && Math.abs(fark) < 18) {
                doz = 0;
                // artış 18-36 mg/dl aralığındaysa 0.5 ml/saat artır
            } else if (iSimdikiKS <= 110 && fark >= 18 && fark <= 36) {
                doz = 0.5;
                // düşüş 18-36 mg/dl aralığındaysa 0.5 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark <= -18 && fark >= -36) {
                doz = -0.5;
                // düşüş 37-54 mg/dl aralığındaysa 1 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -36 && fark >= -54) {
                doz = -1;
                // düşüş 55-72 mg/dl aralığındaysa 1.5 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -54 && fark >= -72) {
                return -1.5;
                // düşüş 72 mg/dl'den fazlaysa 2 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -72) {
                doz = -2;
                // artış 36 mg/dl'den fazlaysa 1 ml/saat artır
            } else if (iSimdikiKS <= 110 && fark >= 18 && fark <= 36) {
                doz = 1;
                // kan şekeri 110 mg/dl'nin üzerindeyse
                // düşüş 18 mg/dl'nin altındaysa 1 ml/saat artır
            } else if (iSimdikiKS > 110 && fark < 0 && fark > -18) {
                doz = 1;
                // düşüş 18-36 mg/dl aralığındaysa değişiklik yapma
            } else if (iSimdikiKS > 110 && fark <= -18 && fark >= -36) {
                doz = 0;
                // düşüş 37-72 mg/dl aralığındaysa 1 ml/saat azalt
            } else if (iSimdikiKS > 110 && fark < -36 && fark >= -72) {
                doz = -1;
                // düşüş 72 mg/dl'den fazlaysa 2 ml/saat azalt
            } else if (iSimdikiKS > 110 && fark < -72) {
                doz = -2;
                // artış 36 mg/dl'nin altındaysa 1 ml/saat artır
            } else if (iSimdikiKS > 110 && fark < 36) {
                doz = 1;
                // artış 36-72 mg/dl aralığındaysa 2 ml/saat artır
            } else if (iSimdikiKS > 110 && fark >= 36 && fark <= 72) {
                doz = 2;
                // artış 72 mg/dl'nin üzerindeyse 4 ml/saat artır
            } else if (iSimdikiKS > 110 && fark > 72) {
                doz = 4;
            }
            // kan şekeri 72-110 mg/dl arasındaysa
            // ama kan şekerini yükseltecek faktörler yoksa
        } else // fark 18 mg/dl'nin altındaysa değişiklik yapma
            if (iSimdikiKS <= 110 && Math.abs(fark) < 18) {
                doz = 0;
                // artış 18-36 mg/dl aralığındaysa 0.5 ml/saat artır
            } else if (iSimdikiKS <= 110 && fark >= 18 && fark <= 36) {
                doz = 0.5;
                // düşüş 18-36 mg/dl aralığındaysa 0.5 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark <= -18 && fark >= -36) {
                doz = -0.5;
                // düşüş 37-54 mg/dl aralığındaysa 1 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -36 && fark >= -54) {
                doz = -1;
                // düşüş 55-72 mg/dl aralığındaysa 1.5 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -54 && fark >= -72) {
                doz = -1.5;
                // düşüş 72 mg/dl'den fazlaysa 2 ml/saat azalt
            } else if (iSimdikiKS <= 110 && fark < -72) {
                doz = -2;
                // artış 36 mg/dl'den fazlaysa 1 ml/saat artır
            } else if (iSimdikiKS <= 110 && fark >= 18 && fark <= 36) {
                doz = 1;
                // kan şekeri 110 mg/dl'nin üzerindeyse
                // düşüş 18 mg/dl'nin altındaysa 1 ml/saat artır
            } else if (iSimdikiKS > 110 && fark < 0 && fark > -18) {
                doz = 1;
                // düşüş 18-36 mg/dl aralığındaysa değişiklik yapma
            } else if (iSimdikiKS > 110 && fark <= -18 && fark >= -36) {
                doz = 0;
                // düşüş 37-72 mg/dl aralığındaysa 1 ml/saat azalt
            } else if (iSimdikiKS > 110 && fark < -36 && fark >= -72) {
                doz = -1;
                // düşüş 72 mg/dl'den fazlaysa 2 ml/saat azalt
            } else if (iSimdikiKS > 110 && fark < -72) {
                doz = -2;
                // artış 36 mg/dl'nin altındaysa 1 ml/saat artır
            } else if (iSimdikiKS > 110 && fark < 36) {
                doz = 1;
                // artış 36-72 mg/dl aralığındaysa 2 ml/saat artır
            } else if (iSimdikiKS > 110 && fark >= 36 && fark <= 72) {
                doz = 2;
                // artış 72 mg/dl'nin üzerindeyse 4 ml/saat artır
            } else if (iSimdikiKS > 110 && fark > 72) {
                doz = 4;
            }
        return doz;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_infuzyon_dozu, menu);
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
        } else if (id == R.id.bolusDoz) {
            Intent bolusDoz = new Intent(this, BolusDoz.class);
            startActivity(bolusDoz);
        }

        return super.onOptionsItemSelected(item);
    }
}
