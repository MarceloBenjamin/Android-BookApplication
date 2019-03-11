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

import book.marcelobenjamin.com.R;

public class SingIn extends AppCompatActivity {

    final FirebaseAuth authy = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));

        singIn();
        singUp();
        resetPassword();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Button singIN = findViewById(R.id.singIN);
        Button singUP = findViewById(R.id.singUP);
        Button reset = findViewById(R.id.reset);
        TextView backBT = findViewById(R.id.backReset);
        EditText edSenha = findViewById(R.id.senhaIN);

        singIN.setVisibility(View.VISIBLE);
        singUP.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);
        backBT.setVisibility(View.VISIBLE);
        edSenha.setText("");
    }

    private void singUp() {
        final Button singUP = findViewById(R.id.singUP);
        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUP.setVisibility(View.INVISIBLE);
                Intent go = new Intent("ACAO_SINGUP");
                startActivity(go);
            }
        });
    }

    private void resetPassword() {
        final Button reset = findViewById(R.id.reset);
        final TextView backBT = findViewById(R.id.backReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.setVisibility(View.INVISIBLE);
                backBT.setVisibility(View.INVISIBLE);
                Intent go = new Intent("ACAO_RESET");
                startActivity(go);
            }
        });
    }

    private void singIn() {
        final EditText edEmail = findViewById(R.id.emailIN);
        final EditText edSenha = findViewById(R.id.senhaIN);
        final Button singIN = findViewById(R.id.singIN);

        singIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((edEmail.getText()+"").equals("") || (edSenha.getText()+"").equals("")) {
                    Toast.makeText(getApplicationContext(),"Obs: Os campos não podem ser nulos", Toast.LENGTH_LONG).show();
                }
                else {
                    singIN.setVisibility(View.INVISIBLE);
                    logar(edEmail.getText().toString(), edSenha.getText().toString());
                }
            }
        });
    }

    private void logar(String email, String senha) {
        final Button singIN = findViewById(R.id.singIN);
        authy.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent go = new Intent("ACAO_HOME");
                            startActivity(go);
                        } else {
                            singIN.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Usuário ou senha incorreto!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
