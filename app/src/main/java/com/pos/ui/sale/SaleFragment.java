package com.pos.ui.sale;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.R;
import com.pos.domain.inventory.LineItem;
import com.pos.domain.sale.Register;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.component.UpdatableFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class SaleFragment extends UpdatableFragment {
    private Register register;
    private ArrayList<Map<String, String>> saleList;
    private ListView saleListView;
    private Button clearButton;
    private TextView totalPrice;
    private Button endButton;
    private UpdatableFragment reportFragment;
    private Resources res;


    public SaleFragment(UpdatableFragment reportFragment) {
        super();
        this.reportFragment = reportFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            register = Register.getInstance();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.layout_sale,container,false);
        res = getResources();
        saleListView = (ListView) view.findViewById(R.id.sale_List);
        totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        clearButton = (Button) view.findViewById(R.id.clearButton);
        endButton = (Button) view.findViewById(R.id.endButton);

        initUI();
        return view;
    }
    private void initUI() {
        endButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               if(register.hasSale()){
                   showPopup(v);
               } else {
                   Toast.makeText(getActivity().getBaseContext() , res.getString(R.string.hint_empty_sale), Toast.LENGTH_SHORT).show();
               }
           }

        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (!register.hasSale() || register.getCurrentSale().getAllLineItem().isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext() , res.getString(R.string.hint_empty_sale), Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmClearDialog();
                }


            }
        });
    }

    public void showPopup(View anchorView) {

        Bundle bundle = new Bundle();
        bundle.putString("edttext", totalPrice.getText().toString());
        PaymentFragmentDialog newFragment = new PaymentFragmentDialog(SaleFragment.this, reportFragment);
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "");
    }
    private void showList(List<LineItem> list) {

        saleList = new ArrayList<Map<String, String>>();
        for(LineItem line : list) {
            saleList.add(line.toMap());
        }
        //System.out.println("showlist count="+saleList.size());
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(getActivity().getBaseContext(), saleList,
                R.layout.listview_lineitem, new String[]{"name","quantity","price"}, new int[] {R.id.name,R.id.quantity,R.id.price});
        saleListView.setAdapter(sAdap);
    }
    @Override
    public void update() {



        if(register.hasSale()){
            showList(register.getCurrentSale().getAllLineItem());
            totalPrice.setText(register.getTotal() + "");
        }
        else{
            showList(new ArrayList<LineItem>());
            totalPrice.setText("0.00");
        }

    }
    /**
     * Show confirm or clear dialog.
     */
    private void showConfirmClearDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(res.getString(R.string.dialog_clear_sale));
        dialog.setPositiveButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setNegativeButton(res.getString(R.string.clear), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        register.cancleSale();
                        update();
                    }
                });

                dialog.show();
    }
}
