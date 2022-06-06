package ro.ase.fhcrypt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Key> {
    private Context context;
    private int resource;
    private List<Key> keysList;
    private LayoutInflater layoutInflater;

    public CustomAdapter(@NonNull Context context, List<Key> list, LayoutInflater layoutInflater) {
        super(context, R.layout.row, list);
        this.context = context;
        this.keysList= list;
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.row, parent, false);
        Key currentKey= keysList.get(position);

        if(currentKey!=null)
        {
            TextView tv1 =(TextView) view.findViewById(R.id.algoName);
            tv1.setText("Algorithm name: "+ currentKey.getKeyType());

            TextView tv2 = (TextView)view.findViewById(R.id.keySize);
            tv2.setText("Key size: "+ currentKey.getKeySize());

            TextView tv3 = (TextView)view.findViewById(R.id.keyValue);
            tv3.setText("Key value: "+ currentKey.getKeyValue());
        }

        return view;
    }
}
