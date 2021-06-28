package firstbelajar.digitalsoftware.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

 import java.util.ArrayList;

public class My_ProfilAct extends AppCompatActivity {
    LinearLayout item_my_ticket;
    Button btn_edit_profile,  btn_sign_out;
    ImageButton btn_back_home;

    DatabaseReference reference, reference2;

    TextView nama_lengkap, bio;
    ImageView photo_profile;

    String USERNAME_KEY = "usernamekey";
    String USERNAME_key = "";
    String username_key_new = "";

    RecyclerView myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my__profil );

        getUsernameLocal();

        item_my_ticket = findViewById( R.id.item_my_ticket );
        btn_edit_profile = findViewById( R.id.btn_edit_profile );
        btn_back_home = findViewById( R.id.btn_back_home );
        btn_sign_out= findViewById( R.id.btn_sign_out );



        nama_lengkap = findViewById( R.id.nama_lengkap );
        bio = findViewById( R.id.bio );
        photo_profile = findViewById( R.id.photo_profile );
        btn_sign_out = findViewById( R.id.btn_sign_out );

        myticket_place = findViewById( R.id.myticket_place );
        myticket_place.setLayoutManager( new LinearLayoutManager( this ) );
        list = new ArrayList<MyTicket>();

        reference = FirebaseDatabase.getInstance().getReference().child( "Users" ).child(username_key_new);
        reference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_lengkap.setText( dataSnapshot.child( "nama_lengkap" ).getValue().toString() );
                bio.setText( dataSnapshot.child( "bio" ).getValue().toString() );
                Picasso.with(My_ProfilAct.this).load(dataSnapshot.child( "url_photo_profile" ).getValue().toString() )
                        .centerCrop().fit().into( photo_profile );
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        } );

        btn_edit_profile = findViewById( R.id.btn_edit_profile );
        btn_edit_profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyeditprofile = new Intent(My_ProfilAct.this,EditProfilAct.class);
                startActivity( gotomyeditprofile );
            }
        } );
        reference2 = FirebaseDatabase.getInstance().
                getReference().child( "MyTickets" ).child( username_key_new );
        reference2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    list.add( p );
                }
                ticketAdapter = new TicketAdapter( My_ProfilAct.this, list );
                myticket_place.setAdapter( ticketAdapter );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
        btn_back_home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(My_ProfilAct.this,HomeAct.class);
                startActivity( gotohome );
            }
        } );

        btn_sign_out.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menghaspus isi / nilai value dari username lokal
                //menyimpan data kepada lokal stoage / handphone
                SharedPreferences sharedPreferences = getSharedPreferences( USERNAME_KEY, MODE_PRIVATE );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString( USERNAME_key, null );
                editor.apply();

                //berpindah activity
                Intent gotohome = new Intent(My_ProfilAct.this,SignInAct.class);
                startActivity( gotohome );
                finish();
            }
        } );

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE );
        username_key_new = sharedPreferences.getString( USERNAME_key, "" );
    }
}