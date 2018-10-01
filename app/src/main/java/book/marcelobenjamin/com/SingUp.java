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

public class SingUp extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        final EditText email = findViewById(R.id.email);
        final EditText senha = findViewById(R.id.senha);

        Button singUP = findViewById(R.id.singUP);

        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((email.getText()+"").equals("") || (senha.getText()+"").equals("")) {
                    Toast.makeText(getApplicationContext(),"Campos sinalizados com \" * \" não podem ser nulos", Toast.LENGTH_LONG).show();
                }
                else {
                    createAccount(email.getText()+"", senha.getText()+"");
                }
            }
        });

    }

    private void createAccount(String email, String senha) {

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
                            Toast.makeText(getApplicationContext(),"Falha ao criar um novo usuário", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}
