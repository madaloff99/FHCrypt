package ro.ase.fhcrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class StatisticsFragment extends Fragment {
    TextView tvEncrypted, tvDecrypted;
    PieChart pieChart;
    private AppDatabase appDatabase;
    private String userPassword;
    private String username;
    private int encNo;
    private int decNo;
    private int totalNo;
    public StatisticsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        tvEncrypted = view.findViewById(R.id.tvEncrypted);
        tvDecrypted = view.findViewById(R.id.tvDecrypted);
        pieChart = view.findViewById(R.id.piechart);
        appDatabase = AppDatabase.getAppDatabase(getActivity());
        final UserDao userDao = appDatabase.userDao();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        userPassword = sharedPreferences.getString("LoggedUserPassword", "");
        username = sharedPreferences.getString("LoggedUserName", "");

        int userID = userDao.getUserID(username,userPassword);
        encNo = userDao.getEncNo(userID);
        decNo = userDao.getDecNo(userID);
        totalNo = encNo + decNo;
        float percentageEnc = (encNo*100)/totalNo;
        float percentageDec = (decNo*100)/totalNo;

        setData(percentageEnc, percentageDec);
        return view;
    }

    private void setData(float pEnc, float pDec)
    {

        tvEncrypted.setText(Float.toString(pEnc)+"%");
        tvDecrypted.setText(Float.toString(pDec)+"%");

        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Float.parseFloat(String.valueOf(pEnc)),
                        Color.parseColor("#8340FF")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Float.parseFloat(String.valueOf(pDec)),
                        Color.parseColor("#FF03DAC5")));

        pieChart.startAnimation();
    }
}