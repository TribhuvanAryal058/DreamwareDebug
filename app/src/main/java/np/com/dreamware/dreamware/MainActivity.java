package np.com.dreamware.dreamware;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final int REQUEST_INVITE = 2;
    private static final String TAG = "Main Activity";
    private static final String apkLink = "apkLink";
    private FloatingActionButton floatingActionButton;
    private ExpandableListView expandableListView;
    private ExpandableAdapter expandableAdapter;
    private List<String> GroupTitles;
    private HashMap<String, List<Stock>> itemHashMap;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private RelativeLayout MainLayout;
    private DatabaseReference mStocksreference;
    private ValueEventListener mStocksListener;
    private DatabaseReference UtilsReference;

    public static HashMap<String, List<Stock>> initializeData() {

        HashMap<String, List<Stock>> mHashMap = new HashMap<>();

        ArrayList<Stock> cuteItems = new ArrayList<>();
        cuteItems.add(new Stock("Pillar Cock"));
        cuteItems.add(new Stock("Bib Cock"));
        cuteItems.add(new Stock("Angle Cock"));
        cuteItems.add(new Stock("Long Body"));
        cuteItems.add(new Stock("Concealed(M)"));
        cuteItems.add(new Stock("Sink Cock"));
        cuteItems.add(new Stock("Swan Neck"));
        cuteItems.add(new Stock("Bib Cock Nose"));
        mHashMap.put("Cute", cuteItems);

        ArrayList<Stock> sparrowItems = new ArrayList<>();
        sparrowItems.add(new Stock("Pillar Cock"));
        sparrowItems.add(new Stock("Bib Cock"));
        sparrowItems.add(new Stock("Angle Cock"));
        sparrowItems.add(new Stock("Long Body"));
        sparrowItems.add(new Stock("Concealed(M)"));
        sparrowItems.add(new Stock("Sink Cock"));
        sparrowItems.add(new Stock("Swan Neck"));
        mHashMap.put("Sparrow", sparrowItems);

        ArrayList<Stock> princeItems = new ArrayList<>();
        princeItems.add(new Stock("Pillar Cock"));
        princeItems.add(new Stock("Bib Cock"));
        princeItems.add(new Stock("Angle Cock"));
        princeItems.add(new Stock("Long Body"));
        princeItems.add(new Stock("Concealed(M)"));
        princeItems.add(new Stock("Concealed(H)"));
        princeItems.add(new Stock("Sink Cock"));
        princeItems.add(new Stock("Swan Neck"));
        princeItems.add(new Stock("2 Way Angle Cock"));
        princeItems.add(new Stock("Central Hole Basin Mixer"));
        princeItems.add(new Stock("Sink Mixer"));
        princeItems.add(new Stock("Wall Mixer L Blend"));
        mHashMap.put("Prince", princeItems);

        ArrayList<Stock> kingItems = new ArrayList<>();
        kingItems.add(new Stock("Pillar Cock"));
        kingItems.add(new Stock("Bib Cock"));
        kingItems.add(new Stock("Angle Cock"));
        kingItems.add(new Stock("Long Body"));
        kingItems.add(new Stock("Concealed(M)"));
        kingItems.add(new Stock("Concealed(H)"));
        kingItems.add(new Stock("Sink Cock"));
        kingItems.add(new Stock("Swan Neck"));
        kingItems.add(new Stock("Central Hole Basin Mixer"));
        kingItems.add(new Stock("Sink Mixer"));
        kingItems.add(new Stock("Wall Mixer L Blend"));
        mHashMap.put("King", kingItems);

        ArrayList<Stock> dreamItems = new ArrayList<>();
        dreamItems.add(new Stock("Pillar Cock(Foam Flow)"));
        dreamItems.add(new Stock("Bib Cock (Foam Flow)"));
        dreamItems.add(new Stock("Bib Cock (Foam Flow)"));
        dreamItems.add(new Stock("Long Body"));
        dreamItems.add(new Stock("Concealed(H)"));
        dreamItems.add(new Stock("Sink Cock"));
        dreamItems.add(new Stock("Swan Neck"));
        dreamItems.add(new Stock("2 Way Angle Cock"));
        dreamItems.add(new Stock("Central Hole Basin Mixer"));
        dreamItems.add(new Stock("Sink Mixer"));
        mHashMap.put("Dream", dreamItems);

        ArrayList<Stock> sikhaItems = new ArrayList<>();
        sikhaItems.add(new Stock("Pillar Cock(Foam Flow)"));
        sikhaItems.add(new Stock("Bib Cock (Foam Flow)"));
        sikhaItems.add(new Stock("Angle Cock"));
        sikhaItems.add(new Stock("Long Body"));
        sikhaItems.add(new Stock("Concealed(H)"));
        sikhaItems.add(new Stock("Sink Cock"));
        sikhaItems.add(new Stock("Swan Neck"));
        sikhaItems.add(new Stock("2 Way Angle Cock"));
        sikhaItems.add(new Stock("Central Hole Basin Mixer"));
        mHashMap.put("Sikha", sikhaItems);

        ArrayList<Stock> miracleItems = new ArrayList<>();
        miracleItems.add(new Stock("Pillar Cock(Foam Flow"));
        miracleItems.add(new Stock("Bib Cock (Foam Flow)"));
        miracleItems.add(new Stock("Angle Cock"));
        miracleItems.add(new Stock("Long Body"));
        miracleItems.add(new Stock("Concealed(H)"));
        miracleItems.add(new Stock("Sink Cock"));
        miracleItems.add(new Stock("Swan Neck"));
        miracleItems.add(new Stock("2 Way Angle Cock"));
        mHashMap.put("Miracle", miracleItems);

        ArrayList<Stock> mirageItems = new ArrayList<>();
        mirageItems.add(new Stock("Pillar Cock(Foam Flow)"));
        mirageItems.add(new Stock("Bib Cock (Foam Flow)"));
        mirageItems.add(new Stock("Angle Cock"));
        mirageItems.add(new Stock("Long Body"));
        mirageItems.add(new Stock("Concealed(H)"));
        mirageItems.add(new Stock("Sink Cock"));
        mirageItems.add(new Stock("Swan Neck"));
        mirageItems.add(new Stock("2 Way Angle Cock"));
        mirageItems.add(new Stock("2 Way Bib Cock"));
        mirageItems.add(new Stock("Central Hole Basin Mixer"));
        mHashMap.put("Mirage", mirageItems);

        ArrayList<Stock> doveItems = new ArrayList<>();
        doveItems.add(new Stock("Pillar Cock(Foam Flow)"));
        doveItems.add(new Stock("Bib Cock (Foam Flow)"));
        doveItems.add(new Stock("Angle Cock"));
        doveItems.add(new Stock("Long Body"));
        doveItems.add(new Stock("Concealed(H)"));
        doveItems.add(new Stock("Sink Cock"));
        doveItems.add(new Stock("Swan Neck"));
        doveItems.add(new Stock("2 Way Angle Cock"));
        doveItems.add(new Stock("Central Hole Basin Mixer"));
        doveItems.add(new Stock("Sink Mixer"));
        doveItems.add(new Stock("Wall Mixer L Blend"));
        mHashMap.put("Dove", doveItems);

        ArrayList<Stock> crestaItems = new ArrayList<>();
        crestaItems.add(new Stock("Pillar Cock(Foam Flow)"));
        crestaItems.add(new Stock("Bib Cock (Foam Flow)"));
        crestaItems.add(new Stock("Angle Cock"));
        crestaItems.add(new Stock("Long Body"));
        crestaItems.add(new Stock("Concealed(H)"));
        crestaItems.add(new Stock("Sink Cock"));
        crestaItems.add(new Stock("Swan Neck"));
        crestaItems.add(new Stock("2 Way Angle Cock"));
        crestaItems.add(new Stock("2 Way Bib Cock"));
        crestaItems.add(new Stock("Central Hole Basin Mixer"));
        crestaItems.add(new Stock("Sink Mixer"));
        crestaItems.add(new Stock("Wall Mixer L Blend"));
        mHashMap.put("Cresta", crestaItems);

        ArrayList<Stock> galaxyItems = new ArrayList<>();
        galaxyItems.add(new Stock("Pillar Cock(Foam Flow)"));
        galaxyItems.add(new Stock("Bib Cock (Foam Flow)"));
        galaxyItems.add(new Stock("Angle Cock"));
        galaxyItems.add(new Stock("Long Body"));
        galaxyItems.add(new Stock("Concealed(H)"));
        galaxyItems.add(new Stock("Sink Cock"));
        galaxyItems.add(new Stock("Swan Neck"));
        galaxyItems.add(new Stock("Central Hole Basin Mixer"));
        galaxyItems.add(new Stock("Sink Mixer"));
        galaxyItems.add(new Stock("Wall Mixer L Blend"));
        mHashMap.put("Galaxy", galaxyItems);

        return mHashMap;
    }

    public static ArrayList<String> initGroup() {
        ArrayList<String> mGroupTitles = new ArrayList<>();

        mGroupTitles.add("Cute");
        mGroupTitles.add("Sparrow");
        mGroupTitles.add("Prince");
        mGroupTitles.add("King");
        mGroupTitles.add("Dream");
        mGroupTitles.add("Sikha");
        mGroupTitles.add("Miracle");
        mGroupTitles.add("Mirage");
        mGroupTitles.add("Dove");
        mGroupTitles.add("Cresta");
        mGroupTitles.add("Galaxy");

        return mGroupTitles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrepareForAuthentication();

        mStocksreference = FirebaseDatabase.getInstance().getReference().child("S");
        UtilsReference = FirebaseDatabase.getInstance().getReference().child("U");

        itemHashMap = new HashMap<>();
        itemHashMap = initializeData();
        GroupTitles = new ArrayList<>();
        GroupTitles = initGroup();

        MainLayout = findViewById(R.id.MainLayout);
        expandableListView = findViewById(R.id.ExpandableListView);
        expandableAdapter = new ExpandableAdapter(this, GroupTitles, itemHashMap);
        expandableListView.setAdapter(expandableAdapter);
        setChildClickListener();

        mStocksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String groupName = dataSnapshot.getKey();
                List<Stock> stocklist = itemHashMap.get(groupName);

                if (dataSnapshot.exists()) {

                    for (DataSnapshot items : dataSnapshot.getChildren()) {
                        String itemName = items.getKey();
                        Long stockquantity = items.getValue(Long.class);
                        for (Stock stock : stocklist) {
                            if (stock.getName().equals(itemName)) {
                                stock.setStock(stockquantity);
                            }
                        }
                    }
                }
                for (Stock stock : stocklist) {
                    if (!stock.isStockSet()) {
                        stock.setStock(0);
                    }
                }
                itemHashMap.put(groupName, stocklist);
                expandableAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Data Error", databaseError.toString());
            }
        };

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                String groupName = (String) expandableAdapter.getGroup(groupPosition);

                mStocksreference.child(groupName).addValueEventListener(mStocksListener);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                String groupName = (String) expandableAdapter.getGroup(groupPosition);
                mStocksreference.child(groupName).removeEventListener(mStocksListener);
            }
        });


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        //every time the activity resumes check for authenticated state.
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //every time the activity pauses remove authListener.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        MainLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_CANCELED) finish();
                break;
        }
        /*if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
                Toast.makeText(MainActivity.this, "Faile to send invite:", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_menu:
                AuthUI.getInstance().signOut(this);
                break;
            case R.id.share_menu:
                shareAppLink();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareAppLink() {
        String mimeType = "text/plain";
        String title = "Share App Link via";
        String link = "https://dreamware-8a812.firebaseapp.com/dreamware.apk";
        ShareCompat.IntentBuilder.from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(link)
                .startChooser();

    }

    private void initializeSignedInState() {
        MainLayout.setVisibility(View.VISIBLE);
        checkNetworkState();
    }

    private void checkNetworkState() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

        if (networkInfo == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You are not connected to a Network!.");
            builder.setCancelable(false);
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            })
                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    /*private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                //.setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }*/

    private void PrepareForAuthentication() {
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user isn't Signed in. Use firebase Auth UI to sign in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Collections.singletonList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )).build(), RC_SIGN_IN
                    );
                } else {
                    initializeSignedInState();
                }
            }
        };
    }

    private void setChildClickListener() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(MainActivity.this, ItemDisplayActivity.class);

                String groupName = String.valueOf(expandableAdapter.getGroup(groupPosition));
                String itemName = ((Stock) (expandableAdapter.getChild(groupPosition, childPosition))).getName();

                intent.putExtra("Group Name", groupName);
                intent.putExtra("Item Name", itemName);

                startActivity(intent);

                return true;
            }
        });
    }
}
