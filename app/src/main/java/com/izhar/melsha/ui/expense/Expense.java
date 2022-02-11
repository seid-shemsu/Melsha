package com.izhar.melsha.ui.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.izhar.melsha.R;
import com.izhar.melsha.models.ExpenseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Expense#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expense extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Expense() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Expense.
     */
    // TODO: Rename and change types and number of parameters
    public static Expense newInstance(String param1, String param2) {
        Expense fragment = new Expense();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View root;
    LinearLayout linear;
    FloatingActionButton fab;
    Button submit;
    List<ExpenseModel> expenses = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_expense, container, false);
        linear = root.findViewById(R.id.linear);
        fab = root.findViewById(R.id.fab);
        submit = root.findViewById(R.id.submit);
        fab.setOnClickListener(v -> {
            View item = getLayoutInflater().inflate(R.layout.single_take_loan, null, false);
            EditText reason, amount;
            ImageView cancel = item.findViewById(R.id.cancel);
            reason = item.findViewById(R.id.code);
            amount = item.findViewById(R.id.amount);
            linear.addView(item);
            cancel.setOnClickListener(v2 -> {
                linear.removeView(item);
            });
        });
        submit.setOnClickListener(v -> {
            boolean valid = true;
            for (int i = 0; i < linear.getChildCount(); i++) {
                View view = linear.getChildAt(i);
                EditText reason, amount;
                reason = view.findViewById(R.id.code);
                amount = view.findViewById(R.id.amount);
                String r = reason.getText().toString();
                String a = amount.getText().toString();
                if (r.isEmpty() || a.isEmpty())
                    valid = false;
                else
                    expenses.add(new ExpenseModel(r, Integer.parseInt(a)));
            }
            if (expenses.size() == 0)
                Toast.makeText(getContext(), "please add expense", Toast.LENGTH_SHORT).show();
            else if (!valid)
                Toast.makeText(getContext(), "there is empty value", Toast.LENGTH_SHORT).show();
            else
                for (int i = 0; i < expenses.size(); i++) {
                    System.out.print("item " + i);
                    System.out.print(expenses.get(i).getReason());
                    System.out.println(expenses.get(i).getAmount());
                }

        });
        return root;
    }
}