package book.marcelobenjamin.com.Paginas_do_usuario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import book.marcelobenjamin.com.R;

public class NewBook extends AppCompatActivity {
    FirebaseUser user;
    String codLivro = "";
    String qtdLivros = "";
    String codLivroUS = "";
    String qtdLivrosUS = "";
    String localum = "";
    String localdois = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    ImageView enviarImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference refBook = FirebaseDatabase.getInstance().getReference("Livros");
        final DatabaseReference refBkUser = FirebaseDatabase.getInstance().getReference("Usuarios");

        refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                codLivro = dataSnapshot.child("Registros").getValue(String.class);
                codLivro = (Integer.parseInt(codLivro+"") + 1)+"";
                qtdLivros = dataSnapshot.child("Quantidade").getValue(String.class);
                qtdLivros = (Integer.parseInt(qtdLivros+"") + 1)+"";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });

        refBkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                codLivroUS = dataSnapshot.child(user.getUid()).child("MLivros").child("Registros").getValue(String.class);
                codLivroUS = (Integer.parseInt(codLivroUS+"") + 1)+"";
                qtdLivrosUS = dataSnapshot.child(user.getUid()).child("MLivros").child("Quantidade").getValue(String.class);
                qtdLivrosUS = (Integer.parseInt(qtdLivrosUS+"") + 1)+"";

                localum = dataSnapshot.child(user.getUid()).child("Dados").child("Local").getValue(String.class);
                localdois = dataSnapshot.child(user.getUid()).child("Dados").child("UF").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();
            }
        });

        //EdtText
        final EditText titulo = findViewById(R.id.titulo);
        final EditText autor = findViewById(R.id.autor);
        final EditText editora = findViewById(R.id.editora);
        final EditText categoria = findViewById(R.id.cat);
        final EditText datapub = findViewById(R.id.date);
        final EditText qtdLiv = findViewById(R.id.qtdLiv);
        final EditText idioma = findViewById(R.id.idioma);
        final EditText desc = findViewById(R.id.desc);
        final EditText preco = findViewById(R.id.preco);
        final Button cad = findViewById(R.id.cad);

        cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( (titulo.getText()+"").equals("") || (autor.getText()+"").equals("") || (editora.getText()+"").equals("") || (categoria.getText()+"").equals("") ||
                        (datapub.getText()+"").equals("") || (qtdLiv.getText()+"").equals("") || (idioma.getText()+"").equals("") || (desc.getText()+"").equals("") || (preco.getText()+"").equals("") ) {
                    Toast.makeText(getApplicationContext(),"Campos sinalizados com \" * \" não podem ser nulos", Toast.LENGTH_LONG).show();
                }
                else {
                    cad.setVisibility(View.INVISIBLE);
                    criarBook(refBkUser, refBook, titulo.getText()+"", autor.getText()+"", editora.getText()+"", categoria.getText()+"", datapub.getText()+"", qtdLiv.getText()+"", idioma.getText()+"", desc.getText()+"", preco.getText()+"");

                    refBook.child("Quantidade").setValue(qtdLivros);
                    refBook.child("Registros").setValue(codLivro);

                    refBkUser.child(user.getUid()).child("MLivros").child("Quantidade").setValue(qtdLivrosUS);
                    refBkUser.child(user.getUid()).child("MLivros").child("Registros").setValue(codLivroUS);

                    StorageReference imgRef = FirebaseStorage.getInstance().getReference(codLivro+".jpg");
                    sendIMG(imgRef);

                    Intent go = new Intent("ACAO_HOME");
                    startActivity(go);
                }
            }
        });

        //Enviar imagem
        enviarImagem = findViewById(R.id.capa);
        enviarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherImagem();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Button cad = findViewById(R.id.cad);
        cad.setVisibility(View.VISIBLE);
    }

    String qtdUser = "";
    String reg = "";
    public void criarBook(DatabaseReference refBkUser, DatabaseReference refBook, String titulo, String autor, String editora, String categoria, String datapub, String qtdLiv, String idioma, String desc, String preco) {

        final DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Usuarios");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        refBook.child("Todos").child(codLivro).child("Titulo").setValue(titulo);
        refBook.child("Todos").child(codLivro).child("Autor").setValue(autor);
        refBook.child("Todos").child(codLivro).child("Editora").setValue(editora);
        refBook.child("Todos").child(codLivro).child("Categoria").setValue(categoria);
        refBook.child("Todos").child(codLivro).child("DataPub").setValue(datapub);
        refBook.child("Todos").child(codLivro).child("QtdLivros").setValue(qtdLiv);
        refBook.child("Todos").child(codLivro).child("Idioma").setValue(idioma);
        refBook.child("Todos").child(codLivro).child("Descricao").setValue(desc);
        refBook.child("Todos").child(codLivro).child("Preco").setValue(preco);
        refBook.child("Todos").child(codLivro).child("UserUDI").setValue(user.getUid());
        refBook.child("Todos").child(codLivro).child("Local").setValue(localum);
        refBook.child("Todos").child(codLivro).child("UF").setValue(localdois);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        String now = sdf.format(date);
        refBook.child("Todos").child(codLivro).child("Data").setValue(now);
        Toast.makeText(getApplicationContext(), "Publicado", Toast.LENGTH_LONG).show();

    }

    public void escolherImagem() {
        Intent go = new Intent();
        go.setType("image/*");
        go.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(go, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(enviarImagem);
        }
    }

    public void sendIMG(StorageReference imgRef) {
        enviarImagem.setDrawingCacheEnabled(true);
        enviarImagem.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) enviarImagem.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

}
