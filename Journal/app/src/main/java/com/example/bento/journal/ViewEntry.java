package com.example.bento.journal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewEntry extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        getSupportActionBar().hide();


        final Bundle b=this.getIntent().getExtras();
        TextView title = (TextView) findViewById(R.id.titleinf);
        TextView body = (TextView) findViewById(R.id.bodyinf);
        TextView add = (TextView) findViewById(R.id.viewAdd);
        TextView dat = (TextView) findViewById(R.id.viewDate);

        title.setText(b.getString("title"));
        body.setText(b.getString("body"));
        add.setText(b.getString("add"));
        dat.setText(b.getString(("date")));
        body.setMovementMethod(new ScrollingMovementMethod());


        Button back = (Button) findViewById(R.id.backBtn);
        Button del = (Button) findViewById(R.id.DeleteBtn);
        openDatabase();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewEntry.this,MainActivity.class );
                startActivity(i); //start the new activity
                finish();

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewEntry.this);
                alertDialogBuilder.setMessage("Are you sure you want delete this entry?");

                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String id = b.getString("id").trim();

                                String sql = "DELETE FROM persons WHERE id=" + id + ";";
                                db.execSQL(sql);
                                Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ViewEntry.this,MainActivity.class );
                                startActivity(i); //start the new activity
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    protected void openDatabase() {
        db = openOrCreateDatabase("entries", getApplicationContext().MODE_PRIVATE, null);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
