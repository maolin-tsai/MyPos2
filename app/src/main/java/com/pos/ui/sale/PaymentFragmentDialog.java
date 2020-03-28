package com.pos.ui.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.pos.R;
import com.pos.ui.component.UpdatableFragment;

public class PaymentFragmentDialog extends DialogFragment {


    private TextView totalPrice;
    private EditText input;
    private Button clearButton;
    private Button confirmButton;
    private UpdatableFragment saleFragment;
    private UpdatableFragment reportFragment;
    private String strtext;

    public PaymentFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
        super();
        this.saleFragment = saleFragment;
        this.reportFragment = reportFragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_payment, container, false);
        strtext = getArguments().getString("edttext");

        input = (EditText)v.findViewById(R.id.dialog_inputcash);
        clearButton = (Button)v.findViewById(R.id.sale_clearbutton);
        confirmButton = (Button)v.findViewById(R.id.sale_confirmButton);
         clearButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v)
           {
               end();
           }
        });
         confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String inputString = input.getText().toString();

                // int
                if(inputString.equals("")) {
                  Toast.makeText(getActivity().getBaseContext(),getResources().getString(R.string.please_input_all),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double SalePrice  = Double.parseDouble(strtext);
                    double InputPrice = Double.parseDouble(inputString);
                    if(InputPrice < SalePrice ){
                       Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.need_money) + " " + (InputPrice - SalePrice), Toast.LENGTH_SHORT).show();
                    }
                    else{
                       // process sale log  and stock data
                        Bundle bundle = new Bundle();
                        bundle.putString("edttext", InputPrice - SalePrice + "");
                        EndPaymentFragmentDialog newFragment = new EndPaymentFragmentDialog(
                                saleFragment, reportFragment);
                        newFragment.setArguments(bundle);
                        newFragment.show(getFragmentManager(), "");
                        end();

                    }

                }
            }

        });
        return v;
    }

    private void end() {
        this.dismiss();

    }
}
