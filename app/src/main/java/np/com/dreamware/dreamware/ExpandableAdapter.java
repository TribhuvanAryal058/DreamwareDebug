package np.com.dreamware.dreamware;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> GroupList;
    private HashMap<String, List<Stock>> itemHashMap;

    public ExpandableAdapter(Context context, List<String> groupList, HashMap<String, List<Stock>> itemHashMap) {
        this.context = context;
        this.GroupList = groupList;
        this.itemHashMap = itemHashMap;
    }

    @Override

    public int getGroupCount() {
        return GroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemHashMap.get(GroupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return GroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemHashMap.get(GroupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_layout, parent, false);
        }

        String GroupTitle = String.valueOf(getGroup(groupPosition));
        TextView groupTextView = convertView.findViewById(R.id.GroupTV);
        groupTextView.setText(GroupTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        }

        Stock stock = (Stock) getChild(groupPosition, childPosition);

        String ChildTitle = stock.getName();
        TextView childTextView = convertView.findViewById(R.id.ItemTV);
        childTextView.setText(ChildTitle);

        TextView stockTextView = convertView.findViewById(R.id.QuantityTV);
        if (stock.isStockSet()) {
            stockTextView.setText(String.valueOf(stock.getStock()));
        } else {
            stockTextView.setText("-");
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
