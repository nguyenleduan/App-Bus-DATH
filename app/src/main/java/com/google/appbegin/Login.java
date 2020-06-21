package com.google.appbegin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{


    EditText edtGmail, edtMatKhau, edtGmailDangky, edtNameDK ,edtPassword, edtPassword2 ,edtPhonemunber;
    Button btlogin, btdangky, btavatarDk, btHuyDangKy ,btDangkyDK , bts ;
    ImageView imgavatarDangKy;
    TextView txtTaiDay, a;
    CheckBox checkBoxDangKy;
    Toast backToast;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInClient mGoogleSignInClient;
    ConstraintLayout constraintlayout1, constraintlayout2;
    private long backPressedTime;
    private FirebaseAuth mAuth;
    private static final String TAG= " ";
    CallbackManager callbackManager;
    LoginButton loginButton;
    String last_name,email,id,linkavatar;
    int code = 1;
    ProfilePictureView profilePictureView;
    DatabaseReference mdatafirebaselogin, mdatafirebasex;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    String sName, sLinkAvatar, sPhone,sEmail,sID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // ánh xạ
        inten();
        // login google
        SignInButton signInButton = findViewById(R.id.sign_in_button1);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        //facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        setloginfacebook();
//        database firebase
        mdatafirebaselogin= FirebaseDatabase.getInstance().getReference();
        // Storrage
//        FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-app-8e444.appspot.com/img");
        btavatarDk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent2,code);
            }
        });

    }

    private void addProfile(String key, String avatar, String email, String name, String phone){
        final ProfileUser profileUser = new ProfileUser(avatar,email,name,phone);
        mdatafirebaselogin.child("profileuser/"+key).setValue(profileUser);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 001);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // login facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == code && resultCode == RESULT_OK && data!=null){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgavatarDangKy.setImageBitmap(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
        // login google
        if (requestCode == 001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUIGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("qqq", "signInResult:failed code=" + e.getStatusCode());
            updateUIGoogle(null);
        }
    }

    private void updateUIGoogle(GoogleSignInAccount account) {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUIGoogle(account);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    private void createUser(){
        String email = edtGmailDangky.getText().toString();
        String password = edtPassword2.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                addProfile(user.getUid(),"link",edtGmailDangky.getText().toString(),edtNameDK.getText().toString(),edtPhonemunber.getText().toString());
                                updateUI(null);
                                Toast.makeText(Login.this, "Đăng ký Thành công", Toast.LENGTH_SHORT).show();
                                constraintlayout2.setVisibility(View.GONE);
                                constraintlayout1.setVisibility(View.VISIBLE);
                                edtGmail.setText(edtGmailDangky.getText().toString());
                        }else
                            Toast.makeText(Login.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // set edt null
    public void SetNull (){
        edtGmail.setText(null);
        edtMatKhau.setText(null);
        edtGmailDangky.setText(null);
        edtNameDK.setText(null);
        edtPassword.setText(null);
        edtPassword2 .setText(null);
        edtPhonemunber.setText(null);
    }
//    public void  getProfile(String s){
//        mdatafirebasex= FirebaseDatabase.getInstance().getReference().child("profileuser").child(s);
//        //  ea = "6eLrmgHUESVBpFZrPEPPyjUupYZ2";
//        sID = s;
//        mdatafirebasex.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ProfileUser profileUser = dataSnapshot.getValue(ProfileUser.class);
//                sName = profileUser.Name;
//                sEmail = profileUser.Email;
//                sPhone = null;
//                sLinkAvatar = profileUser.AvatarLink;
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
    private void TosendActivity (String s){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("KeyID",s);
        startActivity(intent);
    }
    ////////// sự kiện
    public void eventlick (View view) {
        switch (view.getId()) {
            case R.id.btdangky:
                constraintlayout1.setVisibility(View.GONE);
                constraintlayout2.setVisibility(View.VISIBLE);
                SetNull();
                break;
            case R.id.btHuyDangKy:
                constraintlayout2.setVisibility(View.GONE);
                constraintlayout1.setVisibility(View.VISIBLE);
                SetNull();
                break;
            case R.id.btDangkyDK:

                if (edtGmailDangky.getText().toString().equals(""))
                    Toast.makeText(this, "Chưa nhập Emial", Toast.LENGTH_SHORT).show();
                else if (edtNameDK.getText().toString().equals(""))
                    Toast.makeText(this, "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                else if (edtPhonemunber.getText().toString().equals(""))
                    Toast.makeText(this, "Chưa nhập điện điện thoại", Toast.LENGTH_SHORT).show();
                else if (edtPassword.getText().toString().equals("")&& edtPassword2.getText().toString().equals(""))
                    Toast.makeText(this, "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                else if (edtPassword.getText().toString().equals(edtPassword2.getText().toString())){
                    if (checkBoxDangKy.isChecked()){
                         createUser();
                    }else
                        Toast.makeText(this, "Chưa đồng ý các quy định", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btlogin:
                Sigin();

                break;
        }
    }
    /// sign firebase
    private void Sigin() {
        String email = edtGmail.getText().toString();
        String password = edtMatKhau.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            TosendActivity(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    /////////// anh xa item
    private void inten() {
        edtGmail = findViewById(R.id.edtGmail);
        edtMatKhau= findViewById(R.id.edtMatKhau);
        edtGmailDangky= findViewById(R.id.edtGmailDangky);
        edtNameDK= findViewById(R.id.edtNameDK);
        edtPassword= findViewById(R.id.edtPassword);
        edtPassword2= findViewById(R.id.edtPassword2);
        edtPhonemunber= findViewById(R.id.edtPhonemunber);
        btlogin= findViewById(R.id.btlogin);
        btdangky= findViewById(R.id.btdangky);
        btavatarDk= findViewById(R.id.btavatarDk);
        btHuyDangKy= findViewById(R.id.btHuyDangKy);
        btDangkyDK= findViewById(R.id.btDangkyDK);
        imgavatarDangKy= findViewById(R.id.imgavatarDangKy);
        txtTaiDay= findViewById(R.id.txtTaiDay);
        checkBoxDangKy= findViewById(R.id.checkBoxDangKy);
        constraintlayout1= findViewById(R.id.constraintlayout1);
        constraintlayout2= findViewById(R.id.constraintlayout2);
        a= findViewById(R.id.a);
        checkBoxDangKy= findViewById(R.id.checkBoxDangKy);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void setloginfacebook() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // đăng nhập facebook thành công code
                inputprofilefacebook();
            }
            @Override
            public void onCancel() {
                // App code
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
    private void inputprofilefacebook() { // link https://www.youtube.com/watch?v=oSuZNkOSGRE&list=PLzrVYRai0riSVwL5cv7JaC4aOJjjfSMfv&index=5
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("profile", response.getJSONObject().toString());
                try {
                    last_name = object.getString("last_name");
                    email = object.getString("email");
                    id = Profile.getCurrentProfile().getId();
                    linkavatar = "https://graph.facebook.com/" +id+ "/picture?type=large";
                   // Toast.makeText(Login.this, "xin cháo "+last_name+id +"\n"+linkavatar, Toast.LENGTH_SHORT).show();
                    addProfile(id,linkavatar,email,last_name,"");

                    TosendActivity(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
    /// click back
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "click back", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
