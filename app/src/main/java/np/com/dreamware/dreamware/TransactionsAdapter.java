package np.com.dreamware.dreamware;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionDetailsViewHolder> {

    private ArrayList<TransactionDetails> detailsList;

    public TransactionsAdapter(ArrayList<TransactionDetails> detailsList) {
        this.detailsList = detailsList;
    }

    @NonNull
    @Override
    public TransactionDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.individual_item_layout, viewGroup, false);
        return new TransactionDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionDetailsViewHolder TransactionDetailsViewHolder, int i) {

        TransactionDetails details = detailsList.get(i);
        TransactionDetailsViewHolder.quantityTextView.setText(String.valueOf(details.getQuantity()));
        TransactionDetailsViewHolder.dateTimeTextView.setText(formatDate(details.getTime()));

        String[] number = details.getNumber().split("-");
        String purchaseType = number[0].equals("P") ? "P-slip No." : "Gate Pass No.";

        TransactionDetailsViewHolder.purchaseTypeTextView.setText(purchaseType);
        TransactionDetailsViewHolder.numberTextView.setText(number[1]);
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    private String formatDate(Long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat dateFormatter = DateFormat.getDateTimeInstance();
        return dateFormatter.format(date);
    }

    public class TransactionDetailsViewHolder extends RecyclerView.ViewHolder {

        public TextView dateTimeTextView, quantityTextView, numberTextView, purchaseTypeTextView;

        public TransactionDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeTextView = itemView.findViewById(R.id.DateTimeTextView);
            quantityTextView = itemView.findViewById(R.id.QuantityTextView);
            numberTextView = itemView.findViewById(R.id.NumberTV);
            purchaseTypeTextView = itemView.findViewById(R.id.PurchaseType);
        }

    }
}
