package harshkhare.e_locker;

import android.provider.DocumentsContract;

import com.google.firebase.database.DataSnapshot;

import static android.R.attr.data;

/**
 * Created by Hi ! HARSH on 22-Apr-17.
 */

class ScanModel {
    String description;
    String url;
    String userid;
    String Document;
    String document;
    private String key;
    private Long uploaded_on;

    public ScanModel(DataSnapshot dataSnapshot) {
        this.description = dataSnapshot.child("desc").getValue(String.class);
        this.url = dataSnapshot.child("url").getValue(String.class);
        this.userid = dataSnapshot.child("userid").getValue(String.class);
        this.Document=dataSnapshot.child("desc").getValue(String.class);
        key = dataSnapshot.getKey();
        uploaded_on = dataSnapshot.child("uploaded_on").getValue(Long.class);
        this.document=dataSnapshot.child("desc").getValue(String.class);

    }

    public String getDescription() {
        return description;
    }

    public String getUserid() {
        return userid;
    }

    public String getKey() {
        return key;
    }

    public Long getUploaded_on() {
        return uploaded_on;
    }

    public String getUrl() {

        return url;
    }


    ScanModel() {
    }
}
