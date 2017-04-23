package harshkhare.e_locker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_favorites);
        final List<FavModel> links = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        RecyclerView rvFavs = (RecyclerView) findViewById(R.id.rvFavs);
        rvFavs.setLayoutManager(new LinearLayoutManager(this));
        final FavAdapter adapter = new FavAdapter(links);
        rvFavs.setAdapter(adapter);
        final DatabaseReference fav = FirebaseDatabase.getInstance().getReference("favorites").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        fav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                links.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        links.add(new FavModel(snapshot));
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(FavoritesActivity.this, "no data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    Toast.makeText(FavoritesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder> {

        private final List<FavModel> links;

        FavAdapter(List<FavModel> links) {
            this.links = links;
        }

        @Override
        public FavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = ((LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.user_document_detail, parent, false);
            return new FavHolder(row);
        }

        @Override
        public void onBindViewHolder(FavHolder holder, int position) {
            final FavModel model = links.get(position);
            Picasso.with(holder.ivImages.getContext()).load(model.getLink()).into(holder.ivImages);
            holder.tvTitle.setText(model.getTitle());
            holder.ivImages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBrowser(v,model.getLink());
                }
            });
        }

        private void openBrowser(View v, String link) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            v.getContext().startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return links.size();
        }

        class FavHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            ImageView ivImages;

            public FavHolder(View itemView) {
                super(itemView);
                tvTitle= (TextView) itemView.findViewById(R.id.tvTitle);
                ivImages= (ImageView) itemView.findViewById(R.id.ivImages);
            }
        }
    }

    }
