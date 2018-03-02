package de.iteratec.slab.segway.remote.robot.service;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.iteratec.slab.segway.remote.robot.R;

/**
 * Created by sei on 22.02.2018.
 */

public class LoomoRequestService {

    private static final String TAG = "LoomoRequestService";

    public static Context context;
    public static LoomoRequestService instance;

    public static LoomoRequestService getInstance() {
        if(instance == null) {
            throw new IllegalStateException("LoomoRequestService has no instance initialized yet");
        }
        return instance;
    }

    public LoomoRequestService(Context context) {
        this.context = context;
    }

    //TODO initiate text in Loomo Robot for request
    final TextView mTextView = (TextView) findViewById(R.id.text);

    //Instantiate RequestQueue
    RequestQueue queue = Volley.newRequestQueue(context);

    String url = "http://www.google.com";

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    mTextView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mTextView.setText("That didn't work!");
        }
    });

    private void blub(){
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
