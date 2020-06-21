package com.google.appbegin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class q extends AppCompatActivity {
    ListView lv;
    DatabaseReference mdatafirebasex,mdatafirebaseaddBus;
    ArrayList<String> ar;
    ArrayList<Bus> buslist;
    ArrayAdapter arrayAdapterz = null;
    TextView  t;
    Button b;
    static String  ea = "";

    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q);
        lv = findViewById(R.id.lv);
        t = findViewById(R.id.t);
        b = findViewById(R.id.bt);
        ar = new ArrayList<String>();
        //lv.setAdapter(arrayAdapterz);
        // getProfile();

        buslist = new ArrayList<Bus>();
        //gets();
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Random rd=new Random();
//                x=rd.nextInt((1000-1+1)+1);
//                Toast.makeText(q.this, x+"", Toast.LENGTH_SHORT).show();
//                String iduser = "1480537918795748";
//                addBus(iduser,"Long An","Xe Ngũ Long - "+x,"62C1 26484","096648181","6","Xe máy","15 Tấn",iduser+x);

                if (buslist.size() != 0) {
                    Toast.makeText(q.this, "Click", Toast.LENGTH_SHORT).show();
                    BusAdapter adapter = new BusAdapter(q.this, R.layout.custom_listview_bus, buslist);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                } else {
                    Toast.makeText(q.this, "!!!", Toast.LENGTH_SHORT).show();
                    BusAdapter adapter = new BusAdapter(q.this, R.layout.custom_listview_bus, buslist);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buslist.clear();
                //gets();
            }
        });

//    }
//    private void addBus(String keyuser,String LocationBus, String Name, String NumberBus, String PhoneBus, String Point, String Speciescar, String Tonnage, String idBus  ){
//        final Bus bus = new Bus(LocationBus,Name,NumberBus,PhoneBus, Point, Speciescar, Tonnage,idBus );
//        mdatafirebaseaddBus= FirebaseDatabase.getInstance().getReference();
//        mdatafirebaseaddBus.child("bus/"+keyuser+"/"+idBus).setValue(bus);
//    }
//    public void  getProfile( ){
//        mdatafirebasex= FirebaseDatabase.getInstance().getReference().child("bus").child("1996") ;
//        mdatafirebasex.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ea = "1";
//                Bus bus = dataSnapshot.getValue(Bus.class);
//                ar.add(bus.Name+" - "+bus.idBus);
//                arrayAdapterz.notifyDataSetChanged();
//                for(DataSnapshot  datas: dataSnapshot.getChildren()){
//                    String names=datas.child("Name").getValue().toString();
//                    t.setText(names);
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    }
}
