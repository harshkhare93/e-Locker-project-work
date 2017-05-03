package harshkhare.e_locker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Hi ! HARSH on 22-Apr-17.
 */

public class DocumentHolder extends RecyclerView.ViewHolder {
    public ImageView ivDoc;
    TextView tvdesc;
    TextView tvDate;


    public DocumentHolder(View itemView) {
        super(itemView);
        tvdesc= (TextView) itemView.findViewById(R.id.tvdesc);
        ivDoc = (ImageView) itemView.findViewById(R.id.ivDoc);
        tvDate= (TextView) itemView.findViewById(R.id.tvDate);

    }
}
