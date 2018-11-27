package book.marcelobenjamin.com.Paginas_de_interacao;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Random;

import book.marcelobenjamin.com.R;

public class HomePG extends AppCompatActivity {
    FirebaseAuth authy;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String EXTRA_MESSAGE = "CodigoLivro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pg);
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
        Button btmsngo = findViewById(R.id.button);
        btmsngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_MSN");
                startActivity(go);
            }
        });

        //Pesquisa
        Button search = findViewById(R.id.Search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent("ACAO_SEARCH");
                startActivity(go);
            }
        });

        //Abrindo e fechando menu
        final Guideline guiaMenu = findViewById(R.id.guiaMenu);
        final TextView backfade = findViewById(R.id.backfade);
        Button menu = findViewById(R.id.Menu);
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
        if (user != null) {
            DatabaseReference refBook = FirebaseDatabase.getInstance().getReference("Livros");
            getRecent(refBook);
            getProximos(refBook);
            getRecomendados(refBook);
        } else {
            Intent go = new Intent("ACAO_SINGIN");
            startActivity(go);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Guideline guiaMenu = findViewById(R.id.guiaMenu);
        TextView backfade = findViewById(R.id.backfade);
        guiaMenu.setGuidelinePercent(Float.parseFloat("0.0"));
        backfade.setVisibility(View.INVISIBLE);

    }

    //Variáveis para getRecent
    String refRecem = "";
    int reg;
    int pic;
    public void getRecent(DatabaseReference refBook) {
        //Butões Recém adicionados
        final ArrayList<ImageView> btRecem = new ArrayList<>();
        ImageView btLan01 = findViewById(R.id.BtLan01);
        ImageView btLan02 = findViewById(R.id.BtLan02);
        ImageView btLan03 = findViewById(R.id.BtLan03);
        ImageView btLan04 = findViewById(R.id.BtLan04);
        ImageView btLan05 = findViewById(R.id.BtLan05);
        ImageView btLan06 = findViewById(R.id.BtLan06);
        ImageView btLan07 = findViewById(R.id.BtLan07);
        btRecem.add(btLan01);
        btRecem.add(btLan02);
        btRecem.add(btLan03);
        btRecem.add(btLan04);
        btRecem.add(btLan05);
        btRecem.add(btLan06);
        btRecem.add(btLan07);
        //Textos Recém adicionados
        final ArrayList<TextView> txRecem = new ArrayList<>();
        TextView txLan01 = findViewById(R.id.TxLan01);
        TextView txLan02 = findViewById(R.id.TxLan02);
        TextView txLan03 = findViewById(R.id.TxLan03);
        TextView txLan04 = findViewById(R.id.TxLan04);
        TextView txLan05 = findViewById(R.id.TxLan05);
        TextView txLan06 = findViewById(R.id.TxLan06);
        TextView txLan07 = findViewById(R.id.TxLan07);
        txRecem.add(txLan01);
        txRecem.add(txLan02);
        txRecem.add(txLan03);
        txRecem.add(txLan04);
        txRecem.add(txLan05);
        txRecem.add(txLan06);
        txRecem.add(txLan07);
        //Textos 2 Recém adicionados
        final ArrayList<TextView> txRecem2 = new ArrayList<>();
        TextView txLan011 = findViewById(R.id.TxLan01_1);
        TextView txLan022 = findViewById(R.id.TxLan02_1);
        TextView txLan033 = findViewById(R.id.TxLan03_1);
        TextView txLan044 = findViewById(R.id.TxLan04_1);
        TextView txLan055 = findViewById(R.id.TxLan05_1);
        TextView txLan066 = findViewById(R.id.TxLan06_1);
        TextView txLan077 = findViewById(R.id.TxLan07_1);
        txRecem2.add(txLan011);
        txRecem2.add(txLan022);
        txRecem2.add(txLan033);
        txRecem2.add(txLan044);
        txRecem2.add(txLan055);
        txRecem2.add(txLan066);
        txRecem2.add(txLan077);
        refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refRecem = dataSnapshot.child("Registros").getValue(String.class);
                reg = Integer.parseInt(refRecem);
                pic = Integer.parseInt(refRecem);
                String ti;
                String au;
                int x = 0;
                while (x < 7 && reg > 0) {

                    if (dataSnapshot.child("Todos").child(reg+"").child("Titulo").getValue(String.class).equals("")) {
                        Toast.makeText(getApplicationContext(), "Era igual a nulu", Toast.LENGTH_SHORT).show();
                        reg--;
                    }
                    else {
                        final int p = reg;
                        if(dataSnapshot.child("Todos").child(reg+"").child("Titulo").getValue(String.class).length() > 14) {
                            ti = dataSnapshot.child("Todos").child(reg+"").child("Titulo").getValue(String.class);
                            int t = 0;
                            txRecem.get(x).setText("");
                            while (t < 13) {
                                txRecem.get(x).setText(txRecem.get(x).getText()+((ti.charAt(t)+"")));
                                t++;
                            }
                            txRecem.get(x).setText(txRecem.get(x).getText()+"..");

                        } else {
                            txRecem.get(x).setText(dataSnapshot.child("Todos").child(reg+"").child("Titulo").getValue(String.class));
                        }

                        if(dataSnapshot.child("Todos").child(reg+"").child("Autor").getValue(String.class).length() > 14) {
                            au = dataSnapshot.child("Todos").child(reg+"").child("Autor").getValue(String.class);
                            int t = 0;
                            txRecem2.get(x).setText("");
                            while (t < 14) {
                                txRecem2.get(x).setText(txRecem2.get(x).getText()+((au.charAt(t)+"")));
                                t++;
                            }
                            txRecem2.get(x).setText(txRecem2.get(x).getText()+"..");

                        } else {
                            txRecem2.get(x).setText(dataSnapshot.child("Todos").child(reg+"").child("Autor").getValue(String.class));
                        }

                        btRecem.get(x).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent go = new Intent("ACAO_PRODUCT");
                                go.putExtra(EXTRA_MESSAGE, p+"");
                                startActivity(go);
                            }
                        });
                        final int im = x;
                        StorageReference refImg = FirebaseStorage.getInstance().getReference(reg+".jpg");
                        refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext()).load(uri).into(btRecem.get(im));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        x++;
                        reg--;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //variaveis para proximos
    String refProx = "";
    int regp;
    String localp;
    String UFp;
    public void getProximos(DatabaseReference refBook) {
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Usuarios");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ArrayList<ImageView> btRecem = new ArrayList<>();
        ImageView btLan01 = findViewById(R.id.BtPro01);
        ImageView btLan02 = findViewById(R.id.BtPro02);
        ImageView btLan03 = findViewById(R.id.BtPro03);
        ImageView btLan04 = findViewById(R.id.BtPro04);
        ImageView btLan05 = findViewById(R.id.BtPro05);
        ImageView btLan06 = findViewById(R.id.BtPro06);
        ImageView btLan07 = findViewById(R.id.BtPro07);
        btRecem.add(btLan01);
        btRecem.add(btLan02);
        btRecem.add(btLan03);
        btRecem.add(btLan04);
        btRecem.add(btLan05);
        btRecem.add(btLan06);
        btRecem.add(btLan07);
        //Textos proximos adicionados
        final ArrayList<TextView> txRecem = new ArrayList<>();
        TextView txLan01 = findViewById(R.id.TxPro01);
        TextView txLan02 = findViewById(R.id.TxPro02);
        TextView txLan03 = findViewById(R.id.TxPro03);
        TextView txLan04 = findViewById(R.id.TxPro04);
        TextView txLan05 = findViewById(R.id.TxPro05);
        TextView txLan06 = findViewById(R.id.TxPro06);
        TextView txLan07 = findViewById(R.id.TxPro07);
        txRecem.add(txLan01);
        txRecem.add(txLan02);
        txRecem.add(txLan03);
        txRecem.add(txLan04);
        txRecem.add(txLan05);
        txRecem.add(txLan06);
        txRecem.add(txLan07);
        //Textos 2 proximos adicionados
        final ArrayList<TextView> txRecem2 = new ArrayList<>();
        TextView txLan011 = findViewById(R.id.TxPro01_1);
        TextView txLan022 = findViewById(R.id.TxPro02_1);
        TextView txLan033 = findViewById(R.id.TxPro03_1);
        TextView txLan044 = findViewById(R.id.TxPro04_1);
        TextView txLan055 = findViewById(R.id.TxPro05_1);
        TextView txLan066 = findViewById(R.id.TxPro06_1);
        TextView txLan077 = findViewById(R.id.TxPro07_1);
        txRecem2.add(txLan011);
        txRecem2.add(txLan022);
        txRecem2.add(txLan033);
        txRecem2.add(txLan044);
        txRecem2.add(txLan055);
        txRecem2.add(txLan066);
        txRecem2.add(txLan077);

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                localp = dataSnapshot.child(user.getUid()).child("Dados").child("Local").getValue(String.class);
                UFp = dataSnapshot.child(user.getUid()).child("Dados").child("UF").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refProx = dataSnapshot.child("Registros").getValue(String.class);
                regp = Integer.parseInt(refProx);
                String ti;
                String au;
                int xp = 0;
                while (xp < 7 && regp > 0) {

                    if (dataSnapshot.child("Todos").child(regp+"").child("Titulo").getValue(String.class).equals("")) {
                        Toast.makeText(getApplicationContext(), "Era igual a nulu", Toast.LENGTH_SHORT).show();
                        regp--;
                    }
                    else {

                        if (localp.equals((dataSnapshot.child("Todos").child(regp+"").child("Local").getValue(String.class))) && UFp.equals((dataSnapshot.child("Todos").child(regp+"").child("UF").getValue(String.class)))) {

                            final int p = regp;
                            if(dataSnapshot.child("Todos").child(regp+"").child("Titulo").getValue(String.class).length() > 14) {
                                ti = dataSnapshot.child("Todos").child(regp+"").child("Titulo").getValue(String.class);
                                int t = 0;
                                txRecem.get(xp).setText("");
                                while (t < 13) {
                                    txRecem.get(xp).setText(txRecem.get(xp).getText()+((ti.charAt(t)+"")));
                                    t++;
                                }
                                txRecem.get(xp).setText(txRecem.get(xp).getText()+"..");

                            } else {
                                txRecem.get(xp).setText(dataSnapshot.child("Todos").child(regp+"").child("Titulo").getValue(String.class));
                            }

                            if(dataSnapshot.child("Todos").child(regp+"").child("Autor").getValue(String.class).length() > 14) {
                                au = dataSnapshot.child("Todos").child(regp+"").child("Autor").getValue(String.class);
                                int t = 0;
                                txRecem2.get(xp).setText("");
                                while (t < 14) {
                                    txRecem2.get(xp).setText(txRecem2.get(xp).getText()+((au.charAt(t)+"")));
                                    t++;
                                }
                                txRecem2.get(xp).setText(txRecem2.get(xp).getText()+"..");

                            } else {
                                txRecem2.get(xp).setText(dataSnapshot.child("Todos").child(regp+"").child("Autor").getValue(String.class));
                            }

                            btRecem.get(xp).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent go = new Intent("ACAO_PRODUCT");
                                    go.putExtra(EXTRA_MESSAGE, p+"");
                                    startActivity(go);
                                }
                            });
                            final int im = xp;
                            StorageReference refImg = FirebaseStorage.getInstance().getReference(regp+".jpg");
                            refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(getApplicationContext()).load(uri).into(btRecem.get(im));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            xp++;
                        }

                        regp--;
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //variaveis para proximos
    String refRec = "";
    int regRec;
    int vet[] = new int[7];
    public void getRecomendados(DatabaseReference refBook) {
        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Usuarios");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ArrayList<ImageView> btRecem = new ArrayList<>();
        ImageView btLan01 = findViewById(R.id.BtRec01);
        ImageView btLan02 = findViewById(R.id.BtRec02);
        ImageView btLan03 = findViewById(R.id.BtRec03);
        ImageView btLan04 = findViewById(R.id.BtRec04);
        ImageView btLan05 = findViewById(R.id.BtRec05);
        ImageView btLan06 = findViewById(R.id.BtRec06);
        ImageView btLan07 = findViewById(R.id.BtRec07);
        btRecem.add(btLan01);
        btRecem.add(btLan02);
        btRecem.add(btLan03);
        btRecem.add(btLan04);
        btRecem.add(btLan05);
        btRecem.add(btLan06);
        btRecem.add(btLan07);
        //Textos proximos adicionados
        final ArrayList<TextView> txRecem = new ArrayList<>();
        TextView txLan01 = findViewById(R.id.TxRec01);
        TextView txLan02 = findViewById(R.id.TxRec02);
        TextView txLan03 = findViewById(R.id.TxRec03);
        TextView txLan04 = findViewById(R.id.TxRec04);
        TextView txLan05 = findViewById(R.id.TxRec05);
        TextView txLan06 = findViewById(R.id.TxRec06);
        TextView txLan07 = findViewById(R.id.TxRec07);
        txRecem.add(txLan01);
        txRecem.add(txLan02);
        txRecem.add(txLan03);
        txRecem.add(txLan04);
        txRecem.add(txLan05);
        txRecem.add(txLan06);
        txRecem.add(txLan07);
        //Textos 2 proximos adicionados
        final ArrayList<TextView> txRecem2 = new ArrayList<>();
        TextView txLan011 = findViewById(R.id.TxRec01_1);
        TextView txLan022 = findViewById(R.id.TxRec02_1);
        TextView txLan033 = findViewById(R.id.TxRec03_1);
        TextView txLan044 = findViewById(R.id.TxRec04_1);
        TextView txLan055 = findViewById(R.id.TxRec05_1);
        TextView txLan066 = findViewById(R.id.TxRec06_1);
        TextView txLan077 = findViewById(R.id.TxRec07_1);
        txRecem2.add(txLan011);
        txRecem2.add(txLan022);
        txRecem2.add(txLan033);
        txRecem2.add(txLan044);
        txRecem2.add(txLan055);
        txRecem2.add(txLan066);
        txRecem2.add(txLan077);

        refBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refRec = dataSnapshot.child("Registros").getValue(String.class);
                regRec = Integer.parseInt(refRec);
                String ti;
                String au;
                int xp = 0;
                int h;

                while (xp < 7 && regRec > 0) {
                    h = regRec;

                    if (dataSnapshot.child("Todos").child(h+"").child("Titulo").getValue(String.class).equals("")) {
                        Toast.makeText(getApplicationContext(), "Era igual a nulu", Toast.LENGTH_SHORT).show();
                        regRec--;
                    }
                    else {

                        final int p = h;
                        if(dataSnapshot.child("Todos").child(h+"").child("Titulo").getValue(String.class).length() > 14) {
                            ti = dataSnapshot.child("Todos").child(h+"").child("Titulo").getValue(String.class);
                            int t = 0;
                            txRecem.get(xp).setText("");
                            while (t < 13) {
                                txRecem.get(xp).setText(txRecem.get(xp).getText()+((ti.charAt(t)+"")));
                                t++;
                            }
                            txRecem.get(xp).setText(txRecem.get(xp).getText()+"..");

                        } else {
                            txRecem.get(xp).setText(dataSnapshot.child("Todos").child(h+"").child("Titulo").getValue(String.class));
                        }

                        if(dataSnapshot.child("Todos").child(h+"").child("Autor").getValue(String.class).length() > 14) {
                            au = dataSnapshot.child("Todos").child(h+"").child("Autor").getValue(String.class);
                            int t = 0;
                            txRecem2.get(xp).setText("");
                            while (t < 14) {
                                txRecem2.get(xp).setText(txRecem2.get(xp).getText()+((au.charAt(t)+"")));
                                t++;
                            }
                            txRecem2.get(xp).setText(txRecem2.get(xp).getText()+"..");

                        } else {
                            txRecem2.get(xp).setText(dataSnapshot.child("Todos").child(h+"").child("Autor").getValue(String.class));
                        }

                        btRecem.get(xp).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent go = new Intent("ACAO_PRODUCT");
                                go.putExtra(EXTRA_MESSAGE, p+"");
                                startActivity(go);
                            }
                        });
                        final int im = xp;
                        StorageReference refImg = FirebaseStorage.getInstance().getReference(h+".jpg");
                        refImg.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(getApplicationContext()).load(uri).into(btRecem.get(im));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        xp++;
                        h--;

                        regRec--;
                    }
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
