package com.pos.ui.inventory;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.viewpager.widget.ViewPager;

import com.pos.R;
import com.pos.domain.inventory.Inventory;
import com.pos.domain.inventory.Product;
import com.pos.domain.inventory.ProductCatalog;
import com.pos.domain.sale.Register;
import com.pos.techicalservices.DatabaseContents;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.MainActivity;
import com.pos.ui.component.ButtonAdapter;
import com.pos.ui.component.UpdatableFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class InventoryFragment extends UpdatableFragment {


    private ProductCatalog productCatalog;
    private Register register;
    private List<Map<String, String>> inventoryList;
    private UpdatableFragment saleFragment;

    // view component
    private Resources res;
    private MainActivity main;
    private ViewPager viewPager;
    private Button addProductButton;
    private EditText searchBox;
    private Button scanButton;
    private ListView inventoryListView;

    public InventoryFragment(UpdatableFragment saleFragment)
    {
        super();
        this.saleFragment = saleFragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            productCatalog = Inventory.getInstance().getProductCatalog();
            register = Register.getInstance();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.layout_inventory,container,false);

        res = getResources();
        inventoryListView = (ListView) view.findViewById(R.id.productListView);
        addProductButton = (Button) view.findViewById(R.id.addProductButton);
        scanButton = (Button) view.findViewById(R.id.scanButton);
        searchBox = (EditText) view.findViewById(R.id.searchBox);
        main = (MainActivity)getActivity();
        viewPager = main.getViewPager();

        InitUI();
        return view;

    }

    private void InitUI(){
        addProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                showPopup(v);
            }
        });

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int _id = Integer.parseInt(inventoryList.get(position).get("id").toString());
                // add  one sale data
                 register.addItem(productCatalog.getProductById(_id), 1);
               //boolean  btablesale = register.SqltabbleIsExist(DatabaseContents.TABLE_SALE.toString());
               // boolean  btablecatalog = register.SqltabbleIsExist(DatabaseContents.TABLE_PRODUCT_CATALOG.toString());
                //boolean  btablectest = register.SqltabbleIsExist("testdb");

                //System.out.println("btablesale="+btablesale);
                //System.out.println("btablecatalog="+btablecatalog);

                //System.out.println("btablectest="+btablectest);
                saleFragment.update();
                viewPager.setCurrentItem(1);
            }
        });


    }

    public void showPopup(View anchorView) {
      //  System.out.println("class=" +anchorView.getClass());
        AddProductDialogFragment newFragment = new AddProductDialogFragment(InventoryFragment.this);
        newFragment.show(getFragmentManager(), "");
    }
    private void search() {
        String search = searchBox.getText().toString();
        if(search.equals(""))
        {
            showList(productCatalog.getAllProduct());
        }

    }
    private void showList(List<Product> list) {

        inventoryList = new ArrayList<Map<String,String>>();
        for(Product product:list)
        {
            inventoryList.add(product.toMap());
        }
        ButtonAdapter sAdap = new ButtonAdapter(getActivity().getBaseContext(),inventoryList,
                                                 R.layout.listview_inventory,new String[]{"name"},new int[]{R.id.name},R.id.optionView,"id");
        inventoryListView.setAdapter(sAdap);
/*
	public ButtonAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to, int buttonId, String tag) {
* */
       /* ButtonAdapter sAdap = new ButtonAdapter(getActivity().getBaseContext(), inventoryList,
                R.layout.listview_inventory, new String[]{"name"}, new int[] {R.id.name}, R.id.optionView, "id");
        inventoryListView.setAdapter(sAdap);*/
    }
    @Override
    public void update() {
        search();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

}
