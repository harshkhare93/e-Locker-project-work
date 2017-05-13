package harshkhare.e_locker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Hi ! HARSH on 22-Apr-17.
 */

public class DocumentHolder extends RecyclerView.ViewHolder {

    public ImageView ivDoc;
    TextView tvdesc;
    TextView tvDate;
    public  ProgressBar pbsubstatus;



    public DocumentHolder(View itemView) {
        super(itemView);
        tvdesc = (TextView) itemView.findViewById(R.id.tvdesc);
        ivDoc = (ImageView) itemView.findViewById(R.id.ivDoc);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        pbsubstatus=(ProgressBar) itemView.findViewById(R.id.pbsubstatus);

    }

}
