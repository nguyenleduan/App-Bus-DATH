package com.google.appbegin;

import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class FragmentUser extends Fragment  {
    ImageButton imgbtlogout;
    TextView txtquanlyxe, txtthongtincanhan , txtName,txtNumberVanChuyen,
            txtEmailDetailedProfile, txtNameDetailedProfile,txtPhoneDetailedProfile ,
            txtNumberDetailedProfile,txtDatailNameBus,txtDatailLocationBus,txtDatailNumberBus,
            txtDatailLoaiXeBus,txtDatailTrongTaiBus            ;
    LinearLayout LinearDetailBus, LinearDetailedProfile,LinearEditProfile,LinearHistoryProfile,
            Linearprofile1 ,Linearprofile2,Linearprofile3;
    ScrollView ScrollViewProfiles;
    ListView lvManageBus;
    ArrayList<String> aBus;
    ArrayAdapter adapter = null;
    EditText edtsetphonedialog ;
    Button bthuysetphonedialog, btsetphonedialog,btEditDatailBus;
    DatabaseReference mdatafiregetbus,mdatafirebaseSetProfile;
    ConstraintLayout ConstraintLayoutdangkybus;
    ArrayList<Bus> buslist;
    Dialog dialogsetphoneuser;
    MainActivity main = new MainActivity();
    public int m1 = 0,m2 = 0,m3 = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View viewFragmentUser = inflater.inflate(R.layout.fragment_layout_user,container,false);
        buslist = new ArrayList<Bus>();
        //intent
        dialog = new Dialog(getActivity());
        LinearDetailBus= viewFragmentUser.findViewById(R.id.LinearDetailBus);
        txtDatailTrongTaiBus= viewFragmentUser.findViewById(R.id.txtDatailTrongTaiBus);
        txtDatailLoaiXeBus= viewFragmentUser.findViewById(R.id.txtDatailLoaiXeBus);
        txtDatailNumberBus= viewFragmentUser.findViewById(R.id.txtDatailNumberBus);
        txtDatailLocationBus= viewFragmentUser.findViewById(R.id.txtDatailLocationBus);
        txtDatailNameBus= viewFragmentUser.findViewById(R.id.txtDatailNameBus);
        btEditDatailBus= viewFragmentUser.findViewById(R.id.btEditDatailBus);
        txtName= viewFragmentUser.findViewById(R.id.txtName);
        Linearprofile1 = viewFragmentUser.findViewById(R.id.Linearprofile1);
        Linearprofile2= viewFragmentUser.findViewById(R.id.Linearprofile2);
        Linearprofile3= viewFragmentUser.findViewById(R.id.Linearprofile3);
        txtNumberVanChuyen= viewFragmentUser.findViewById(R.id.txtNumberVanChuyen);
        txtEmailDetailedProfile= viewFragmentUser.findViewById(R.id.txtEmailDetailedProfile);
        txtNameDetailedProfile= viewFragmentUser.findViewById(R.id.txtNameDetailedProfile);
        txtPhoneDetailedProfile = viewFragmentUser.findViewById(R.id.txtPhoneDetailedProfile);
        txtNumberDetailedProfile= viewFragmentUser.findViewById(R.id.txtNumberDetailedProfile);
        imgbtlogout = viewFragmentUser.findViewById(R.id.imgbtlogout);
        LinearHistoryProfile= viewFragmentUser.findViewById(R.id.LinearHistoryProfile);
        txtquanlyxe = viewFragmentUser.findViewById(R.id.txtquanlyxe);
        ScrollViewProfiles= viewFragmentUser.findViewById(R.id.ScrollViewProfiles);
        txtthongtincanhan = viewFragmentUser.findViewById(R.id.txtthongtincanhan);
        LinearDetailedProfile= viewFragmentUser.findViewById(R.id.LinearDetailedProfile);
        LinearEditProfile= viewFragmentUser.findViewById(R.id.LinearEditProfile);
        // dialog
        dialogsetphoneuser =  new Dialog(getActivity());
        //clickeven
        onlick();
        setProfile();
        return viewFragmentUser;
    }
    private void getDetailBus(String id){
        mdatafiregetbus = FirebaseDatabase.getInstance().getReference().child("Bus").child(id);
        mdatafiregetbus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bus bus = dataSnapshot.getValue(Bus.class);
                txtDatailNameBus.setText(bus.Ten);
                txtDatailLoaiXeBus.setText(bus.loaiXe);
                txtDatailTrongTaiBus.setText(bus.trongtai);
                txtDatailLocationBus.setText(bus.Tinh+", "+bus.Quan+", "+bus.Phuong);
                txtDatailNumberBus.setText(bus.soxe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setProfile() {
        txtName.setText(main.sName);
        txtEmailDetailedProfile.setText(main.sEmail);
        txtNameDetailedProfile.setText(main.sName);
        txtPhoneDetailedProfile.setText(main.sPhone);
    }
    Dialog dialog;
    ImageView imgdialogregistration;


    /// onlick
    public void onlick() {


        txtquanlyxe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetailBus(main.sIID);
                ScrollViewProfiles.setVisibility(View.GONE);
                LinearDetailBus.setVisibility(View.VISIBLE);
                txtquanlyxe.setBackgroundColor(Color.parseColor("#8AA0EC47"));
                txtthongtincanhan.setBackgroundColor(Color.parseColor("#FFFDFD"));
               // ẩn linear profile
                buslist.clear();

                m3 = m1 = m2 = 0;
                LinearDetailedProfile.setVisibility(View.GONE);
                LinearHistoryProfile.setVisibility(View.GONE);
                LinearEditProfile.setVisibility(View.GONE);

            }
        });
        txtthongtincanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearDetailBus.setVisibility(View.GONE);
                ScrollViewProfiles.setVisibility(View.VISIBLE);
                txtthongtincanhan.setBackgroundColor(Color.parseColor("#8AA0EC47"));
                txtquanlyxe.setBackgroundColor(Color.parseColor("#FFFDFD"));
            }
        });
        /// ẩn hien liner profile
        Linearprofile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m1 == 0){
                    LinearDetailedProfile.setVisibility(View.VISIBLE);
                    m1 = 1;
                }else{
                    LinearDetailedProfile.setVisibility(View.GONE);
                    m1 = 0;
                }

            }
        });
        Linearprofile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m2 == 0){
                    LinearEditProfile.setVisibility(View.VISIBLE);
                    m2 = 1;
                }else{
                    LinearEditProfile.setVisibility(View.GONE);
                    m2 = 0;
                }
            }
        });
        Linearprofile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m3 == 0){
                    LinearHistoryProfile.setVisibility(View.VISIBLE);
                    m3 = 1;
                }else{
                    LinearHistoryProfile.setVisibility(View.GONE);
                    m3 = 0;
                }
            }
        });

    }


}
