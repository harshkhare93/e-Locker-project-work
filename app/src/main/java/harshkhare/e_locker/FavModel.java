package harshkhare.e_locker;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Hi ! HARSH on 23-Apr-17.
 */

class FavModel extends ScanModel {

    private final String title;
    private final String link;
    private final String image;

    public FavModel(DataSnapshot snapshot) {

        title = snapshot.child("title").getValue(String.class);
        link = snapshot.child("link").getValue(String.class);
        image = snapshot.child("image").getValue(String.class);
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    }

