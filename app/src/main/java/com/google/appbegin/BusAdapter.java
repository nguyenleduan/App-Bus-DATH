package com.google.appbegin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BusAdapter extends BaseAdapter {
    Context mycontext;
    int mylayout;
    List<Bus> arrayBus;
    public BusAdapter(Context context, int layout, List<Bus> listbuss){
        mycontext = context;
        mylayout = layout;
        arrayBus = listbuss;
    }
    @Override
    public int getCount() {
        return arrayBus.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView = inflater.inflate(mylayout,null);
//        TextView txtNameBus= convertView.findViewById(R.id.txtNameBus);
//        TextView txtSpeciescar = convertView.findViewById(R.id.txtSpeciescar);
//        TextView txtLocationBus= convertView.findViewById(R.id.txtLocationBus);
//        TextView txtPointBus= convertView.findViewById(R.id.txtPointBus);
//        String a = arrayBus.get(position).Speciescar;
//
//        TextView txtTonnage= convertView.findViewById(R.id.txtTonnage);
//        ImageView imgbuslisetview= convertView.findViewById(R.id.imgbuslisetview);
//        txtNameBus.setText(arrayBus.get(position).Name);
//        txtSpeciescar.setText(a);
//        txtLocationBus.setText(arrayBus.get(position).LocationBus);
//        txtPointBus.setText(arrayBus.get(position).Point);
//        txtTonnage.setText(arrayBus.get(position).Tonnage);
//        if (arrayBus.get(position).Speciescar.equals("Xe tải")){
//            imgbuslisetview.setImageResource(R.drawable.ic_bus_transport);
//        }if (arrayBus.get(position).Speciescar.equals("Xe bán tải") ){
//            imgbuslisetview.setImageResource(R.drawable.ic_bus_pickuptruck);
//        }if (arrayBus.get(position).Speciescar.equals("Xe 3 gác")) {
//            imgbuslisetview.setImageResource(R.drawable.ic_bus_3wheel);
//        }if (arrayBus.get(position).Speciescar.equals("Xe máy")) {
//            imgbuslisetview.setImageResource(R.drawable.ic_bus_motorycle);
//        }


        return convertView;
    }
}
