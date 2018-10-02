package book.marcelobenjamin.com;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingIn extends AppCompatActivity {

    FirebaseAuth authy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        authy = FirebaseAuth.getInstance();

        final EditText edEmail = findViewById(R.id.emailIN);
        final EditText edSenha = findViewById(R.id.senhaIN);

        Button singIN = findViewById(R.id.singIN);
        Button singUP = findViewById(R.id.singUP);

        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_SINGUP");
                startActivity(go);
            }
        });

        singIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((edEmail.getText()+"").equals("") || (edSenha.getText()+"").equals("")) {
                    Toast.makeText(getApplicationContext(),"Os campos não podem ser nulos", Toast.LENGTH_LONG).show();
                }
                else {
                    logar(edEmail.getText()+"", edSenha.getText()+"");
                }
            }
        });

    }

    private void logar(String email, String senha) {
        authy.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Login realizado com sucesso", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Usuário ou senha incorretos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
