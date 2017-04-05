package com.example.bento.journal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor c;
    private String entryTitle[];
    private String id[];
    private String entryBody[];
    private String entryAdd[];
    private String entryLong[];
    private String entryDate[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newEntry = (Button) findViewById(R.id.newEntry);
        newEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent i = new Intent(MainActivity.this,NewEntry.class );
                startActivity(i); //start the new activity
                finish();
            }
        });
        openDatabase();
        c = db.rawQuery("SELECT * FROM persons ORDER BY id DESC", null);
        int n = c.getCount();
        int cols = c.getColumnCount();
        c.moveToFirst();

        id = new String[n];
        entryTitle = new String[n];
        entryBody = new String[n];
        entryAdd = new String[n];
        entryLong = new String[n];
        entryDate = new String[n];

        for (int i = 0; i < n; i++){
            id[i] = c.getString(0);
            entryTitle[i] = c.getString(1);
            entryBody[i] = c.getString(2);
            entryAdd[i] = c.getString(3);
            entryDate[i] = c.getString(4);
            c.moveToNext();

        }

        ListView lv = (ListView)findViewById(R.id.list); //attach the listview
        lv.setAdapter(new MyCustomBaseAdapter(this, id, entryTitle, entryBody, entryAdd, entryDate)); //create a new adapter

    }

    protected void openDatabase() {
        db = openOrCreateDatabase("entries", getApplicationContext().MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS persons(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR,body VARCHAR, lat VARCHAR, date VARCHAR);");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.newA:
                Intent i = new Intent(MainActivity.this,NewEntry.class );
                startActivity(i); //start the new activity
                finish();
                break;
            default:
                break;
        }
        return true;
    }



    //Create the adapter class as well as the inflater to show the list items correctly
    private class MyCustomBaseAdapter extends BaseAdapter {

        private String[] i;
        private String[] t;
        private String[] b;
        private String[] adds;
        private String[] longs;
        private String[] d;
        private LayoutInflater mInflater;
        private Context context;
        private int[] newP;
        private Bundle bund = new Bundle();


        public MyCustomBaseAdapter(Context con, String[] id, String[] titles, String[] body, String address[], String[] dat){
            context = con;
            i = id;
            t = titles;
            b = body;
            adds = address;
            d = dat;
            mInflater = LayoutInflater.from(context);

        }


        @Override
        public int getCount() {
            return t.length;
        }

        @Override
        public Object getItem(int position) {
            return t[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        //Set the view to contain the image on the left and the text on the right, inflate
        //the list view with these items
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null){
                convertView = mInflater.inflate(R.layout.custom_row_view, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById((R.id.titleRow));
                holder.dat = (TextView)convertView.findViewById(R.id.dateRow);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }


            holder.title.setText(t[position]);
            holder.dat.setText(d[position]);

            //Toast.makeText(getApplicationContext(), t[position] + "    " + d[position], Toast.LENGTH_LONG).show();


            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bund.clear();
                    bund.putString("id",i[position]);
                    bund.putString ("title", t[position]);
                    bund.putString("body", b[position]);
                    bund.putString("add", adds[position]);
                    bund.putString("date", d[position]);

                   Intent i = new Intent(context.getApplicationContext(),ViewEntry.class ); //Go to details activity
                   i.putExtras(bund);
                   context.startActivity(i);//start the details
                    finish();

                }
            });

            holder.title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Discard This Entry?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with discard
                            String id = i[position].trim();

                            String sql = "DELETE FROM persons WHERE id=" + id + ";";
                            db.execSQL(sql);
                            Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                            recreate();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing

                        }
                    });
                    alertDialogBuilder.show();
                    return true;
                }
            });
            holder.dat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bund.clear();
                    bund.putString("id",i[position]);
                    bund.putString ("title", t[position]);
                    bund.putString("body", b[position]);
                    bund.putString("add", adds[position]);
                    bund.putString("date", d[position]);


                    Intent i = new Intent(context.getApplicationContext(),ViewEntry.class ); //Go to details activity
                    i.putExtras(bund);
                    context.startActivity(i);//start the details
                    finish();

                }
            });
            holder.dat.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setMessage("Discard This Entry?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with discard
                            String id = i[position].trim();

                            String sql = "DELETE FROM persons WHERE id=" + id + ";";
                            db.execSQL(sql);
                            Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                            recreate();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing

                        }
                    });
                    alertDialogBuilder.show();
                    return true;
                }
            });
            return convertView;

        }
    }
    static class ViewHolder{

        TextView title;
        TextView dat;
    }
}


