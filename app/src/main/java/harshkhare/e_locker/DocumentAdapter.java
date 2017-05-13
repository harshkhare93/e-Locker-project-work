package harshkhare.e_locker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    ArrayList<ScanModel> documentList;
    ArrayList<ScanModel>document;

    public DocumentAdapter(ArrayList<ScanModel> documentList) {
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

        final ScanModel model = (ScanModel) documentList.get(position);
        holder.tvdesc.setText(model.description);
        holder.tvDate.setText(String.valueOf(model.getUploaded_on()));
        holder.pbsubstatus.setVisibility(View.VISIBLE);

        Glide.with(holder.ivDoc.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.bg2)
                .into(holder.ivDoc);

        holder.ivDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, Document_desc.class);
                intent.putExtra("EXTRA_URL",model.url);
                intent.putExtra("EXTRA_KEY",model.getKey());
                context.startActivity(intent);
            }
        });
        holder.pbsubstatus.setVisibility(View.INVISIBLE);
    }
    @Override
    public int getItemCount() {
        return documentList.size();
    }
}