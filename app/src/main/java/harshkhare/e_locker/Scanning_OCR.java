package harshkhare.e_locker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Scanning_OCR extends AppCompatActivity {
    private static final String LOG_TAG = "Text API";
    private static final int PHOTO_REQUEST = 10;
    public static final String TAG = "Scanning_OCR";

    private Uri imageUri;
    private TextRecognizer detector;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private ImageView ivScanDoc;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private Uri downloadUrl;
    private FirebaseDatabase db;
    private FirebaseDatabase scannedtextref;
    private DatabaseReference scantextresult;
    private EditText etscanResults;
    private ProgressBar pbStatus;
    private TextView tvUploadStatus;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private String uid;
    private String username;
    private DatabaseReference user_scanDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning__ocr);
        Button btnUpload = (Button) findViewById(R.id.btnUpload);
        Button btnScan = (Button) findViewById(R.id.btnScan);
        ivScanDoc = (ImageView) findViewById(R.id.ivScanDoc);
        etscanResults = (EditText) findViewById(R.id.etscanResults);
        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance();
        FirebaseStorage fbstorage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imagesRef = storageRef.child("images");
        scantextresult = db.getReference();

        pbStatus = (ProgressBar) findViewById(R.id.pbStatus);
        pbStatus.setVisibility(View.GONE);

        tvUploadStatus = (TextView) findViewById(R.id.tvUploadStatus);
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            etscanResults.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleScanInterface();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase(v);
            }
        });
        databaseReference = db.getReference("docs_db");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        user_scanDB = databaseReference.child(uid);

        username = currentUser.getDisplayName();

    }

    private void handleScanInterface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Scanning_OCR.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION);
            } else {
                takePicture();
            }
        } else {
            takePicture();
        }
    }

    private void uploadToFirebase(final View v) {
        v.setEnabled(false);
        pbStatus.setVisibility(View.VISIBLE);
        ivScanDoc.setDrawingCacheEnabled(true);
        ivScanDoc.buildDrawingCache();
        Bitmap bitmap = ivScanDoc.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();
        // Date currentDate = new Date(System.currentTimeMillis());
        //String date=currentDate.toString();

        String filename = "docs_" + System.currentTimeMillis();
        StorageReference docs = FirebaseStorage.getInstance().getReference(filename);

        UploadTask uploadTask = docs.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Scanning_OCR.this, "Failed to upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Toast.makeText(Scanning_OCR.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                String text = etscanResults.getText().toString();
                if (text.isEmpty()) {
                    text = "No extra info";
                }

                pbStatus.setVisibility(View.GONE);
                new MyUploadTask().execute(downloadUrl.toString(), text, uid);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                long totalByteCount = taskSnapshot.getTotalByteCount();
                @SuppressWarnings("VisibleForTests")
                long transferred = taskSnapshot.getBytesTransferred();
                tvUploadStatus.setText(transferred + "/" + totalByteCount);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(Scanning_OCR.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    handleScanInterface();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    String words = "";
                    ivScanDoc.setImageBitmap(bitmap);
                    for (int index = 0; index < textBlocks.size(); index++) {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            //extract scanned text lines here
                            lines = lines + line.getValue() + "\n";
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                words = words + element.getValue() + ", ";
                            }
                        }
                    }

                    if (textBlocks.size() == 0) {
                        etscanResults.setText("");
                    } else {
                        etscanResults.setText(etscanResults.getText() + words + "\n");
                    }
                } else {
                    etscanResults.setText("");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, etscanResults.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    class MyUploadTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            String infoText = params[1];
            String uid=params[2];
            HashMap<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("userid", uid);
            data.put("uploaded_on", System.currentTimeMillis());
            data.put("desc", infoText);
            user_scanDB.push().setValue(data);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(Scanning_OCR.this, "uploaded", Toast.LENGTH_SHORT).show();
            //show success
        }
    }
}