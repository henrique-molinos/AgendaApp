package com.example.agendafirebaseteste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Instanciação dos objetos de layout
    EditText mEditId, mEditNome, mEditTelefone;
    Button mButtonSalvar, mButtonPesquisar, mButtonAtualizar, mButtonRemover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associação os objetos com os respectivos itens de layout
        mEditId = findViewById(R.id.editId);
        mEditNome = findViewById(R.id.editNome);
        mEditTelefone = findViewById(R.id.editTelefone);

        mButtonSalvar = findViewById(R.id.buttonSalvar);
        mButtonPesquisar = findViewById(R.id.buttonPesquisar);
        mButtonAtualizar = findViewById(R.id.buttonAtualizar);
        mButtonRemover = findViewById(R.id.buttonRemover);

        //Criação dos listeners
        mButtonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarContato();
            }
        });

        mButtonPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisarDados();
            }
        });

        mButtonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarDados();
            }
        });

        mButtonRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removerDados();
            }
        });
    }

    public void salvarContato() {
        String idStr = mEditId.getText().toString();
        int id = Integer.parseInt(idStr);
        Agenda agenda = new Agenda(id, mEditNome.getText().toString(), mEditTelefone.getText().toString());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();
        reference.child("agenda").push().setValue(agenda);

        mEditId.setText("");
        mEditNome.setText("");
        mEditTelefone.setText("");
    }

    public void pesquisarDados() {
        mEditNome.setText("");
        mEditTelefone.setText("");

        //Adiciona-se o ChildEventListener para a referência do Realtime Database
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        int id = Integer.parseInt(mEditId.getText().toString());
        database.child("agenda").orderByChild("id").equalTo(id).addChildEventListener(childEventListener);
    }

    public void atualizarDados() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        int id = Integer.parseInt(mEditId.getText().toString());

        String nome = mEditNome.getText().toString();
        String tel = mEditTelefone.getText().toString();

        database.child("agenda").orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Verifica-se todas as referências existentes através do método getChildren()
                for (DataSnapshot data : snapshot.getChildren()) {
                    //Objeto com a estrutua key-value (String = chave) (Object = valor)
                    Map<String, Object> valoresAtualizados = new HashMap<>();

                    //Informa-se qual atributo deseja-se atualizar e o valor atualizado
                    valoresAtualizados.put("telefone", tel);
                    valoresAtualizados.put("nome", nome);
                    data.getRef().updateChildren(valoresAtualizados);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removerDados() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        int id = Integer.parseInt(mEditId.getText().toString());

        database.child("agenda").orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Percorre-se todas as referências existentes através do método getChildren()
                for (DataSnapshot data : snapshot.getChildren()) {
                    //Remove-se o produto "1" do Realtime Database
                    data.getRef().removeValue();

                    mEditId.setText("");
                    mEditNome.setText("");
                    mEditTelefone.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Agenda agenda = snapshot.getValue(Agenda.class);
            Log.i("FIREBASE_CONSULTA", snapshot.getValue().toString());
            mEditNome.setText(agenda.getNome());
            mEditTelefone.setText(agenda.getTelefone());
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}