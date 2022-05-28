package com.example.savingmoney.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingmoney.AddActivity;
import com.example.savingmoney.R;
import com.example.savingmoney.adapter.RecycleViewAdapter;
import com.example.savingmoney.dal.SQLiteHelper;
import com.example.savingmoney.model.item;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Calendar;
import java.util.List;

public class FragmentSearch extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    private TextView tvTong;
    private Button btSearch;
    private SearchView searchView;
    private EditText eFrom, eTo;
    private Spinner spCategory;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;
    // // Create the object PieChart class
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        // recycleView đưa dũ liệu ra
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        List<item> list = db.getAll();
        adapter.setList(list);
        tvTong.setText("Tong tien: "+ sum(list)+ "vnd");
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        // search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<item>list = db.searchByTitle(s);
                tvTong.setText("Tong tien: "+ sum(list)+ " vnd");
                adapter.setList(list);
                return true;
            }

        });
        // Lắng nghe các sự kiện
        eFrom.setOnClickListener(this);
        eTo.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String cate = spCategory.getItemAtPosition(position).toString();
                List<item>list;
                // Xét case: all/ 1 category
                if (!cate.equalsIgnoreCase("all")){
                    list = db.searchByCategory(cate);
                } else {
                    list = db.getAll();
                }
                adapter.setList(list);
                tvTong.setText("Tong tien: "+ sum(list)+ " vnd");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Tinh Tong
    private int sum(List<item> list){
        int t = 0;
        for (item i:list){
            t += Integer.parseInt(i.getPrice());
        }
        return t;
    }
    // Ánh xạ tất cả vào
    private void initView(View view) {
        // Link those objects with their respective
        // id's that we have given in .XML file
        pieChart = view.findViewById(R.id.piechart);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvTong = view.findViewById(R.id.tvTong);
        btSearch = view.findViewById(R.id.btSearch);
        searchView = view.findViewById(R.id.search);
        eFrom = view.findViewById(R.id.eFrom);
        eTo = view.findViewById(R.id.eTo);
        spCategory = view.findViewById(R.id.spCatagory);
        String[] arr = getResources().getStringArray(R.array.category);
        String[] arrAll = new String[arr.length + 1];
        arrAll[0] = "All";
        for (int i = 0; i < arr.length; i++){
            arrAll[i+1] = arr[i];
        }
        spCategory.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.item_spinner, arrAll));
    }
    @Override
    public void onClick(View view) {
        // Bắt sự kiện: eFrom
        if (view == eFrom){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (m > 8){
                        date = d+ "/"+ (m+1)+ "/"+ y;
                    } else {
                        date = d+ "/0"+ (m+1)+ "/"+ y;
                    }
                    eFrom.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        // eTo
        if (view == eTo){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (m > 8){
                        date = d+ "/"+ (m+1)+ "/"+ y;
                    } else {
                        date = d+ "/0"+ (m+1)+ "/"+ y;
                    }
                    eTo.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        // btSearch
        if (view == btSearch){
            String dayFrom = eFrom.getText().toString();
            String dayTo = eTo.getText().toString();
            if (!dayFrom.isEmpty() && !dayTo.isEmpty()){
                List<item>list = db.searchByDateFromTo(dayFrom, dayTo);
                adapter.setList(list);
                tvTong.setText("Tong tien: "+ sum(list)+ " vnd");
                // ve bieu do
                setData(list);
            }
        }
    }
    // Tính %
    public int percent(List<item> list, String category){
        int p = 0;
        int t = sum(list);
        int sumCate = 0;
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).getCategory().equalsIgnoreCase(category)){
                sumCate += Integer.parseInt(list.get(i).getPrice().toString().trim());
            }
        }
        p = (sumCate*100)/t;
        return p;
    }
    // set Data char
    private void setData(List<item> list) {
        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Mua sam",
                        percent(list, "Mua sam"),
                        Color.parseColor("#F44336")));
        pieChart.addPieSlice(
                new PieModel(
                        "Di chuyen",
                        percent(list, "Di chuyen"),
                        Color.parseColor("#2196F3")));
        pieChart.addPieSlice(
                new PieModel(
                        "Tien an",
                        percent(list, "Tien an"),
                        Color.parseColor("#E91E63")));

        pieChart.addPieSlice(
                new PieModel(
                        "Tien dien",
                        percent(list, "Tien dien"),
                        Color.parseColor("#4CAF50")));
        pieChart.addPieSlice(
                new PieModel(
                        "Tien nuoc",
                        percent(list, "Tien nuoc"),
                        Color.parseColor("#9C27B0")));
        pieChart.addPieSlice(
                new PieModel(
                        "Du lich",
                        percent(list, "Du lich"),
                        Color.parseColor("#FF9800")));
        // To animate the pie chart
        pieChart.startAnimation();
    }
    private void clearData(List<item> list){
        pieChart.addPieSlice(
                new PieModel(
                        "Mua sam",
                        0,
                        Color.parseColor("#F44336")));
    }

}
