package com.events.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.CategoryActivity;
import com.events.EventDetailActivity;
import com.events.R;
import com.events.custom.CustomFragment;
import com.events.model.Category;
import com.events.model.Data;
import com.events.services.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cruze on 12/10/2016.
 */

public class CategoryDetail extends CustomFragment {
    ArrayList<Category> categoryArrayList = new ArrayList<>();
//    Button btnGetData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_detail, null);

//        btnGetData = (Button) v.findViewById(R.id.buttonInsert);
//        btnGetData.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                getLandingPage();
//            }
//        });

//        setCategoryList(v);

        return v;
    }

    private void setCategoryList(View v) {
//        getLandingPage();

        ListView list = (ListView) v.findViewById(R.id.list);
        list.setAdapter(new CategoryAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                startActivity(new Intent(getActivity(),
                        CategoryActivity.class));
            }
        });
    }

    private void getLandingPage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.95:9876")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService service = retrofit.create(APIService.class);
        Call<List<Category>> call = service.getLandingPage();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();

                for(int i = 0; i< categories.size(); i++) {
                    Integer id = categories.get(i).getId();
                    String categoryName = categories.get(i).getCategoryName();
                    String image = categories.get(i).getImage();

                    categoryArrayList.add(new Category(id, categoryName, image));
                }
//                Context context = getActivity().getApplicationContext();
//                CharSequence text = details;
//                int duration = Toast.LENGTH_LONG;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private class CategoryAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return categoryArrayList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Category getItem(int position) {
            return categoryArrayList.get(position);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater(null).inflate(
                        R.layout.category_detail, null);

            Category d = getItem(position);
            TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
            lbl.setText(d.getCategoryName());

            lbl = (TextView) convertView.findViewById(R.id.lbl2);
            lbl.setText(d.getCategoryName()); //title 2

            lbl = (TextView) convertView.findViewById(R.id.lbl3);
            lbl.setText(d.getCategoryName()); //description

            ImageView img = (ImageView) convertView.findViewById(R.id.img);
            img.setImageResource(d.getId()); //image

            return convertView;
        }

    }
}
