package book.marcelobenjamin.com.Paginas_do_usuario;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import book.marcelobenjamin.com.R;

public class UserPage extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth authy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));
        authy = FirebaseAuth.getInstance();

        TextView btAdd = findViewById(R.id.AddBook);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_ADDBOOK");
                startActivity(go);
            }
        });
        TextView btSair = findViewById(R.id.btSair);
        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authy.signOut();
                onStart();
            }
        });
        TextView inicio = findViewById(R.id.btMain);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_HOME");
                startActivity(go);
            }
        });
        TextView msn = findViewById(R.id.btMsn);
        msn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_MSN");
                startActivity(go);
            }
        });
        TextView msnn = findViewById(R.id.btmsnn);
        msnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_MSN");
                startActivity(go);
            }
        });

        //Abrindo e fechando menu
        final Guideline guiaMenu = findViewById(R.id.guiaMenu);
        final TextView backfade = findViewById(R.id.backfade);
        TextView menu = findViewById(R.id.btMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiaMenu.setGuidelinePercent(Float.parseFloat("0.6"));
                backfade.setVisibility(View.VISIBLE);
            }
        });
        backfade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backfade.setVisibility(View.INVISIBLE);
                guiaMenu.setGuidelinePercent(Float.parseFloat("0.0"));
            }
        });
        //Acoes do menu
        TextView btUser = findViewById(R.id.btUser);
        btUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_USERPAGE");
                startActivity(go);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        final TextView userName = findViewById(R.id.userName);
        final TextView local = findViewById(R.id.userPlace);
        final TextView qtdL = findViewById(R.id.qtdLivros);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child(user.getUid()).child("Dados").child("Nome").getValue(String.class));
                userName.setText(userName.getText()+" "+dataSnapshot.child(user.getUid()).child("Dados").child("Sobrenome").getValue(String.class));
                local.setText(dataSnapshot.child(user.getUid()).child("Dados").child("Local").getValue(String.class)+" - "+dataSnapshot.child(user.getUid()).child("Dados").child("UF").getValue(String.class));
                qtdL.setText(dataSnapshot.child(user.getUid()).child("MLivros").child("Quantidade").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Falha ao ler o nome", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Guideline guiaMenu = findViewById(R.id.guiaMenu);
        TextView backfade = findViewById(R.id.backfade);
        guiaMenu.setGuidelinePercent(Float.parseFloat("0.0"));
        backfade.setVisibility(View.INVISIBLE);
    }

}
