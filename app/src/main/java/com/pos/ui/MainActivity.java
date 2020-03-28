package com.pos.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.pos.R;
import com.pos.domain.inventory.Inventory;
import com.pos.domain.inventory.Product;
import com.pos.domain.inventory.ProductCatalog;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.component.UpdatableFragment;
import com.pos.ui.inventory.InventoryFragment;
import com.pos.ui.sale.ReportFragment;
import com.pos.ui.sale.SaleFragment;

import java.util.Locale;


@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {
    private Resources res;
    private static boolean SDK_SUPPORTED;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ProductCatalog productCatalog;
    private Product product;

    private String productId;

    @SuppressLint("NewApi")

    private void initiateActionBar() {
        if (SDK_SUPPORTED ) {

           // ActionBar actionBar = getActionBar();
            ActionBar actionBar = getSupportActionBar();
            if(actionBar == null)
                System.out.println("ActionBar NULL");
            //ActionBar actionBar = getSupportActionBar();
            //ActionBar.TabListener tabListener = new ActionBar.TabListener()
            // {



            // };

           // actionBar.addTab(actionBar.newTab().setText(res.getString(R.string.inventory))
            //      .setTabListener(tabListener), 0, false);


           // actionBar.setDisplayHomeAsUpEnabled(true);
           // actionBar.setTitle("test title");
           // actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));

        }

    }

    public void optionOnClickHandler(View view) {
        String id = view.getTag().toString();
        productId = id;
        try {
            productCatalog = Inventory.getInstance().getProductCatalog();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }
        product = productCatalog.getProductById(Integer.parseInt(productId));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        res = getResources();
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        super.onCreate(savedInstanceState);
        System.out.println("SDK_INT="+Build.VERSION.SDK_INT+"_HONEYCOMB="+Build.VERSION_CODES.HONEYCOMB);
        SDK_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        initiateActionBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager, res);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
         MenuInflater minflater = getMenuInflater();
        minflater.inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.lang_en:
                setLanguage("en");
                return true;
            case R.id.lang_th:
                setLanguage("th");
                return true;
            case R.id.lang_jp:
                setLanguage("jp");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public ViewPager getViewPager() {
        return viewPager;
    }
    private void setLanguage(String localeString) {
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
      // LanguageController.getInstance().setLanguage(localeString); // for database lang
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
class PagerAdapter extends FragmentStatePagerAdapter {
//class PagerAdapter extends FragmentPagerAdapter {

    private UpdatableFragment[] fragments;
    private String[] fragmentNames;

   public PagerAdapter(FragmentManager fragmentManager, Resources res) {
       super(fragmentManager);
       UpdatableFragment reportFragment = new ReportFragment();
       UpdatableFragment saleFragment = new SaleFragment(reportFragment);
       UpdatableFragment inventoryFragment = new InventoryFragment(
               saleFragment);


       fragments = new UpdatableFragment[]{inventoryFragment, saleFragment,
               reportFragment };
       fragmentNames = new String[] { res.getString(R.string.inventory),
               res.getString(R.string.sale),
               res.getString(R.string.report) };


   }
    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int i) {
        return fragmentNames[i];
    }


    public void update(int index) {
        fragments[index].update();
    }

}