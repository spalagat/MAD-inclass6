package com.example.inclass06;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HeadlineAsync extends AsyncTask<String,Void,ArrayList>{
    MainActivity activity;

    public HeadlineAsync(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Articles> doInBackground(String[] strings) {
        HttpURLConnection connection = null;
        //BufferedReader reader = null;
        URL url = null;
        ArrayList<Articles> result = new ArrayList<>();
        try {
            url = new URL( strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                       /* reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }*/
                String Json = IOUtils.toString(connection.getInputStream(), "UTF-8");


                JSONObject root = new JSONObject(Json);
                JSONArray articles = root.getJSONArray("articles");
                for (int i=0;i<articles.length();i++) {
                    JSONObject articleJson = articles.getJSONObject(i);
                    Articles article = new Articles();
                    article.title = articleJson.getString("title");
                    article.date = articleJson.getString("publishedAt");
                    article.image_url = articleJson.getString("urlToImage");
                    article.description = articleJson.getString("description");
                    result.add(article);
                }




                    //result = stringBuilder.toString();
            }

        } catch  (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Handle the exceptions
        catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //Close open connections and reader
            if (connection != null) {
                connection.disconnect();
            }
        }
        //Log.d("res",result);
        return result;


    }

    @Override
    protected void onPostExecute(ArrayList s) {

        super.onPostExecute(s);
        activity.get_jsondata(s);
    }
}
