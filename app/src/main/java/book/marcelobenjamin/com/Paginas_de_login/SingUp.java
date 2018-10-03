package book.marcelobenjamin.com.Paginas_de_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import book.marcelobenjamin.com.R;

public class SingUp extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        final EditText email = findViewById(R.id.emailReset);
        final EditText senha = findViewById(R.id.senha);
        final TextView backBT = findViewById(R.id.backBT);
        final Button singUP = findViewById(R.id.singUP);

        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((email.getText()+"").equals("") || (senha.getText()+"").equals("")) {
                    Toast.makeText(getApplicationContext(),"Campos sinalizados com \" * \" não podem ser nulos", Toast.LENGTH_LONG).show();
                }
                else {
                    singUP.setVisibility(View.INVISIBLE);
                    backBT.setVisibility(View.INVISIBLE);
                    createAccount(email.getText()+"", senha.getText()+"");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final TextView backBT = findViewById(R.id.backBT);
        final Button singUP = findViewById(R.id.singUP);

        backBT.setVisibility(View.VISIBLE);
        singUP.setVisibility(View.VISIBLE);
    }

    private void createAccount(String email, String senha) {

        final TextView backBT = findViewById(R.id.backBT);
        final Button singUP = findViewById(R.id.singUP);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Usuário criado", Toast.LENGTH_LONG).show();
                            Intent go = new Intent("ACAO_SINGIN");
                            startActivity(go);
                        } else {
                            backBT.setVisibility(View.VISIBLE);
                            singUP.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Falha ao criar um novo usuário", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}
