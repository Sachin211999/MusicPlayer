package com.example.sachu.mediaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Main3Activity extends AppCompatActivity {
    ListView listView;
SearchView searchView;
    ArrayList<String> audioList = new ArrayList<>();
    ArrayList<String> songsname = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    //String[] songs={"Space Bound","So Bad","Not Afraid","Beautiful","25 to life","Stan"};
    //int[] songsid={R.raw.s1,R.raw.s2,R.raw.s3,R.raw.s4,R.raw.s5,R.raw.s6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

   searchView=(SearchView)findViewById(R.id.search);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
        listView = (ListView) findViewById(R.id.listview);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)
                );
                String path = cursor.getString(
                        cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                );

                audioList.add(path);
                songsname.add(name);

            } while (cursor.moveToNext());
            cursor.close();
adapter=new ArrayAdapter<String>(this, R.layout.listrow, R.id.textView2, songsname);
            listView.setAdapter(adapter);


        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
view.setSelected(true);

                Intent intent = new Intent(Main3Activity.this, MainActivity.class).putExtra("songs", audioList).putExtra("songsname", songsname).putExtra("pos", position);

                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if(songsname.contains(query))
                {
final int i=songsname.indexOf(query);
                    adapter.getFilter().filter(query);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            position=i+1;
                            Intent intent = new Intent(Main3Activity.this, MainActivity.class).putExtra("songs", audioList).putExtra("songsname", songsname).putExtra("pos", position);

                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    Toast.makeText(Main3Activity.this,"NO MATCH FOUND", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final int i=songsname.indexOf(newText);
                adapter.getFilter().filter(newText);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        position=i;
                        Intent intent = new Intent(Main3Activity.this, MainActivity.class).putExtra("songs", audioList).putExtra("songsname", songsname).putExtra("pos", position);

                        startActivity(intent);
                    }
                });
                return true;
            }
        });

    }
}