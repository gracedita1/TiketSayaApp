package firstbelajar.digitalsoftware.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {
    Button btn_Continue;
    LinearLayout btn_back;
    EditText username, password, email_address;
    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String USERNAME_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_one );
        username = findViewById( R.id.username );
        password = findViewById( R.id.password );
        email_address = findViewById( R.id.email_address );

        btn_Continue = findViewById( R.id.btn_Continue );
        btn_Continue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_Continue.setEnabled( false );
                btn_Continue.setText( "Loading..." );

                //mengambil username pada database
                reference_username = FirebaseDatabase.getInstance().getReference()
                        .child( "Users" ).child(  username.getText().toString() );
                reference_username.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //jika username tersedia
                        if(dataSnapshot.exists()){
                            //Test apakah username sudah masuk
                            Toast.makeText( getApplicationContext(),"Username sudah tersedia!!",
                                    Toast.LENGTH_SHORT).show();

                            //ubah state menjadi active
                            btn_Continue.setEnabled( true );
                            btn_Continue.setText( "CONTINUE" );
                        }
                        else{

                            //menyimpan data kepada lokal stoage / handphone
                            SharedPreferences sharedPreferences = getSharedPreferences( USERNAME_KEY, MODE_PRIVATE );
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString( USERNAME_key, username.getText().toString() );
                            editor.apply();

                            //simpan kepada database
                            reference = FirebaseDatabase.getInstance().getReference()
                                    .child( "Users" ).child( username.getText().toString() );
                            reference.addListenerForSingleValueEvent( new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child( "username" ).setValue( username.getText().toString());
                                    dataSnapshot.getRef().child( "password" ).setValue( password.getText().toString());
                                    dataSnapshot.getRef().child( "email_address" ).setValue( email_address.getText().toString());
                                    dataSnapshot.getRef().child( "user_balance" ).setValue(800);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            } );
                            //Berpindah Act
                            Intent gotonextregister = new Intent(RegisterOneAct.this,RegisterTwoAct.class);
                            startActivity( gotonextregister );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

            }
        } );

        btn_back = findViewById( R.id.btn_back );
        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
    }

}