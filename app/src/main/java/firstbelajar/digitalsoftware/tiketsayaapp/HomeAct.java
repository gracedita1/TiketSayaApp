package firstbelajar.digitalsoftware.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {
    LinearLayout btn_ticket_pisa,btn_ticket_torri,btn_ticket_pagoda,
    btn_ticket_candi,btn_ticket_sphinx,btn_ticket_monas;
    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap, user_balance, bio;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String USERNAME_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        getUsernameLocal();

        btn_ticket_pisa = findViewById( R.id.btn_ticket_pisa );
        btn_to_profile = findViewById( R.id.btn_to_profil );

        btn_ticket_torri = findViewById( R.id.btn_ticket_torri );
        btn_ticket_pagoda = findViewById( R.id.btn_ticket_pagoda );
        btn_ticket_candi = findViewById( R.id.btn_ticket_candi );
        btn_ticket_sphinx = findViewById( R.id.btn_ticket_sphinx );
        btn_ticket_monas = findViewById( R.id.btn_ticket_monas );


        nama_lengkap = findViewById( R.id.nama_lengkap );
        user_balance = findViewById( R.id.user_balance );
        bio = findViewById( R.id.bio );
        photo_home_user = findViewById( R.id.photo_home_user );


        reference = FirebaseDatabase.getInstance().getReference()
                .child( "Users" ).child(username_key_new);

        reference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nama_lengkap.setText( dataSnapshot.child( "nama_lengkap" ).getValue().toString() );
                bio.setText( dataSnapshot.child( "bio" ).getValue().toString() );
                user_balance.setText( "US$" + dataSnapshot.child( "user_balance" ).getValue().toString() );
                Picasso.with( HomeAct.this ).load( dataSnapshot.child( "url_photo_profile" ).getValue().toString()).into( photo_home_user );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        btn_to_profile.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofil = new Intent(HomeAct.this,My_ProfilAct.class);
                startActivity( gotoprofil );
            }
        } );

        btn_ticket_pisa.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                //meletakkan data pada intent
                gotopisaticket.putExtra( "jenis_tiket","Pisa" );
                startActivity( gotopisaticket );
            }
        } );
        btn_ticket_torri.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                gotopisaticket.putExtra( "jenis_tiket","Torri" );
                startActivity( gotopisaticket );
            }
        } );
        btn_ticket_pagoda.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                gotopisaticket.putExtra( "jenis_tiket","Pagoda" );
                startActivity( gotopisaticket );
            }
        } );
        btn_ticket_candi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                gotopisaticket.putExtra( "jenis_tiket","Candi" );
                startActivity( gotopisaticket );
            }
        } );
        btn_ticket_sphinx.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                gotopisaticket.putExtra( "jenis_tiket","Sphinx" );
                startActivity( gotopisaticket );
            }
        } );
        btn_ticket_monas.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopisaticket = new Intent(HomeAct.this,TicketDetail.class);
                gotopisaticket.putExtra( "jenis_tiket","Monas" );
                startActivity( gotopisaticket );
            }
        } );
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE );
        username_key_new = sharedPreferences.getString( USERNAME_key, "" );
    }
}