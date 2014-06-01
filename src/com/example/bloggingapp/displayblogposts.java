package com.example.bloggingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class displayblogposts extends Activity {

	String url;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaylayout);
		url = "http://bloggy.comeze.com/read_blogposts.php";
		lv = (ListView) findViewById(R.id.lv);
		getdataclass obj = new getdataclass();
		obj.execute(url);
	}

	class getdataclass extends AsyncTask<String, Integer, ArrayList<String>> {

		HttpResponse response;
		HttpGet request;
		HttpClient client;
		JSONArray jarray;
		JSONObject jobject;
		ArrayList<String> list;
		ArrayAdapter<String> adapter;
		String data;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			list = new ArrayList<String>();
			adapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_list_item_1, list);
			client = new DefaultHttpClient();
			request = new HttpGet(url);
			// response = null;
			jobject = new JSONObject();
			// jarray = new JSONArray();
			data = "";
			Toast.makeText(getApplicationContext(), "onPreExecute",
					Toast.LENGTH_SHORT).show();

		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), "doInBackground",
			// Toast.LENGTH_SHORT).show();

			try {
				response = client.execute(request);
				// Toast.makeText(getApplicationContext(), "request executed" ,
				// Toast.LENGTH_SHORT).show();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {

				InputStream istream =response.getEntity().getContent();
				InputStreamReader istreamreader = new InputStreamReader(istream);
				BufferedReader bfreader = new BufferedReader(istreamreader);
				StringBuffer sbuffer = new StringBuffer("");
				String nl = System.getProperty("line.seperator");
				String l = "";

				while ((l = bfreader.readLine()) != null) {
					sbuffer.append(l + nl);
				}
				data = sbuffer.toString();
				jarray = new JSONArray(data);
				// Toast.makeText(getApplicationContext(), "jsonarray created" ,
				// Toast.LENGTH_SHORT).show();
				jobject=jarray.optJSONObject(0);
				data=jobject.getString("title");
				Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
				
				int i = 0;
				while (true) {
					jobject = jarray.optJSONObject(i);
					if (jobject == null) break;
					data = jobject.getString("title")+" : "+jobject.getString("description");
					Log.i("tag", data);
					list.add(data);
					++i;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;

		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(),
					data, Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "onPostExecute",
					Toast.LENGTH_SHORT).show();

			lv.setAdapter(adapter);
			Toast.makeText(getApplicationContext(), "list displayed",
					Toast.LENGTH_SHORT).show();
		}

	}

}
