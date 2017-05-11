package harshkhare.e_locker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

import static android.widget.Toast.LENGTH_SHORT;
import static harshkhare.e_locker.R.id.imageView;
import static harshkhare.e_locker.R.id.ivDoc;
import static harshkhare.e_locker.R.id.ivScanDoc;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentHolder> {
    ArrayList<Object> documentList;

    public DocumentAdapter(ArrayList<Object> documentList) {
        this.documentList = documentList;
    }

    @Override
    public DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = ((LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.user_document_detail, parent, false);

        return new DocumentHolder(row);


    }

    @Override
    public void onBindViewHolder(final DocumentHolder holder, int position) {
        //databinding

        ScanModel model = (ScanModel) documentList.get(position);
        holder.tvdesc.setText(model.description);
        holder.tvDate.setText(String.valueOf(model.getUploaded_on()));


        Glide.with(holder.ivDoc.getContext())
                .load(model.getUrl())
                // .resize(400,300)
                // .onlyScaleDown()
                .placeholder(R.drawable.bg2)
                .into(holder.ivDoc);

        holder.ivDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Document_desc.class);
                context.startActivity(intent);
            }
        });
        //------------- Downloading ------------------
        //download on local file
        holder.ivdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Toast.makeText(context, "download", Toast.LENGTH_SHORT).show();


                File localFile = null;
                try {
                    localFile = File.createTempFile("images", "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                holder.strref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created

                        /* Load the image using Glide
                                 Glide.with(this)
                                .using(new FirebaseImageLoader())
                                .load()
                                .into(imageView);*/


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
}