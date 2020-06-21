package com.google.appbegin;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerDragListener
        ,GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationClickListener , GoogleMap.OnMyLocationButtonClickListener  {
    ImageView imgSearch,imgUser,imgdialogregistration;
    DatabaseReference mdatafiresetprofile,mdatafireLoadAccuracy;
    Spinner spTinh,spQuan,spPhuong,spLoaixe, sptrongtai;
    TextView txtxemdialog,e,txtNameUserDetailTranSport ,txtSDTUserDetailTranSport ,
            txtLocationBeginDetailTranSport, txtLocationEndDetailTranSport,txtKhoangCachDetailTranSport,
            txtDetailLocationSDT, txtDetailLocationEnd, txtDetailLocationBegin,txtDetailLocationVND,txtCostDetailTranSport;
    DatabaseReference   mdatafirebasex,mdatafireCheckBus;
    EditText edtBienSoXeDialog,edtTenXeDialog,edtsetphonedialog;
    Dialog dialogDetailTransport,dialog, dialogyesno,dialogloading,dialogsetphone;
    ScrollView Scrollviewdialogquydinh;
    LinearLayout Lineardialog2,LinearTransport,LinearFragmen,Linear199,  Lineardialog1;
    CheckBox cbdialogsetBus;
    public  static Accuracy accuracy = new Accuracy();
    Button btNoDetailTranSport,btYesDetailTranSport,btfinishlTransport,btCancelTransport
            ,btHuydangkydialog,btdialogclose, btdangkydialog,btsetphonedialog,bthuysetphonedialog;
    int countime = 1 ,checkDetailBus=0;
    static float sdiem;
    public static String  sIID,  sSoXe = null ,    sName, sLinkAvatar, sPhone,sEmail,sID;
    Timer T, timeLocationBus;
    static double x=0,y=0;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    static SupportMapFragment mapTest;
///s

    public static  Bus buss;
    private GoogleApiClient clientgoogle;
    private LocationRequest locationRequest;
    private Location lastlocation , los;
    private Marker currenMarker,mMarkerBeginTransport,mMarkerEndTransport;
    static double dLatBeginMap,dLongBeginMap,dLatEndMap,dLongEndMap, dLatMy,dLongMy;
    static int idiaLogYesOrNo = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
//      lấy tọa độ từ gps
        client = LocationServices.getFusedLocationProviderClient(this);

        mapTest = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapGoogle);
        mapTest.getMapAsync(this);
        // firebase
        mdatafiresetprofile= FirebaseDatabase.getInstance().getReference();
        sIID = sID = "1480537918795748";
        inten();
//        getBusUser("sd");
        T=new Timer();
        timeLocationBus=new Timer();

        dialogyesno = new Dialog(this);
        dialog = new Dialog(this);
        dialogDetailTransport = new Dialog(this);
        dialogloading = new Dialog(this);
        dialogsetphone = new Dialog(this);
        dialogsetphone.setContentView(R.layout.dialog_custom_setsdt);
        dialogyesno.setContentView(R.layout.dialog_yesnotransport);
        dialogDetailTransport.setContentView(R.layout.dialog_custom_detailtransport);
        dialogloading.setContentView(R.layout.dialog_loading);
        btsetphonedialog = dialogsetphone.findViewById(R.id.btsetphonedialog);
        bthuysetphonedialog = dialogsetphone.findViewById(R.id.bthuysetphonedialog);
        edtsetphonedialog = dialogsetphone.findViewById(R.id.edtsetphonedialog);
        checkBus(sID);
        res();
        dialogloading.show();
         // time wait load database
        getProfileUser(sID);
        Loading();
        btsetphonedialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtsetphonedialog.getText().equals("")){
                    Toast.makeText(MainActivity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }else {
                    setPhoneUser(sID,sName,sEmail,edtsetphonedialog.getText().toString(),sLinkAvatar);
                    getProfileUser(sID);
                    dialogsetphone.dismiss();
                }
            }

        });

    }
    // auto load location bus
    private void autoLoadLocationBus(String id, final String Lat, final String Long){

                Bus bus1 = new Bus(buss.Phuong,buss.Quan,buss.Ten,buss.Tinh,buss.diem,
                        buss.iduser,buss.loaiXe,
                        buss.soxe,buss.trangthai ,buss.trongtai,Lat,Long);
                mdatafiresetprofile.child("Bus/"+id).setValue(bus1);

    }
    // dialog yes or no
    private void diaLogYesOrNo(String Tile){
        dialogyesno.show();
        TextView txtTTTDialog = dialogyesno.findViewById(R.id.txtTTTDialog);
        Button btyesdialog = dialogyesno.findViewById(R.id.btyesdialog);
        Button btNodialog = dialogyesno.findViewById(R.id.btNodialog);
        txtTTTDialog.setText(Tile);
        btNodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idiaLogYesOrNo = 0;
                dialogyesno.dismiss();
            }
        });
        btyesdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idiaLogYesOrNo = 1;
                dialogyesno.dismiss();
                setStatusBus(sID,"1");
                Toast.makeText(MainActivity.this, "Đã xác nhận", Toast.LENGTH_SHORT).show();
                LinearTransport.setVisibility(View.GONE);
            }
        });
    }
    //set trang thai
    private void setStatusBus(String idbus, final String statusbus){

        mdatafireLoadAccuracy= FirebaseDatabase.getInstance().getReference().child("Bus").child(idbus);
        mdatafireLoadAccuracy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bus bus = dataSnapshot.getValue(Bus.class);
                Bus bus1 = new Bus(bus.Phuong,bus.Quan,bus.Ten,bus.Tinh,bus.diem,bus.iduser,bus.loaiXe,
                        bus.soxe,statusbus ,bus.trongtai,bus.viTrix,bus.viTriy);
                mdatafiresetprofile.child("Bus/"+sID).setValue(bus1);
                mdatafireLoadAccuracy.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //add marker transport
    private void addMarkerTransport(){
//        markerEnd = mMapTest.addMarker(new MarkerOptions().position(e).title( location));
//        mMapTest.animateCamera(CameraUpdateFactory.newLatLngZoom(e,15));

        LatLng begin = new LatLng(dLatBeginMap,dLongBeginMap );
        LatLng end = new LatLng(dLatEndMap,dLongEndMap );
        mMarkerBeginTransport = mMap.addMarker(new MarkerOptions()
                .position(begin)
                .title("Điểm bắt đầu" )
                .snippet(""+txtLocationBeginDetailTranSport.getText()+" \n"+txtSDTUserDetailTranSport.getText())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerbegin)));
        mMarkerEndTransport = mMap.addMarker(new MarkerOptions()
                .position(end)
                .title("Điểm kết thúc")
                .snippet(""+txtLocationEndDetailTranSport.getText())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerend)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(begin,15));
    }
    // dialog thông tin transport
    private void dialogDetailTransport(String id){
        dialogDetailTransport.show();
        btNoDetailTranSport = dialogDetailTransport.findViewById(R.id.btNoDetailTranSport);
        btYesDetailTranSport = dialogDetailTransport.findViewById(R.id.btYesDetailTranSport);
        txtSDTUserDetailTranSport = dialogDetailTransport.findViewById(R.id.txtSDTUserDetailTranSport);
        txtNameUserDetailTranSport = dialogDetailTransport.findViewById(R.id.txtNameUserDetailTranSport);
        txtLocationBeginDetailTranSport = dialogDetailTransport.findViewById(R.id.txtLocationBeginDetailTranSport);
        txtKhoangCachDetailTranSport = dialogDetailTransport.findViewById(R.id.txtKhoangCachDetailTranSport);
        txtLocationEndDetailTranSport = dialogDetailTransport.findViewById(R.id.txtLocationEndDetailTranSport);
        txtCostDetailTranSport = dialogDetailTransport.findViewById(R.id.txtCostDetailTranSport);
        // lấy thông tin khách hàng
        mdatafireLoadAccuracy= FirebaseDatabase.getInstance().getReference().child("profileuser").child(id);
        mdatafireLoadAccuracy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileUser user = dataSnapshot.getValue(ProfileUser.class);
                txtNameUserDetailTranSport.setText(user.Name);
                txtSDTUserDetailTranSport.setText(user.Phonenumber);
                mdatafireLoadAccuracy.removeEventListener(this);
                txtDetailLocationSDT.setText(user.Phonenumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btYesDetailTranSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = accuracy.CostAccuracy
                        ,sidUser = accuracy.IDUserSendAccuracy
                        ,sLatBegin = accuracy.LatBeginAccuracy
                        ,sLatEnd = accuracy.LatEndAccuracy
                        ,slocationBegin = accuracy.LocationBeginAccuracy
                        ,slocationEnd = accuracy.LocationEndAccuracy
                        ,sLongbegin = accuracy.LongBeginAccuracy
                        ,sLongEnd = accuracy.LongEndAccuracy;

                final Accuracy accuracy = new Accuracy(acc,sidUser,sLatBegin
                        ,sLatEnd,slocationBegin
                        ,slocationEnd,sLongbegin
                        ,sLongEnd,"1" );
                mdatafiresetprofile.child("Accuracy/"+sID).setValue(accuracy);
                dialogDetailTransport.dismiss();
                LinearFragmen.setVisibility(View.GONE);
                LinearTransport.setVisibility(View.VISIBLE);
                addMarkerTransport();
                setStatusBus(sID,"0");
            }
        });
        btNoDetailTranSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetailTransport.dismiss();
            }
        });

    }
    // load yếu cầu
    private void LoadAccuracy(String id){
        mdatafireLoadAccuracy= FirebaseDatabase.getInstance().getReference().child("Accuracy").child(id);
        mdatafireLoadAccuracy.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 accuracy = dataSnapshot.getValue(Accuracy.class);
                if (accuracy.StatusAccuracy.equals("5")){
                    dialogDetailTransport(accuracy.IDUserSendAccuracy);
                    if (accuracy.LocationBeginAccuracy.equals("Vị trí hiện tại")){
                        txtLocationBeginDetailTranSport.setText("Vị trí của khách hàng");
                    }else {
                        txtLocationBeginDetailTranSport.setText(accuracy.LocationEndAccuracy);
                    }
                    if (accuracy.LocationEndAccuracy.equals("Vị trí hiện tại")){
                        txtLocationEndDetailTranSport.setText("Vị trí của khách hàng");
                    }else {
                        txtLocationEndDetailTranSport.setText(accuracy.LocationEndAccuracy);
                    }
                    txtCostDetailTranSport.setText(accuracy.CostAccuracy+"");
                    txtKhoangCachDetailTranSport.setText("update");
                    txtDetailLocationBegin.setText(accuracy.LocationBeginAccuracy);
                    txtDetailLocationEnd.setText(accuracy.LocationEndAccuracy);
                    txtDetailLocationVND.setText(accuracy.CostAccuracy);
                    dLatBeginMap = Double.parseDouble(accuracy.LatBeginAccuracy);
                    dLongBeginMap = Double.parseDouble(accuracy.LongBeginAccuracy);
                    dLatEndMap = Double.parseDouble(accuracy.LatEndAccuracy);
                    dLongEndMap = Double.parseDouble(accuracy.LongEndAccuracy);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void checkBus(String id){
        mdatafireCheckBus = FirebaseDatabase.getInstance().getReference().child("Bus").child(id);
        mdatafireCheckBus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 buss = dataSnapshot.getValue(Bus.class);
                 try {
                     sSoXe = buss.soxe;
                     if (sSoXe!=null){
                         Toast.makeText(MainActivity.this, buss.iduser+"", Toast.LENGTH_SHORT).show();
                         LoadAccuracy(sID);
                         mdatafireCheckBus.removeEventListener(this);
                     }
                 }catch (Exception e){


                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /// đếm time load CSDL
//    kiểm tra user có đăng ký thành viên chưa
    private void Loading() {
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        countime++;
                        if (countime== 5 ){
                            if (sSoXe==null){
                                /// điếm 6s để load data
                                dialogloading.dismiss();
                                dialog.show();
                                 dialogregistration();
                                 T.cancel();

                            }else {
                                dialogloading.dismiss();
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);

    }
    /// lấy phuong tiện theo id user
    // lấy tọa độ

    private void res() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    /// lấy thông tin của user qu a key

    public void  getProfileUser(final String s){
        mdatafirebasex= FirebaseDatabase.getInstance().getReference().child("profileuser").child(s);
        mdatafirebasex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileUser profile = dataSnapshot.getValue(ProfileUser.class);
//                 xác thực user có sdt hay chưa
                     sPhone = profile.Phonenumber;
                    if (sPhone.equals("") )
                    {
                        dialogloading.dismiss();
                        dialogsetphone.show();
                        Toast.makeText(MainActivity.this, "Chưa có sdt", Toast.LENGTH_SHORT).show();
                    }
                    if (profile.Email.equals("")){

                    }else {
                        // set dữ liệu lên biến
                        sName = profile.Name;
                        sEmail = profile.Email;
                        sPhone = profile.Phonenumber;
                        sLinkAvatar= profile.AvatarLink;
                    }
               }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    // dialog set phone to ProfileUser
    private void setPhoneUser(String id,String name,String email,String phone,String avatar)
    {
        final ProfileUser profileUser = new ProfileUser(avatar,email,name,phone);
        mdatafiresetprofile.child("profileuser/"+id).setValue(profileUser);
        getProfileUser(sID);
    }

    // ánh xạ
    private void inten() {

        txtDetailLocationVND = findViewById(R.id.txtDetailLocationVND);
        txtDetailLocationBegin = findViewById(R.id.txtDetailLocationBegin);
        txtDetailLocationEnd = findViewById(R.id.txtDetailLocationEnd);
        txtDetailLocationSDT = findViewById(R.id.txtDetailLocationSDT);
        btfinishlTransport = findViewById(R.id.btfinishlTransport);
        btCancelTransport = findViewById(R.id.btCancelTransport);
        LinearFragmen = findViewById(R.id.LinearFragmen);
        LinearTransport = findViewById(R.id.LinearTransport);
        imgSearch = findViewById(R.id.imgS);
        imgUser = findViewById(R.id.imgU);
    }
    private void checkphoneuser(){
        if (sPhone.equals(""))
            dialogsetphone.show();
    }
    public void onclick(View view){
        switch (view.getId()){
            case R.id.imgS:
                LinearFragmen.setVisibility(View.GONE);
                LinearTransport.setVisibility(View.VISIBLE);
                checkphoneuser();
                break;
            case R.id.btEditDatailBus:
                imgSearch.setBackgroundColor(Color.parseColor("#8AA0EC47"));
                imgUser.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                checkphoneuser();
                break;
                // hoàn thành
            case R.id.btfinishlTransport:
                 diaLogYesOrNo("Xác nhận hoàn thành");
                break;
                // huy van chuyển
            case R.id.btCancelTransport:
                diaLogYesOrNo("Xác nhận hủy vận chuyển");
                break;
        }
    }


    public void toollickaddfragment (View view){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (view.getId()){
            case R.id.imgU:
                LinearTransport.setVisibility(View.GONE);
                LinearFragmen.setVisibility(View.VISIBLE);
                fragment = new FragmentUser();
                imgUser.setBackgroundColor(Color.parseColor("#8AA0EC47"));
                imgSearch.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                checkphoneuser();
                break;
        }
        fragmentTransaction.replace(R.id.FramContent,fragment);
        //add Fragment
        fragmentTransaction.commit();

    }
    // add suer
    private void addBusUser( String xphuong,String xquan,String xten,String xtinh,
                             String xdiem,String xiduser,String xloaixe,
                             String xsoxe,String xtrangthai,String xtrongtai,String x,String y)
    {
        final Bus bus = new Bus(xphuong,xquan,xten,xtinh,xdiem,xiduser,xloaixe,xsoxe,xtrangthai,xtrongtai,x,y);
        mdatafiresetprofile.child("Bus/"+xiduser).setValue(bus);
    }
    public void dialogregistration (){

        dialog.setContentView(R.layout.dialog_custom_addbus);
        imgdialogregistration = dialog.findViewById(R.id.imgdialogregistration);
        edtTenXeDialog = dialog.findViewById(R.id.edtTenXeDialog);
        btdangkydialog = dialog.findViewById(R.id.btdangkydialog);
        edtBienSoXeDialog = dialog.findViewById(R.id.edtBienSoXeDialog);
        btHuydangkydialog = dialog.findViewById(R.id.btHuydangkydialog);
        btdialogclose = dialog.findViewById(R.id.btdialogclose);
        txtxemdialog = dialog.findViewById(R.id.txtxemdialog);
        sptrongtai = dialog.findViewById(R.id.sptrongtai);
        spLoaixe = dialog.findViewById(R.id.spLoaixe);
        spPhuong = dialog.findViewById(R.id.spPhuong);
        spQuan = dialog.findViewById(R.id.spquan);
        spTinh = dialog.findViewById(R.id.spTinh);
        Lineardialog1 = dialog.findViewById(R.id.Lineardialog1);
        Lineardialog2 = dialog.findViewById(R.id.Lineardialog2);
        Scrollviewdialogquydinh = dialog.findViewById(R.id.Scrollviewdialogquydinh);
        cbdialogsetBus = dialog.findViewById(R.id.cbdialogsetBus);
        ///
        imgdialogregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lineardialog1.setVisibility(View.GONE);
                Lineardialog2.setVisibility(View.VISIBLE);
            }
        });
        btHuydangkydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lineardialog1.setVisibility(View.VISIBLE);
                Lineardialog2.setVisibility(View.GONE);
            }
        });
        btdialogclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtxemdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scrollviewdialogquydinh.setVisibility(View.VISIBLE);
            }
        });
        btdangkydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTenXeDialog.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Chưa nhập tên phương tiện", Toast.LENGTH_SHORT).show();
                }else  if (edtBienSoXeDialog.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Chưa nhập biển số xe", Toast.LENGTH_SHORT).show();
                }else  if (cbdialogsetBus.isChecked()){
                    addBusUser( spPhuong.getSelectedItem().toString(),spQuan.getSelectedItem().toString(),
                            edtTenXeDialog.getText().toString(),spTinh.getSelectedItem().toString(),
                            "5.0", sID,spLoaixe.getSelectedItem().toString(),
                            edtBienSoXeDialog.getText().toString(), "0",sptrongtai.getSelectedItem().toString(),"","" );

                    dialog.dismiss();
                }else {
                    Toast.makeText(MainActivity.this, "Chưa đồng ý các quy định", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ////
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this,R.array.ListThanhPho,android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTinh.setAdapter(spAdapter);
        ArrayAdapter<CharSequence> spLoaixeAdapter = ArrayAdapter.createFromResource(this,R.array.ListLoaixe,android.R.layout.simple_spinner_item);
        spLoaixeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoaixe.setAdapter(spLoaixeAdapter);
        spLoaixe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    ArrayAdapter<CharSequence> spLoaixeAdapter = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListTrongTaiXeTai,android.R.layout.simple_spinner_item);
                    spLoaixeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sptrongtai.setAdapter(spLoaixeAdapter);
                }else if (position == 1){
                    ArrayAdapter<CharSequence> spLoaixeAdapter = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListTrongTaiXeBanTai,android.R.layout.simple_spinner_item);
                    spLoaixeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sptrongtai.setAdapter(spLoaixeAdapter);
                }else if (position == 2){
                    ArrayAdapter<CharSequence> spLoaixeAdapter = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListTrongTaiXe3Gac,android.R.layout.simple_spinner_item);
                    spLoaixeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sptrongtai.setAdapter(spLoaixeAdapter);
                }else if (position == 3){
                    ArrayAdapter<CharSequence> spLoaixeAdapter = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListTrongTaiXeMay,android.R.layout.simple_spinner_item);
                    spLoaixeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sptrongtai.setAdapter(spLoaixeAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //// load dia chỉ
        spTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    ArrayAdapter<CharSequence> spAdapterQuan = ArrayAdapter.createFromResource(MainActivity.this,R.array.ListQuanHCM,android.R.layout.simple_spinner_item);
                    spAdapterQuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spQuan.setAdapter(spAdapterQuan);
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spQuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position== 0){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ListPhuongQuan1HCM,android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 1 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this,R.array.ListPhuongQuan2HCM,android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 2 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListPhuongQuan3HCM,
                            android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 3 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListPhuongQuan4HCM,
                            android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 4 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListPhuongQuan5HCM,
                            android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 5 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListPhuongQuan6HCM,
                            android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }else if (position == 6 ){
                    ArrayAdapter<CharSequence> spAdapterQuan1 = ArrayAdapter.createFromResource(MainActivity.this
                            ,R.array.ListPhuongQuan7HCM ,
                            android.R.layout.simple_spinner_item);
                    spAdapterQuan1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spPhuong.setAdapter(spAdapterQuan1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;
        if (lastlocation != null)
            currenMarker.remove();
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Here !!");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currenMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(100));

        if (clientgoogle != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(clientgoogle, (com.google.android.gms.location.LocationListener) MainActivity.this);

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        autoLoadLocationBus(sID,location.getLatitude()+"",location.getLongitude()+"");
    }

    protected synchronized void buildGoogle(){
        clientgoogle= new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API)
                .build();

        clientgoogle.connect();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            buildGoogle();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

        }
        timeLocationBus.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {countime++;
                        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(sSoXe != null){
                                    onMyLocationClick(location);
                                }
                            }
                        });
                    }
                });
            }
        }, 1000, 5000);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

//        T.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {countime++;
//                        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                onMyLocationClick(location);
//                            }
//                        });
//                    }
//                });
//            }
//        }, 1000, 4000);
    }
}
