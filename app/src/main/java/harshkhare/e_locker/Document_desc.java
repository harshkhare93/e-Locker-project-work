package harshkhare.e_locker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static harshkhare.e_locker.R.id.rvDescription;

public class Document_desc extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private TextView document;
    private ImageButton ivbtndown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_desc);
        document = (TextView) findViewById(R.id.Document);
        ivbtndown = (ImageButton) findViewById(R.id.ivbtndownload);
        ivbtndown.setOnClickListener(this);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("desc");
        /*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    document.add(new ScanModel(snapshot));
                }
                Toast.makeText(Document_desc.this, "Data loaded Successfully", Toast.LENGTH_SHORT).show();

                DocumentAdapter adapter=new DocumentAdapter(document);
                rvDescription.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {

    }


}
