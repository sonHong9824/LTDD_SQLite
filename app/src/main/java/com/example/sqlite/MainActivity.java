package com.example.sqlite;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;
    ImageButton btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.item_listview, arrayList);
        listView.setAdapter(adapter);
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(view -> {
            openAddNoteDialog();
        });

        InitDataBaseSQLite(); // Khởi tạo database
//        createDatabaseSQLite(); // Chèn dữ liệu
        databaseSQLite(); // Lấy dữ liệu và hiển thị
    }

    private void openAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm ghi chú");

        // Tạo EditText để nhập tên ghi chú
        EditText input = new EditText(this);
        input.setHint("Nhập nội dung ghi chú...");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String noteText = input.getText().toString().trim();
            if (!noteText.isEmpty()) {
                // Thêm vào SQLite
                databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '" + noteText + "')");
                Toast.makeText(MainActivity.this, "Đã thêm ghi chú!", Toast.LENGTH_SHORT).show();
                databaseSQLite(); // Cập nhật danh sách
            } else {
                Toast.makeText(MainActivity.this, "Ghi chú không được để trống!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void createDatabaseSQLite(){
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'Vi du SQLite 1')");
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'Vi du SQLite 2')");
    }

    private void InitDataBaseSQLite() {
        databaseHandler = new DatabaseHandler(this, "notes.sql", null, 1);
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNotes VARCHAR(200))");
    }
    private void databaseSQLite(){
        Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
        arrayList.clear(); // Xóa dữ liệu cũ để tránh trùng lặp

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            arrayList.add(new NotesModel(id, name)); // Thêm vào danh sách
        }
        cursor.close();

        adapter.notifyDataSetChanged(); // Cập nhật giao diện
    }
    public void DiaLogCapNhatNotes(String name, int id){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_note);

        EditText editText = dialog.findViewById(R.id.edt_edit_note);
        Button buttonsave = dialog.findViewById(R.id.btn_save_note);
        Button buttoncancel = dialog.findViewById(R.id.btn_cancel);
        editText.setText(name);

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                databaseHandler.QueryData("UPDATE Notes SET NameNotes = '"+ name + "' WHERE Id = '" + id + "'");
                Toast.makeText(MainActivity.this, "Đã cập nhập note thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                databaseSQLite();
            }
        });
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void DiaLogXoaNotes(String nameNote, int idNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa " + nameNote);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHandler.QueryData("DELETE FROM Notes WHERE Id = '"+ idNote +"'");
                Toast.makeText(MainActivity.this, "Đã xóa Notes "+ nameNote, Toast.LENGTH_SHORT).show();
                databaseSQLite();

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}