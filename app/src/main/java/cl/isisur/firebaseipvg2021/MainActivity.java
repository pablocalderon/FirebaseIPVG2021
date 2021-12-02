package cl.isisur.firebaseipvg2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cl.isisur.firebaseipvg2021.Clases.Libro;

public class MainActivity extends AppCompatActivity {
             private List<Libro> ListLibro=new ArrayList<Libro>();
             ArrayAdapter<Libro> arrayAdapterLibro;

             EditText eTNombre,eTEditorial;
             Button bTBoton, btEliminar;
             ListView lvListadoLibros;

             FirebaseDatabase firebaseDatabase;
             DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eTNombre=findViewById(R.id.eTNombre);
        eTEditorial=findViewById(R.id.eTEditorial);
        bTBoton=findViewById(R.id.bTAgregar);
        btEliminar=findViewById(R.id.btEliminar);
        lvListadoLibros=findViewById(R.id.lvListadoLibros);

        inicilizarFireBase();

        listarDator();

        bTBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Libro libro =new Libro();
                libro.setIdAutor("123456");
                //libro.setIdAutor(UUID.randomUUID().toString());
                libro.setEstado(eTEditorial.getText().toString());
                libro.setNombre(eTNombre.getText().toString());
                databaseReference.child("Libro").child(libro.getIdAutor()).setValue(libro);
            }
        });
        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Libro").child("123456").setValue(null);
            }
        });



    }

    private void listarDator() {
      databaseReference.child("Libro").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              ListLibro.clear();
              for (DataSnapshot objS : snapshot.getChildren()){

                  Libro li =objS.getValue(Libro.class);
                  if (li.getEstado().equals("IPVG3")) {
                      ListLibro.add(li);
                      arrayAdapterLibro = new ArrayAdapter<Libro>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, ListLibro);
                      lvListadoLibros.setAdapter(arrayAdapterLibro);
                  }

              }



          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });






    }

    private void inicilizarFireBase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }
}