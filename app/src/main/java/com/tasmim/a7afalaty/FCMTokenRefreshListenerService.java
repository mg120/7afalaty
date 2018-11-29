package com.tasmim.a7afalaty;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ma7MouD on 4/30/2018.
 */

public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // start service to register new token
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("refreshed_token", token);
        sendTokenToServer(token);

    }

    // method use vollezy to send token to server and stop the service when done or error happened
    private void sendTokenToServer(final String token) {
        String ADD_TOKEN_URL = "https://cookehome.com/CookApp/User/sendToken.php";
//        StringRequest request = new StringRequest(Request.Method.POST, ADD_TOKEN_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean success = jsonObject.getBoolean("success");
////                    Toast.makeText(FCMTokenRefreshListenerService.this, "successs:" + success, Toast.LENGTH_SHORT).show();
//                    if (success) {
//                        //  preferences.edit().putBoolean("token_sent", true).apply();
//                        Log.e("Registration Service", "Response : Send Token Success");
////                        stopSelf();
//
//                    } else {
//                        // preferences.edit().putBoolean("token_sent", false).apply();
//                        Log.e("Registration Service", "Response : Send Token Failed");
////                        stopSelf();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // preferences.edit().putBoolean("token_sent", false).apply();
//                error.printStackTrace();
//                error.getMessage();
//                Log.e("Registration Service", "Error :Send Token Failed");
////                stopSelf();
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("token", token);
//                return params;
//            }
//        };
//
//        Volley.newRequestQueue(this).add(request);
    }
}
