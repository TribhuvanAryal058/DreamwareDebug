package np.com.dreamware.dreamware;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemDisplayActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mItemsReference, mStocksReference;
    private Query mItemsReferenceQuery;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mStocksEventListener;

    private RecyclerView mListView;
    private TransactionsAdapter mTransactionsAdapter;
    private ArrayList<TransactionDetails> transactionDetailsList;
    private Button loadButton;
    private TextView StockTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference();
        Intent intent = getIntent();
        String groupName = intent.getStringExtra("Group Name");
        String itemName = intent.getStringExtra("Item Name");

        mItemsReference = databaseReference.child("T").child(groupName).child(itemName);
        mStocksReference = databaseReference.child("S").child(groupName).child(itemName);

        mListView = findViewById(R.id.ItemsListView);
        loadButton = findViewById(R.id.LoadB);

        transactionDetailsList = new ArrayList<>();
        mTransactionsAdapter = new TransactionsAdapter(transactionDetailsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        mListView.setLayoutManager(mLayoutManager);

        mListView.setItemAnimator(new DefaultItemAnimator());

        mListView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        mListView.setAdapter(mTransactionsAdapter);

        setTitle(groupName + " : " + itemName);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachDatabaseReadListener();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        transactionDetailsList.clear();
        mTransactionsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.UpdateItem:
                try {
                    TransactionDetails details = transactionDetailsList.get(transactionDetailsList.size() - 1);
                    UpdateDetail(details);
                } catch (ArrayIndexOutOfBoundsException exception) {
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.exists()) {
                        TransactionDetails details = dataSnapshot.getValue(TransactionDetails.class);
                        transactionDetailsList.add(details);
                        mTransactionsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ItemDisplayActivity.this, "No Transactions to display!",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    TransactionDetails details = dataSnapshot.getValue(TransactionDetails.class);
                    int index = 0;
                    for (TransactionDetails transactionDetails : transactionDetailsList) {
                        if (transactionDetails.getId().equals(details.getId())) {
                            index = transactionDetailsList.indexOf(transactionDetails);
                            transactionDetailsList.set(index, details);
                        }
                    }
                    mTransactionsAdapter.notifyItemChanged(index);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    TransactionDetails details = dataSnapshot.getValue(TransactionDetails.class);
                    Log.i("Removed:", details.toString());
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ItemDisplayActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            mItemsReferenceQuery = mItemsReference.orderByKey().limitToLast(100);
            mItemsReferenceQuery.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mItemsReferenceQuery.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    private void UpdateDetail(final TransactionDetails details) {
        if (details != null) {
            ArrayList<String> TList = new ArrayList<>();
            TList.add("Production");
            TList.add("Sales");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);

            View dialogView = LayoutInflater.from(this).inflate(R.layout.input_dialog_layout, null);

            final EditText quantityInput = dialogView.findViewById(R.id.QuantityInput);
            final EditText numberInput = dialogView.findViewById(R.id.NumberET);
            final Spinner transactionSpinner = dialogView.findViewById(R.id.TransactionSpinner);
            TextView NTTextView = dialogView.findViewById(R.id.PTypeTv);
            TextView titleTextView = dialogView.findViewById(R.id.MessageTextView);

            setUpTransactionSpinner(transactionSpinner, TList, NTTextView);

            titleTextView.setText(R.string.update_transaction_string);
            titleTextView.setTypeface(Typeface.DEFAULT_BOLD);

            quantityInput.setText(String.valueOf(details.getQuantity()));

            String[] number = details.getNumber().split("-");
            final String transactionType = number[0].equals("P") ? "Production" : "Sales";

            transactionSpinner.setSelection(TList.indexOf(transactionType));

            numberInput.setText(number[1]);

            builder.setView(dialogView);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String quantityI = quantityInput.getText().toString();
                    Long quantity = Long.parseLong(quantityI);
                    String id = details.getId();
                    Long OQuantity = details.getQuantity();

                    String number = numberInput.getText().toString();
                    String type = transactionSpinner.getSelectedItem().toString();

                    String code = type.equals("Production") ? "P-" : "S-";
                    String BNumber = code + number;
                    details.setNumber(BNumber);
                    details.setQuantity(quantity);
                    mItemsReference.child(id).setValue(details);

                    quantity = type.equals("Production") ? quantity : -quantity;
                    String currentStock = StockTV.getText().toString();
                    Long cStock = Long.parseLong(currentStock);
                    OQuantity = transactionType.equals("Production") ? OQuantity : -OQuantity;
                    Long nStock = cStock - OQuantity + quantity;
                    mStocksReference.setValue(nStock);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void setUpTransactionSpinner(Spinner TSpinner, ArrayList<String> TList, final TextView textView) {

        ArrayAdapter<String> TAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TList);
        TAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        TSpinner.setAdapter(TAdapter);

        TSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String TransactionType = parent.getItemAtPosition(position).toString();
                String text = TransactionType.equals("Production") ? "Update P-Slip Number" : "Update Gate Pass";
                textView.setText(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}
