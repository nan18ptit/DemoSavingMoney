package com.example.savingmoney.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingmoney.R;
import com.example.savingmoney.UpdateDeleteActivity;
import com.example.savingmoney.adapter.RecycleViewAdapter;
import com.example.savingmoney.dal.SQLiteHelper;
import com.example.savingmoney.model.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentToday extends Fragment  {
    private RecyclerView recyclerView;
    private RecycleViewAdapter adapter;
    private SQLiteHelper db;
    private TextView tvTong;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

       @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTong = view.findViewById(R.id.tvTong);
        adapter = new RecycleViewAdapter();
        db = new SQLiteHelper(getContext());
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        List<item> list = db.getByDate(f.format(d));
        adapter.setList(list);
        tvTong.setText("Tong tien: " + sum(list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    // Tinh Tong
    private int sum(List<item> list){
        int t = 0;
        for (item i:list){
            t += Integer.parseInt(i.getPrice());
        }
        return t;
    }
    @Override
    public void onResume() {
        super.onResume();
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        List<item> list = db.getByDate(f.format(d));
        adapter.setList(list);
        tvTong.setText("Tong tien: " + sum(list));
    }

}
