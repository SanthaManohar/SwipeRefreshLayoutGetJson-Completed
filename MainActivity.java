package basicandroid.com.swiperefreshlayoutgetjson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    TextView name, email,title;
    ImageView profile;
//    public static final String url = "https://api.myjson.com/bins/wpvhy";
    public static final String url = "https://api.myjson.com/bins/15knku";

    public static final String TAG = MainActivity.class.getSimpleName();
    int item_count = 0;
    public static ArrayList<HashMap<String, String>> nameAry = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        profile = (ImageView)findViewById(R.id.profile);
        title = (TextView)findViewById(R.id.items_title);

        title.setText("All Students View");

        getData();





        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(item_count >=0) {


                    name.setText(nameAry.get(item_count).get("name"));
                    email.setText(nameAry.get(item_count).get("email"));
                    Picasso.with(MainActivity.this).load(nameAry.get(item_count).get("image")).into(profile);

                }
                item_count++;

                if(item_count == nameAry.size()-1){
                    item_count=0;
                }

                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    private void getData() {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                nameAry.clear();

                try {
                    JSONArray keralaPlaceArray = response.getJSONArray("candidate");

                    for (int i = 0; i < keralaPlaceArray.length(); i++) {


                                HashMap<String,String> course_detail = new HashMap<String,String>();
                                JSONObject corseobj = new JSONObject(String.valueOf(keralaPlaceArray.get(i)));
                                Iterator<String> keys = corseobj.keys();
                                while(keys.hasNext()) {
                                    String key = keys.next();
                                    course_detail.put(key, String.valueOf(corseobj.get(key)));
                                }
                                nameAry.add(course_detail);

                            }
                            Log.v(TAG,"name_detail="+nameAry);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                name.setText(nameAry.get(0).get("name"));
                email.setText(nameAry.get(0).get("email"));
                Picasso.with(MainActivity.this).load(nameAry.get(0).get("image")).into(profile);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:" + error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(request);


    }

}



