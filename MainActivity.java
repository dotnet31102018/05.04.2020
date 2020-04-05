package hello.itay.com.a05apr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText idET, nameET, surenameET, phET;
    TextView logTV;
    DatabaseHelper db;

    private static final int DELETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idET = findViewById(R.id.idET);
        nameET = findViewById(R.id.nameET);
        surenameET = findViewById(R.id.surenameET);
        phET = findViewById(R.id.phET);
        logTV = findViewById(R.id.logTV);

        db = new DatabaseHelper(this);
        // ADD
        findViewById(R.id.button_add).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (db.insertData(nameET.getText().toString(),
                                surenameET.getText().toString(),
                                phET.getText().toString()))
                            QuickToast("Inserted...");
                        else
                            QuickToast("Failed...");
                    }
                }
        );

        // View All
        findViewById(R.id.button_viewAll).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor = db.getAllData();
                        if (cursor.getCount() == 0)
                        {
                            QuickToast("No data!");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (cursor.moveToNext())
                        {
                            //" (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                            //"NAME TEXT, SURENAME TEXT, PHONE TEXT)");
                            buffer.append("Id :" + cursor.getString(0) + "\n");
                            buffer.append("Name :" + cursor.getString(1) + "\n");
                            buffer.append("Surename :" + cursor.getString(2) + "\n");
                            buffer.append("Phone :" + cursor.getString(3) + "\n");
                        }
                        showMessage("View All", buffer.toString());
                    }
                }
        );
        // UPDATE
        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.updateData(idET.getText().toString(), nameET.getText().toString(),
                        surenameET.getText().toString(),
                        phET.getText().toString()))
                    QuickToast("Updated row in id " + idET.getText().toString() + " ...");
                else
                    QuickToast("Failed...");
            }
        });
        // DELETE
        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent delete_activity_intent = new Intent(getBaseContext(), DeleteActivity.class);
                //startActivity(delete_activity_intent);
                startActivityForResult(delete_activity_intent, DELETE_REQUEST_CODE);

                /*
                final String id_delete = idET.getText().toString();

                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);


                b.setTitle("delete row with id " + id_delete);

                b.setMessage("Are you sure you want to delete?");

                b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        if (db.deleteData(idET.getText().toString()) > 0)
                            QuickToast("deleted row id " + id_delete + " ...");
                        else
                            QuickToast("Failed...");
                    }
                });
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        QuickToast("Delete aborted...");

                    }
                });

               AlertDialog alert = b.create();
               alert.show();
               */
            }
        });

        Cursor cursor = db.getAllData();
        if (cursor.getCount() == 0)
        {
            QuickToast("No data!");
            return;
        }

        logTV.setText("no last action...");
    }

    // will be executed when the startActivityForResult was completed...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //logTV.setText("user deleted a row ...");
                logTV.setText(data.getStringExtra("action").toString());
            }
            else  if (resultCode == RESULT_CANCELED) {
                logTV.setText("user cancalled row deletion ...");
            }
        }
    }

    private void QuickToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
