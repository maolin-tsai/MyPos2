package com.pos.ui.sale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.pos.domain.DateTimeStrategy;
import com.pos.domain.sale.Register;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.component.UpdatableFragment;

import com.pos.R;

public class EndPaymentFragmentDialog extends DialogFragment {
    private Button doneButton;
    private TextView chg;
    private Register regis;
    private UpdatableFragment saleFragment;
    private UpdatableFragment reportFragment;

    public EndPaymentFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
        super();
        this.saleFragment = saleFragment;
        this.reportFragment = reportFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            regis = Register.getInstance();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }

        View v = inflater.inflate(R.layout.dialog_paymentsuccession, container, false);
        String strtext=getArguments().getString("edttext");
        chg = (TextView) v.findViewById(R.id.changeTxt);
        chg.setText(strtext);
        doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                end();
            }
        });

        return v ;
    }

    private void end(){
        regis.endSale(DateTimeStrategy.getCurrentTime());
        saleFragment.update();
        reportFragment.update();
        this.dismiss();
    }
}
