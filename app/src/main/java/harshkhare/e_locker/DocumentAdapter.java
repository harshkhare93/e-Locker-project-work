package harshkhare.e_locker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Hi ! HARSH on 22-Apr-17.
 */

public class DocumentAdapter extends RecyclerView.Adapter<DocumentHolder> {
    ArrayList<Object> documentList;



    public DocumentAdapter(ArrayList<Object> documentList) {
        this.documentList = documentList;
    }

    @Override
    public DocumentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = ((LayoutInflater)parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.user_document_detail,parent,false);

       return new DocumentHolder(row);
    }

    @Override
    public void onBindViewHolder(DocumentHolder holder, int position) {
        //databinding

        ScanModel model= (ScanModel) documentList.get(position);
        holder.tvdesc.setText(model.description);
        holder.tvDate.setText(String.valueOf(model.getUploaded_on()));

        Picasso.with(holder.ivDoc.getContext())
                .load(model.getUrl())
                .resize(600, 200)
                .onlyScaleDown()
                .into(holder.ivDoc);




        holder.ivDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 23-Apr-17  give options
            }
        });

    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }
}
