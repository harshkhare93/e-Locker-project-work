package harshkhare.e_locker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static harshkhare.e_locker.R.id.rvDescription;

public class Document_desc extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase db;
    private DatabaseReference ref;

    private Toolbar tbThis;
    private ImageView imgThis;
    private TextView document;
    private ImageButton ivbtndown;

    private String url;
    private String description;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_desc);

        Intent in = getIntent();
        url = in.getStringExtra("EXTRA_URL");
        description = in.getStringExtra("EXTRA_DESCRIPTION");
        key = in.getStringExtra("EXTRA_KEY");

        this.tbThis = (Toolbar) findViewById(R.id.toolbar);
        this.imgThis = (ImageView) findViewById(R.id.img_this);
        this.document = (TextView) findViewById(R.id.Document);
        this.ivbtndown = (ImageButton) findViewById(R.id.ivbtndownload);

        this.tbThis.setTitle("Document Description");
        Glide.with(this)
                .load(url)
                .into(this.imgThis);

        this.document.setText(description);
        ivbtndown.setOnClickListener(this);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference("desc");


        Log.d("file-url", url);
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
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .progress(true,0)
                .content("Downloading...")
                .canceledOnTouchOutside(false)
                .build();
        dialog.show();

//        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);//storageRef.child();
        Log.d("path",getFilesDir().getAbsolutePath());
        Log.d("Name",islandRef.getName());
        try {
//            File localFile = File.createTempFile("images", "jpg");
            File localFile=new File("/sdcard/Download/",islandRef.getName()+".jpg");
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    dialog.hide();
                    Snackbar.make(findViewById(R.id.col_root),"File saved Successfully",Snackbar.LENGTH_LONG).show();
                    // Local temp file has been created
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    dialog.hide();
                    Snackbar.make(findViewById(R.id.col_root),"Failed to download file",Snackbar.LENGTH_LONG).show();
                    // Handle any errors
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
