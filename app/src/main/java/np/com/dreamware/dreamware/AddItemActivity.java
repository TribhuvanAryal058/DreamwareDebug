package np.com.dreamware.dreamware;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private Spinner groupSpinner;
    private Spinner itemSpinner;
    private Spinner TSpinner;
    private EditText quantityEditText, NumberEditText;
    private Button addButton, clearButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private String groupName, itemName, TransactionType, hint;
    private ArrayList<String> GroupTitles;
    private ArrayList<String> TList;
    private HashMap<String, List<Stock>> itemsHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();

        GroupTitles = new ArrayList<>();
        GroupTitles = MainActivity.initGroup();

        TList = new ArrayList<>();
        TList.add("Production");
        TList.add("Sales");

        itemsHashMap = MainActivity.initializeData();

        groupName = GroupTitles.get(0);
        itemName = itemsHashMap.get(groupName).get(0).getName();
        TransactionType = TList.get(0);
        hint = "Enter Gate Pass";

        groupSpinner = findViewById(R.id.GroupSpinner);
        itemSpinner = findViewById(R.id.ItemSpinner);
        TSpinner = findViewById(R.id.TSpinner);

        quantityEditText = findViewById(R.id.QuantityInputET);
        addButton = findViewById(R.id.AddButton);
        clearButton = findViewById(R.id.ClearButton);
        NumberEditText = findViewById(R.id.NumberInputET);

        setUpGroupSpinner();
        setUpItemSpinner(GroupTitles.get(0));
        setUpTspinner();
        NumberEditText.setHint(hint);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearView();
            }
        });
    }

    private void setUpGroupSpinner() {

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, GroupTitles);

        groupAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setSelection(0);

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupName = parent.getItemAtPosition(position).toString();
                setUpItemSpinner(groupName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                groupName = parent.getItemAtPosition(0).toString();
            }
        });
    }

    private void setUpItemSpinner(String groupName) {

        ArrayList<Stock> itemList = (ArrayList<Stock>) itemsHashMap.get(groupName);

        ArrayList<String> stocksList = new ArrayList<>();
        for (Stock stock : itemList) {
            stocksList.add(stock.getName());
        }
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stocksList);

        itemAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        itemSpinner.setAdapter(itemAdapter);
        itemSpinner.setSelection(0);

        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemName = parent.getItemAtPosition(0).toString();
            }
        });
    }

    private void setUpTspinner() {

        ArrayAdapter<String> TAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TList);
        TAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        TSpinner.setAdapter(TAdapter);
        TSpinner.setSelection(0);

        TSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransactionType = parent.getItemAtPosition(position).toString();
                hint = TransactionType.equals("Production") ? "Enter P-Slip Number" : "Enter Gate Pass";
                NumberEditText.setHint(hint);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TransactionType = parent.getItemAtPosition(0).toString();
            }
        });

    }

    private void addData() {
        DatabaseReference mItemsReference = mDatabaseReference.child("T").child(groupName).child(itemName);

        DatabaseReference mStocksReference = mDatabaseReference.child("S").child(groupName).child(itemName);
        final String quantity = quantityEditText.getText().toString();
        String number = NumberEditText.getText().toString();
        if (quantity.equals("")) {
            quantityEditText.setError("Need to enter Quantity.");
            return;
        }
        if (number.equals("")) {
            NumberEditText.setError(hint);
            return;
        }
        long time = System.currentTimeMillis();
        Long qu = Long.parseLong(quantity);
        String key = mItemsReference.push().getKey();
        String transaction = TransactionType.equals("Production") ? "P-" : "S-";
        final String BNumber = transaction + number;

        quantityEditText.setText("");
        NumberEditText.setText("");
        TransactionDetails details = new TransactionDetails(time, qu, key, BNumber);
        mItemsReference.child(key).setValue(details, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(AddItemActivity.this,
                        "Quantity: " + quantity + " Number" + BNumber + "\ntransaction at: " + groupName + ":" + itemName,
                        Toast.LENGTH_SHORT).show();

            }
        });

        qu = TransactionType.equals("Production") ? qu : -qu;
        final Long finalQu = qu;
        mStocksReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long stocks = dataSnapshot.exists() ? dataSnapshot.getValue(Long.class) : 0;

                Long newStock = stocks + finalQu;

                dataSnapshot.getRef().setValue(newStock);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.i("ref", databaseError.toString());
            }
        });
    }

    private void clearView() {
        groupSpinner.setSelection(0, true);
        itemSpinner.setSelection(0, true);
        TSpinner.setSelection(0, true);
        quantityEditText.setText("");
        NumberEditText.setText("");
    }
}
