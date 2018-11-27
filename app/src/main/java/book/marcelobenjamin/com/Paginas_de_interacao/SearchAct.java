package book.marcelobenjamin.com.Paginas_de_interacao;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import book.marcelobenjamin.com.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class SearchAct extends AppCompatActivity {
    ArrayList<Livro> livrosB = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setStatusBarColor(getColor(R.color.Gainsboro3));
        final EditText searchTxt = findViewById(R.id.searchText);
        final Button search = findViewById(R.id.btSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchTxt.getText().equals("")) {
                }
                else {
                    livrosB.clear();
                    livrosB = recDados(searchTxt.getText()+"", livrosB );
                    try {
                        Thread.sleep(3000);
                        RecyclerView rView = findViewById(R.id.recView);
                        rView.setAdapter(new NossoAdapter());
                        RecyclerView.LayoutManager layout = new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false);
                        rView.setLayoutManager(layout);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
    }

    String palavra;
    public ArrayList<Livro> recDados(final String pesquisa, final ArrayList<Livro> livros ) {
        DatabaseReference refBk = FirebaseDatabase.getInstance().getReference("Livros");

        refBk.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int bks = 1;
                int reg = Integer.parseInt(dataSnapshot.child("Registros").getValue(String.class));
                while (bks <= reg) {
                    palavra = dataSnapshot.child("Todos").child(bks+"").child("Titulo").getValue(String.class);
                    ArrayList<String> lavras = new ArrayList<>();
                    char l;
                    for (int i = 0; i < palavra.length(); i++) {
                        String pala = "";
                        boolean t = true;
                        for (int j = i; (j < palavra.length()) && (t != false); j++) {
                            l = palavra.charAt(j);
                            if ((l+"").equals(" ")) {
                                t = false;
                            }
                            else {
                                pala = pala + l;
                            }
                            i = j;
                        }
                        lavras.add(pala);
                    }
                    boolean v = false;
                    int x = 0;
                    while (x < lavras.size()) {
                        if(pesquisa.equals(lavras.get(x))) {
                            v = true;
                        }
                        x++;
                    }
                    if (v == true) {
                        Livro novo = new Livro(dataSnapshot.child("Todos").child(bks+"").child("Titulo").getValue(String.class),
                                dataSnapshot.child("Todos").child(bks+"").child("Preco").getValue(String.class),
                                dataSnapshot.child("Todos").child(bks+"").child("Autor").getValue(String.class),
                                bks);
                        livros.add(novo);
                    }
                    bks++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return livros;

    }

    public class NossoAdapter extends RecyclerView.Adapter {

        public NossoAdapter() {
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.item_livro, parent, false);
            NossoViewHolder holder = new NossoViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                     int position) {
            final NossoViewHolder holder = (NossoViewHolder) viewHolder;
            final Livro bkss  = livrosB.get(position);
            holder.titulo.setText(bkss.getTitulo());
            holder.titulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent("ACAO_PRODUCT");
                    go.putExtra(EXTRA_MESSAGE, bkss.getPos());
                    startActivity(go);
                }
            });
            holder.preco.setText(bkss.getPreco() + " R$");
            holder.preco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent("ACAO_PRODUCT");
                    go.putExtra(EXTRA_MESSAGE, bkss.getPos());
                    startActivity(go);
                }
            });
            holder.autor.setText("Autor: " + bkss.getAutor());
            holder.autor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent("ACAO_PRODUCT");
                    go.putExtra(EXTRA_MESSAGE, bkss.getPos());
                    startActivity(go);
                }
            });
            final int im = bkss.getPos();
            StorageReference refImg = FirebaseStorage.getInstance().getReference(im+".jpg");
            refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(getApplicationContext()).load(uri).into(holder.capa);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            holder.capa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent go = new Intent("ACAO_PRODUCT");
                    go.putExtra(EXTRA_MESSAGE, bkss.getPos());
                    startActivity(go);
                }
            });
        }
        @Override
        public int getItemCount() {
            return livrosB.size();
        }
    }
    public class NossoViewHolder extends RecyclerView.ViewHolder {
        final TextView titulo;
        final TextView autor;
        final TextView preco;
        final ImageView capa;
        public NossoViewHolder(View view) {
            super(view);
            titulo = view.findViewById(R.id.TituloBk);
            autor = view.findViewById(R.id.AutorBk);
            preco = view.findViewById(R.id.PrecoBk);
            capa = view.findViewById(R.id.CapaBk);
            // restante das buscas
        }
    }

    //Classe para os livros
    public class Livro {
        String titulo;
        String preco;
        String autor;
        int pos;
        public Livro(String titulo, String preco, String autor, int pos) {
            this.titulo = titulo;
            this.preco = preco;
            this.autor = autor;
            this.pos = pos;
        }
        public String getTitulo() {
            return titulo;
        }
        public String getPreco() {
            return preco;
        }
        public String getAutor() {
            return autor;
        }
        public int getPos() {
            return pos;
        }
    }
}