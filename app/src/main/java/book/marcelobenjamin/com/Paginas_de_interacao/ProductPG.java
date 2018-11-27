package book.marcelobenjamin.com.Paginas_de_interacao;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import book.marcelobenjamin.com.R;

public class ProductPG extends AppCompatActivity {
    String bookCod;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));
        setContentView(R.layout.activity_product_pg);
        DatabaseReference refBook = FirebaseDatabase.getInstance().getReference("Livros");
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Usuarios");


        Bundle codBook = getIntent().getExtras();
        bookCod = codBook.getString("CodigoLivro");
        StorageReference imgRef = FirebaseStorage.getInstance().getReference(bookCod+".jpg");

        final TextView titulo = findViewById(R.id.titulo);
        final TextView autor = findViewById(R.id.autor);
        final TextView editora = findViewById(R.id.editora);
        final TextView idioma = findViewById(R.id.idioma);
        final TextView dispor = findViewById(R.id.dispo);
        final TextView preco = findViewById(R.id.preco);
        final TextView loca = findViewById(R.id.localizacao);
        final TextView textDesc = findViewById(R.id.DescTxt);
        final TextView pubBY = findViewById(R.id.pubBy);
        final ImageView capa = findViewById(R.id.capa);

        refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titulo.setText(dataSnapshot.child("Todos").child(bookCod).child("Titulo").getValue(String.class));
                autor.setText("Autor: "+dataSnapshot.child("Todos").child(bookCod).child("Autor").getValue(String.class));
                editora.setText("Editora: "+dataSnapshot.child("Todos").child(bookCod).child("Editora").getValue(String.class));
                idioma.setText("Idioma: "+dataSnapshot.child("Todos").child(bookCod).child("Idioma").getValue(String.class));
                dispor.setText("Disponíveis: "+dataSnapshot.child("Todos").child(bookCod).child("QtdLivros").getValue(String.class));
                loca.setText(dataSnapshot.child("Todos").child(bookCod).child("Local").getValue(String.class)+" - "+dataSnapshot.child("Todos").child(bookCod).child("UF").getValue(String.class));
                textDesc.setText(dataSnapshot.child("Todos").child(bookCod).child("Descricao").getValue(String.class));
                preco.setText("R$: "+dataSnapshot.child("Todos").child(bookCod).child("Preco").getValue(String.class));
                user = dataSnapshot.child("Todos").child(bookCod).child("UserUDI").getValue(String.class)+"";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pubBY.setText("Disponibilizado por: "+dataSnapshot.child(user).child("Dados").child("Nome").getValue(String.class)+" "+dataSnapshot.child(user).child("Dados").child("Sobrenome").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext()).load(uri).into(capa);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }
}
