package com.pos.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import com.pos.R;
import com.pos.domain.DateTimeStrategy;
import com.pos.domain.LanguageController;
import com.pos.domain.inventory.Inventory;
import com.pos.domain.sale.Register;
import com.pos.domain.sale.SaleLedger;
import com.pos.techicalservices.AndroidDatabase;
import com.pos.techicalservices.Database;
import com.pos.techicalservices.DatabaseContents;
import com.pos.techicalservices.DatabaseExecutor;
import com.pos.techicalservices.inventory.InventoryDao;
import com.pos.techicalservices.inventory.InventoryDaoAndroid;
import com.pos.techicalservices.sale.SaleDao;
import com.pos.techicalservices.sale.SaleDaoAndroid;

import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Locale;


//public class WelcomePosScreen extends AppCompatActivity {
public class WelcomePosScreen extends AppCompatActivity {
    private boolean gone = false;
    private static final long SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
       //         WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_pos_screen);
        initiateUI(savedInstanceState);
        initiateCoreApp();
    }

    private void initiateCoreApp() {

        Database database = new AndroidDatabase(this);
        InventoryDao inventoryDao = new InventoryDaoAndroid(database);
        SaleDao saleDao = new SaleDaoAndroid(database);
        DatabaseExecutor.setDatabase(database);

        LanguageController.setDatabase(database);
       // boolean bresult1 = database.SqltabbleIsExist( DatabaseContents.TABLE_test.toString());
      //  System.out.println("create table saleexist="+bresult1);
       // boolean bresult = database.SqltabbleIsExist( DatabaseContents.TABLE_STOCK.toString());
      // System.out.println("create table TABLE_STOCK exist="+bresult);

        Inventory.setInventoryDao(inventoryDao);
        Register.setSaleDao(saleDao);
        SaleLedger.setSaleDao(saleDao);
        //boolean  btablesale = database.SqltabbleIsExist(DatabaseContents.TABLE_SALE.toString());
        //boolean  btablecatalog = database.SqltabbleIsExist(DatabaseContents.TABLE_PRODUCT_CATALOG.toString());
        //boolean  btablectest = database.SqltabbleIsExist("testdb");

        //System.out.println("btablesale="+btablesale);
        //System.out.println("btablecatalog="+btablecatalog);

       // System.out.println("btablectest="+btablectest);

        DateTimeStrategy.setLocale("th", "TH");
        setLanguage(LanguageController.getInstance().getLanguage());

        Log.d("Core App", "INITIATE");
    }
    private void setLanguage(String localeString) {
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
    /**
     * Go.
     */
    private void go() {
        gone = true;
        //Intent(Context packageContext, Class<?> cls)
        Intent newAct = new Intent(WelcomePosScreen.this,MainActivity.class);
        startActivity(newAct);
        WelcomePosScreen.this.finish();

    }
    private void initiateUI(Bundle savedInstanceState)
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gone) go();
            }
        }, SPLASH_TIMEOUT);
    }

}
