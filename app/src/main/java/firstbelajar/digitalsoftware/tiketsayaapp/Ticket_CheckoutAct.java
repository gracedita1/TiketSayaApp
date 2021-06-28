package firstbelajar.digitalsoftware.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

public class Ticket_CheckoutAct extends AppCompatActivity {
    LinearLayout btn_back;
    Button btn_pay;
    ImageButton  btn_mines, btn_plus;
    ImageView notice_uang;
    TextView textjumlah_ticket, texttotalharga, textmybalance, Nama_wisata,lokasi, ketentuan;
    Integer valueJumlahTicket = 1;
    Integer mybalance = 0;
    Integer valuetotalharga = 0;
    Integer valuehargatiket = 0;
    Integer sisa_balance = 0;

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String USERNAME_key = "";
    String username_key_new = "";

    String date_wisata, time_wisata;

    //generate int secara random
    //ingin membuat transaksi secara unik
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ticket__checkout );

        getUsernameLocal();

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        String jenis_tiket_baru = bundle.getString( "jenis_tiket" );

        btn_mines = findViewById( R.id.btn_mines );
        btn_plus = findViewById( R.id.btn_plus );
        textjumlah_ticket = findViewById( R.id.textjumlah_ticket );
        texttotalharga = findViewById( R.id.texttotalharga );
        textmybalance = findViewById( R.id.textmybalance );
        btn_pay = findViewById( R.id.btn_pay);
        notice_uang = findViewById( R.id.notice_uang);
        btn_back = findViewById( R.id.btn_back );
        Nama_wisata = findViewById( R.id.Nama_wisata );
        lokasi = findViewById( R.id.lokasi );
        ketentuan = findViewById( R.id.ketentuan );



        //setting value baru untuk beberapa komponen
        textjumlah_ticket.setText( valueJumlahTicket.toString() );

        //secara default, kita hide btnmines
        btn_mines.animate().alpha( 0 ).setDuration( 300 ).start();
        btn_mines.setEnabled( false );
        notice_uang.setVisibility( View.GONE );

        //mengambil data user dari firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child( "Users" ).child(username_key_new);
        reference2.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mybalance= Integer.valueOf( dataSnapshot.child( "user_balance" ).getValue().toString() );
                textmybalance.setText( "US$ " + mybalance + "" );
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        } );
        //mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child( "Wisata" )
                .child( jenis_tiket_baru );
        reference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data yang baru
                Nama_wisata.setText( dataSnapshot.child( "Nama_wisata" ).getValue().toString() );
                lokasi.setText( dataSnapshot.child( "lokasi" ).getValue().toString() );
                ketentuan.setText( dataSnapshot.child( "ketentuan" ).getValue().toString() );
                date_wisata = dataSnapshot.child( "date_wisata" ).getValue().toString();
                time_wisata = dataSnapshot.child( "time_wisata" ).getValue().toString();
                valuehargatiket = Integer.valueOf( dataSnapshot.child( "Harga_tiket" ).getValue().toString() );

                valuetotalharga = valuehargatiket * valueJumlahTicket;
                texttotalharga.setText( "US$ " + valuetotalharga + "" );

            }
                @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );


        btn_plus.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTicket += 1;
                textjumlah_ticket.setText( valueJumlahTicket.toString() );
                if (valueJumlahTicket > 1){
                    btn_mines.animate().alpha( 1 ).setDuration( 300 ).start();
                    btn_mines.setEnabled( true );
                }
                valuetotalharga = valuehargatiket * valueJumlahTicket;
                texttotalharga.setText( "US$ " + valuetotalharga + "" );
                if(valuetotalharga > mybalance){
                    btn_pay.animate().translationY( 250 ).alpha( 0 ).setDuration( 350 ).start();
                    btn_pay.setEnabled( false );
                    textmybalance.setTextColor( Color.parseColor( "#D1206B"  ) ) ;
                    notice_uang.setVisibility( View.VISIBLE );
                }
            }
        } );


        btn_mines.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTicket -= 1;
                textjumlah_ticket.setText( valueJumlahTicket.toString() );
                if (valueJumlahTicket < 2){
                    btn_mines.animate().alpha( 0 ).setDuration( 300 ).start();
                    btn_mines.setEnabled( false );
                }

                valuetotalharga = valuehargatiket * valueJumlahTicket;
                texttotalharga.setText( "US$ " + valuetotalharga + "" );
                if(valuetotalharga < mybalance){
                    btn_pay.animate().translationY( 0 ).alpha( 1 ).setDuration( 350 ).start();
                    btn_pay.setEnabled( true );
                    textmybalance.setTextColor( Color.parseColor( "#203DD1"  ) ) ;
                    notice_uang.setVisibility( View.GONE );

                }
            }
        } );

        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(Ticket_CheckoutAct.this,HomeAct.class);
                startActivity( backtohome );
            }
        } );


        btn_pay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan data user kepada firebase dan membuat table baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference().child( "MyTickets" ).child(username_key_new).child( Nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reference3.getRef().child( "id_tiket" ).setValue( Nama_wisata.getText().toString() + nomor_transaksi );
                        reference3.getRef().child( "Nama_wisata" ).setValue( Nama_wisata.getText().toString() );
                        reference3.getRef().child( "lokasi" ).setValue( lokasi.getText().toString() );
                        reference3.getRef().child( "ketentuan" ).setValue( ketentuan.getText().toString() );
                        reference3.getRef().child( "date_wisata" ).setValue( date_wisata);
                        reference3.getRef().child( "time_wisata" ).setValue( time_wisata);
                        reference3.getRef().child( "jumlah_tiket" ).setValue(valueJumlahTicket.toString());

                        Intent gotosuccesticket = new Intent(Ticket_CheckoutAct.this,SuccessBuyTicketAct.class);
                        startActivity( gotosuccesticket );
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError) {

                    }
                } );
                //update data balance kepada users (yang saat ini login)
                //mengambil data user dari firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child( "Users" ).child(username_key_new);
                reference4.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        sisa_balance = mybalance - valuetotalharga;
                        reference4.getRef().child( "user_balance" ).setValue(sisa_balance);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
            }
        } );

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE );
        username_key_new = sharedPreferences.getString( USERNAME_key, "" );
    }
}