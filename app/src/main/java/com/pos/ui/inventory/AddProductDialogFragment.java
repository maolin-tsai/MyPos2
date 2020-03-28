package com.pos.ui.inventory;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import com.pos.R;
import com.pos.domain.inventory.Inventory;
import com.pos.domain.inventory.ProductCatalog;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.component.UpdatableFragment;

public class AddProductDialogFragment  extends DialogFragment {

    private EditText barcodeBox;
    private ProductCatalog productCatalog;
    private Button scanButton;
    private EditText priceBox;
    private EditText nameBox;
    private Button confirmButton;
    private Button clearButton;
    private UpdatableFragment fragment;
    private Resources res;
    private static AddProductDialogFragment  AddProductDialogFrag = null;

    public AddProductDialogFragment(UpdatableFragment fragment) {
        super();
        this.fragment = fragment;
    }
    public AddProductDialogFragment() {
        super();
    }

    public void setUpdatableFragment(UpdatableFragment fragment)
    {
        this.fragment = fragment;
    }
    public static AddProductDialogFragment getInstance()
    {
        if(AddProductDialogFrag == null)
           AddProductDialogFrag= new AddProductDialogFragment();

        return AddProductDialogFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            productCatalog = Inventory.getInstance().getProductCatalog();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.layout_addproduct, container,
                false);

        res = getResources();

        barcodeBox = (EditText) v.findViewById(R.id.barcodeBox);
        scanButton = (Button) v.findViewById(R.id.scanButton);
        priceBox = (EditText) v.findViewById(R.id.priceBox);
        nameBox = (EditText) v.findViewById(R.id.nameBox);
        confirmButton = (Button) v.findViewById(R.id.confirmButton);
        clearButton = (Button) v.findViewById(R.id.clearButton);

        initUI();
        return v;
    }

    private void initUI() {

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Toast toast;
                String Msg;
               if(barcodeBox.getText().toString().equals("") ||
                  nameBox.getText().toString().equals("") ||
                  priceBox.getText().toString().equals("")  )
               {
                   Msg = res.getString(R.string.please_input_all);
                   SpannableStringBuilder biggerText = new SpannableStringBuilder(Msg);
                   biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, Msg.length(), 0);
                   Toast.makeText(getActivity().getBaseContext(),
                           biggerText,Toast.LENGTH_SHORT).show();

               }
               else
               {
                   //     addProduct(String name, String barcode, double salePrice)
                   //add production
                   boolean  success = productCatalog.addProduct(nameBox.getText().toString(),barcodeBox.getText().toString(),
                                                                Double.parseDouble(priceBox.getText().toString()));

                   if(success)
                   {
                       Msg = res.getString(R.string.success);
                       SpannableStringBuilder biggerText = new SpannableStringBuilder(Msg);
                       biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, Msg.length(), 0);
                       Toast.makeText(getActivity().getBaseContext(),
                               biggerText,Toast.LENGTH_SHORT).show();
                       fragment.update();
                       clearAllBox();
                       AddProductDialogFragment.this.dismiss();

                   }
                   else
                   {
                       Msg = res.getString(R.string.fail);
                       SpannableStringBuilder biggerText = new SpannableStringBuilder(Msg);
                       biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, Msg.length(), 0);
                       Toast.makeText(getActivity().getBaseContext(),
                               biggerText,Toast.LENGTH_SHORT).show();

                   }

               }


            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v)
            {
              if(barcodeBox.getText().toString().equals("") && nameBox.getText().toString().equals("")&&
                 priceBox.getText().toString().equals(""))
                   AddProductDialogFragment.this.dismiss();
              else
                  clearAllBox();
            }
        });
    }

    /**
     * Clear all box
     */
    private void clearAllBox() {
        barcodeBox.setText("");
        nameBox.setText("");
        priceBox.setText("");
    }
}
