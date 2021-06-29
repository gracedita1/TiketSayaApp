package firstbelajar.digitalsoftware.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetail extends AppCompatActivity {
    Button btn_buy_ticket;
    LinearLayout btn_back;
    TextView location_ticket,title_ticket,photo_spot_ticket
            ,wifi_ticket,festival_ticket,short_desc_ticket;

    DatabaseReference reference;

    ImageView header_ticket_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ticket_detail );

        btn_back = findViewById( R.id.btn_back );
        btn_buy_ticket = findViewById( R.id.btn_buy_ticket );
        header_ticket_detail = findViewById( R.id.header_ticket_detail );


        location_ticket = findViewById( R.id.location_ticket );
        title_ticket = findViewById( R.id.title_ticket );
        photo_spot_ticket = findViewById( R.id.photo_spot_ticket );
        wifi_ticket = findViewById( R.id.wifi_ticket );
        festival_ticket = findViewById( R.id.festival_ticket );
        short_desc_ticket = findViewById( R.id.short_desc_ticket );


        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString( "jenis_tiket" );

        //mengambil data dar9 firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child( "Wisata" )
                .child( jenis_tiket_baru );
        reference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data yang baru
                title_ticket.setText( dataSnapshot.child( "Nama_wisata" ).getValue().toString() );
                location_ticket.setText( dataSnapshot.child( "lokasi" ).getValue().toString() );
                photo_spot_ticket.setText( dataSnapshot.child( "is_photo_spot" ).getValue().toString() );
                wifi_ticket.setText( dataSnapshot.child( "is_wifi" ).getValue().toString() );
                festival_ticket.setText( dataSnapshot.child( "is_festival" ).getValue().toString() );
                short_desc_ticket.setText( dataSnapshot.child( "short_desc" ).getValue().toString() );
                Picasso.with(TicketDetail.this).load(dataSnapshot.child( "url_thumnail" ).getValue().toString() )
                        .centerCrop().fit().into( header_ticket_detail );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

        btn_buy_ticket.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocheckout = new Intent(TicketDetail.this, Ticket_CheckoutAct.class);
                //meletakkan data pada intent
                gotocheckout.putExtra( "jenis_tiket", jenis_tiket_baru );
                startActivity( gotocheckout );
            }
        } );
        btn_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );


    }
}