package com.events;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.events.custom.CustomActivity;

import com.events.model.Category;
import com.events.services.APIService;
import com.events.sync.ServerCommunicator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends CustomActivity {
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private static final String BASE_URL = "http://10.0.1.61:9876";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //addFragment();
        getCategoryData();
        loadCategoryView();
        registerClickCallback();
    }

    private void getCategoryData() {
        APIService service = ServerCommunicator.getServerCommunicator().create(APIService.class);
        Call<List<Category>> call = service.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Log.i("CATEGORIES", response.body().toString());
                        List<Category> categories = response.body();
                        Log.i("SIZE", ""+categories.size());

                        for(int i = 0; i< categories.size(); i++) {
                            int id = categories.get(i).getId();
                            String categoryName = categories.get(i).getCategoryName();
                            String[] item = categories.get(i).getImage().split("/");
                            String image = item[item.length-1];
                            Log.i(categoryName, image);

                            categoryArrayList.add(new Category(id, categoryName, image));
                        }
                        Log.i("CATEGORIES", "Null");
                    } else {
                        Log.i("CATEGORIES", "Failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Context context = getApplicationContext();
                CharSequence text = getString(R.string.network_error);
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private void loadCategoryView() {
        ArrayAdapter<Category> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.categoryActivityView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Category> {
        private MyListAdapter() {
            super(CategoryActivity.this, R.layout.category_item, categoryArrayList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.category_item, parent, false);
            }

            //Find the category to work with
            Category currentCategory = categoryArrayList.get(position);

            //Fill the image view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.categoryImageView);
//            imageView.setImageResource(currentCategory.getId());

            //Get category image from server
            Picasso.with(getApplicationContext())
                    .load(BASE_URL + "/api/landing/image/" + currentCategory.getImage())
                    .placeholder(R.drawable.img1)
                    .error(R.drawable.img2)
                    .resize(621, 290)
                    .into(imageView);

            //Fill the text view
            TextView textView = (TextView) itemView.findViewById(R.id.categoryNameView);
            textView.setText(currentCategory.getCategoryName());

            return itemView;
        }
    }
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.categoryActivityView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id){
                Category clickedCategory = categoryArrayList.get(position);
                CharSequence message = "You clicked " + clickedCategory.getCategoryName();

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
