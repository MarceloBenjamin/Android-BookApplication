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
import com.google.firebase.auth.FirebaseAuth;

import book.marcelobenjamin.com.R;

public class ResetPassword extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        final EditText edEmail = findViewById(R.id.emailReset);
        final Button btSend = findViewById(R.id.send);
        final TextView backBT = findViewById(R.id.backBT);

        mAuth = FirebaseAuth.getInstance();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((edEmail.getText()+"").equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira um e-mail", Toast.LENGTH_LONG).show();
                }
                else {
                    btSend.setVisibility(View.INVISIBLE);
                    backBT.setVisibility(View.INVISIBLE);
                    resetPassword(edEmail.getText()+"");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView backBT = findViewById(R.id.backBT);
        Button btSend = findViewById(R.id.send);

        backBT.setVisibility(View.VISIBLE);
        btSend.setVisibility(View.VISIBLE);

    }

    public void resetPassword(String email) {

        final TextView backBT = findViewById(R.id.backBT);
        final Button btSend = findViewById(R.id.send);

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"E-mail para alterar senha enviado", Toast.LENGTH_LONG).show();
                            Intent go = new Intent("ACAO_SINGIN");
                            startActivity(go);
                        } else {
                            backBT.setVisibility(View.VISIBLE);
                            btSend.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Falha ao encaminhar um e-mail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
