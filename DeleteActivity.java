package hello.itay.com.a05apr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteActivity extends AppCompatActivity {

    EditText id_deleteET;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        db = new DatabaseHelper(this);

        id_deleteET = findViewById(R.id.id_deleteET);

        // DELETE
        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String id_delete = id_deleteET.getText().toString();

                AlertDialog.Builder b = new AlertDialog.Builder(DeleteActivity.this);

                b.setTitle("delete row with id " + id_delete);

                b.setMessage("Are you sure you want to delete?");

                b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        if (db.deleteData(id_deleteET.getText().toString()) > 0) {
                            QuickToast("deleted row id " + id_delete + " ...");

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("action", "deleted row id " + id_delete);

                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }
                        else
                            QuickToast("Failed...");
                    }
                });
                b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        QuickToast("Delete aborted...");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("action", "deleted");

                        setResult(RESULT_CANCELED, resultIntent);
                        finish();
                    }
                });

                AlertDialog alert = b.create();
                alert.show();
            }
        });
    }
    private void QuickToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
