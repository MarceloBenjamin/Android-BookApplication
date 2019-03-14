package book.marcelobenjamin.com.Paginas_de_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import book.marcelobenjamin.com.R;

public class SingUp extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase data = FirebaseDatabase.getInstance();
    FirebaseUser us;

    TextView backBT = findViewById(R.id.backBT);
    Button singUP = findViewById(R.id.singUP);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));

        create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        backBT.setVisibility(View.VISIBLE);
        singUP.setVisibility(View.VISIBLE);
    }

    private void create() {
        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUP.setVisibility(View.INVISIBLE);
                backBT.setVisibility(View.INVISIBLE);
                createAccount();
            }
        });
    }

    private void createAccount() {
        final EditText email = findViewById(R.id.email);
        final EditText senha = findViewById(R.id.senha);
        final EditText nome = findViewById(R.id.nome);
        final EditText sobrenome = findViewById(R.id.sobrenome);
        final EditText local = findViewById(R.id.locaum);
        final EditText locald = findViewById(R.id.locadois);

        if (email.toString().equals("") || senha.toString().equals("") || nome.toString().equals("") || sobrenome.toString().equals("") || local.toString().equals("") || locald.toString().equals("")) {
            Toast.makeText(getApplicationContext(),"Nenhum campo marcado com \"*\" pode ser nulo!", Toast.LENGTH_SHORT).show();

        }
        else {
            final DatabaseReference dataRef = data.getReference("Usuarios");
            mAuth.createUserWithEmailAndPassword(email.toString(), senha.toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Usuário criado", Toast.LENGTH_SHORT).show();
                                us = mAuth.getCurrentUser();
                                dataRef.child(us.getUid()).child("Dados").child("Nome").setValue(nome.getText()+"");
                                dataRef.child(us.getUid()).child("Dados").child("Sobrenome").setValue(sobrenome.getText()+"");
                                dataRef.child(us.getUid()).child("Dados").child("Email").setValue(email);
                                dataRef.child(us.getUid()).child("Dados").child("Livros").setValue("0");
                                dataRef.child(us.getUid()).child("Dados").child("Local").setValue(local.getText()+"");
                                dataRef.child(us.getUid()).child("Dados").child("UF").setValue(locald.getText()+"");
                                dataRef.child(us.getUid()).child("MLivros").child("Registros").setValue("0");
                                dataRef.child(us.getUid()).child("MLivros").child("Quantidade").setValue("0");

                                Intent go = new Intent("ACAO_SINGIN");
                                startActivity(go);
                            } else {
                                backBT.setVisibility(View.VISIBLE);
                                singUP.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Falha ao criar um novo usuário", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
