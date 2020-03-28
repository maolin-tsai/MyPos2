package com.pos.ui.sale;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.domain.DateTimeStrategy;
import com.pos.domain.sale.Sale;
import com.pos.domain.sale.SaleLedger;
import com.pos.techicalservices.NoDaoSetException;
import com.pos.ui.component.UpdatableFragment;
import com.pos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ReportFragment extends UpdatableFragment {
    private SaleLedger saleLedger;
    List<Map<String, String>> saleList;
    private ListView saleLedgerListView;
    private TextView totalBox;
    private Spinner spinner;
    private Button previousButton;
    private Button nextButton;
    private TextView currentBox;
    private Calendar currentTime;
    private DatePickerDialog datePicker;

    public static final int DAILY = 0;
    public static final int WEEKLY = 1;
    public static final int MONTHLY = 2;
    public static final int YEARLY = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            saleLedger = SaleLedger.getInstance();
        } catch (NoDaoSetException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.layout_report, container, false);

        previousButton = (Button) view.findViewById(R.id.previousButton);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        currentBox = (TextView) view.findViewById(R.id.currentBox);
        saleLedgerListView = (ListView) view.findViewById(R.id.saleListView);
        totalBox = (TextView) view.findViewById(R.id.totalBox);
        spinner = (Spinner) view.findViewById(R.id.spinner_report);

        initUI();
        return view;
    }
    private void initUI() {

        currentTime = Calendar.getInstance();
        datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                currentTime.set(Calendar.YEAR, y);
                currentTime.set(Calendar.MONTH, m);
                currentTime.set(Calendar.DAY_OF_MONTH, d);
                update();
            }
        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(Calendar.DAY_OF_MONTH));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(-1);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDate(1);
            }
        });

    }
    private void addDate(int increment) {
        int period = spinner.getSelectedItemPosition();
        if (period == DAILY){
            currentTime.add(Calendar.DATE, 1 * increment);
        } else if (period == WEEKLY){
            currentTime.add(Calendar.DATE, 7 * increment);
        } else if (period == MONTHLY){
            currentTime.add(Calendar.MONTH, 1 * increment);
        } else if (period == YEARLY){
            currentTime.add(Calendar.YEAR, 1 * increment);
        }
        update();
    }
    @Override
    public void update() {

        int period = spinner.getSelectedItemPosition();
        List<Sale> list = null;
        Calendar cTime = (Calendar) currentTime.clone();
        Calendar eTime = (Calendar) currentTime.clone();

       if(period == DAILY){
           currentBox.setText(" [" + DateTimeStrategy.getSQLDateFormat(currentTime) +  "] ");
           currentBox.setTextSize(16);
       }
       else if(period == WEEKLY){

           while(cTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
               cTime.add(Calendar.DATE, -1);
           }
           String toShow = "[" + DateTimeStrategy.getSQLDateFormat(cTime) + "][";
           eTime = (Calendar) cTime.clone();
           eTime.add(Calendar.DATE, 7);
           toShow += DateTimeStrategy.getSQLDateFormat(eTime) +  "] ";
           currentBox.setTextSize(16);
           currentBox.setText(toShow);

       }
       else if(period == MONTHLY){
           cTime.set(Calendar.DATE, 1);
           eTime = (Calendar) cTime.clone();
           eTime.add(Calendar.MONTH, 1);
           eTime.add(Calendar.DATE, -1);
           currentBox.setTextSize(18);
           currentBox.setText(" [" + currentTime.get(Calendar.YEAR) + "-" + (currentTime.get(Calendar.MONTH)+1) + "] ");

       }
       else if(period == YEARLY){
           cTime.set(Calendar.DATE, 1);
           cTime.set(Calendar.MONTH, 0);
           eTime = (Calendar) cTime.clone();
           eTime.add(Calendar.YEAR, 1);
           eTime.add(Calendar.DATE, -1);
           currentBox.setTextSize(20);
           currentBox.setText(" [" + currentTime.get(Calendar.YEAR) +  "] ");
       }
       currentTime = cTime;
       list = saleLedger.getAllSaleDuring(cTime, eTime);
        System.out.println("saleLedger="+saleLedger);
       double total = 0;
        for (Sale sale : list)
            total += sale.getTotal();
        totalBox.setText(total + "");
        showList(list);
    }
    private void showList(List<Sale> list) {

        saleList = new ArrayList<Map<String, String>>();
        for (Sale sale : list) {
            saleList.add(sale.toMap());
        }
/*
    public SimpleAdapter(Context context, List<? extends Map<String, ?>> data,
            @LayoutRes int resource, String[] from, @IdRes int[] to) {
* */
        SimpleAdapter sAdap = new SimpleAdapter(getActivity().getBaseContext() , saleList,
                R.layout.listview_report, new String[] { "id", "startTime", "total"},
                new int[] { R.id.report_id, R.id.report_starttime , R.id.report_total});
        saleLedgerListView.setAdapter(sAdap);
    }
}
