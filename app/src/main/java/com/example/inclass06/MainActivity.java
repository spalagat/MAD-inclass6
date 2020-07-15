package com.example.inclass06;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button go;
    String[] keywords={"business", "entertainment", "general", "health", "science", "sports", "technology"};
    EditText e1;
    String Api_Key = "8ff8a26d789141de8433feb7e17e164f";
    TextView title;
    TextView date;
    ImageView next;
    ImageView prev;
    int count;
    TextView Loading;
    TextView description;
    ArrayList<Articles> art;
    ProgressBar image_progress;
    ImageView article_image;
    TextView position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        art = new ArrayList<Articles>();
        e1 = findViewById(R.id.editText);
        title = findViewById(R.id.textView2);
        date = findViewById(R.id.textView3);
        next = findViewById(R.id.imageView3);
        prev = findViewById(R.id.imageView2);
        position = findViewById(R.id.textView5);
        description=findViewById(R.id.textView4);
        Loading =findViewById(R.id.textView);
        image_progress = findViewById(R.id.progressBar);
        article_image = findViewById(R.id.imageView);
        Loading.setVisibility(View.INVISIBLE);
        image_progress.setVisibility(View.INVISIBLE);

        go = findViewById(R.id.Go_button);
        isConnected();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Choose a Keyword")
                        .setSingleChoiceItems(keywords, keywords.length, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ListView lw = ((AlertDialog) dialogInterface).getListView();
                                Object checkedItem = lw.getAdapter().getItem(i);
                                if(isConnected()) {
                                    e1.setText(checkedItem.toString());
                                    url_parsing();
                                    //Toast.makeText(MainActivity.this, e1.getText().toString(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setClickable(true);
                if(isConnected()){
                    if(count==art.size()-1){
                        count=-1;
                    }

                    //Toast.makeText(MainActivity.this, "Clicked next", Toast.LENGTH_SHORT).show();

                    count = count + 1;

                    news_data(count);
                }


            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prev.setClickable(true);
                if(isConnected()){
                   if(count==0){
                        count=art.size();}
                    count = count - 1;
                    news_data(count);

                    //Log.d("pos", "prev Image");
                }
            }
        });
        }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void url_parsing(){
        String category = e1.getText().toString();
        String Url ="https://newsapi.org/v2/top-headlines?country=us&category="+category+"&apiKey="+Api_Key;
        new HeadlineAsync(MainActivity.this).execute(Url);
    }
    public void get_jsondata(ArrayList<Articles> article_data){
        this.art = article_data;
        Log.d("length",String.valueOf(art.size()));

        count=0;
        news_data(count);
    }
    public void news_data(int article_no){


        Articles article_data = art.get(count);
        title.setText(article_data.title);
        date.setText(article_data.date);
        if(article_data.description=="null"){
            description.setText("");}
        else{
            description.setText(article_data.description);
        }
        position.setText((count+1)+" out of "+art.size());

        Log.d("url",article_data.image_url);
        title.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        article_image.setVisibility(View.INVISIBLE);
        Loading.setVisibility(View.VISIBLE);
        image_progress.setVisibility(View.VISIBLE);
        new ImageShowingAsync(MainActivity.this).execute(article_data.image_url);
        if (article_no==art.size()-1){


        }
        else if(article_no==0){
            //count=art.size()-1;
            Log.d("pos",String.valueOf(count));
        }
        else{
            prev.setClickable(true);
            Log.d("pos",String.valueOf(count));
        }


        }




    public void display_image(Bitmap bitmap) {
        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        title.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        image_progress.setVisibility(View.INVISIBLE);
        Loading.setVisibility(View.INVISIBLE);

        if (bitmap==null){
            article_image.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "No Image To Display", Toast.LENGTH_SHORT).show();

        }
        else{
        //Log.d("bitmapString",bitmap.toString());
            article_image.setVisibility(View.VISIBLE);
        imageview.setImageBitmap(bitmap);}
    }
}
