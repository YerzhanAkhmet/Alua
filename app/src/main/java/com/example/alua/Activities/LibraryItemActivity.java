package com.example.alua.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alua.Classes.Book;
import com.example.alua.Data.LibraryContract;
import com.example.alua.Data.LibraryDbHelper;
import com.example.alua.R;

import java.util.ArrayList;
import java.util.List;

public class LibraryItemActivity extends AppCompatActivity {

    TextView name, author, content;
    ImageView image;
    LibraryDbHelper mDbHelper = new LibraryDbHelper(this);
    List<Book> books = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_item);
        name = findViewById(R.id.bookName);
        author = findViewById(R.id.bookAuthor);
        content = findViewById(R.id.bookContent);
        image = findViewById(R.id.bookImage);
        displayDatabaseInfo();
        Bundle argument = getIntent().getExtras();
        int id = argument.getInt("id");
        name.setText(books.get(id).getName());
        author.setText(books.get(id).getAuthor());
        content.setText(Html.fromHtml(books.get(id).getContent()));
        Glide.with(LibraryItemActivity.this).load(books.get(id).getAvatar()).into(image);
    }
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                LibraryContract.BookCreate._ID,
                LibraryContract.BookCreate.COLUMN_NAME,
                LibraryContract.BookCreate.COLUMN_AUTHOR,
                LibraryContract.BookCreate.COLUMN_GENRE,
                LibraryContract.BookCreate.COLUMN_CONTENT,
                LibraryContract.BookCreate.COLUMN_AVATAR};

        // Делаем запрос
        Cursor cursor = db.query(
                LibraryContract.BookCreate.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        try {
            int idColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate._ID);
            int nameColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate.COLUMN_NAME);
            int authorColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate.COLUMN_AUTHOR);
            int contentColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate.COLUMN_CONTENT);
            int genreColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate.COLUMN_GENRE);
            int avatarColumnIndex = cursor.getColumnIndex(LibraryContract.BookCreate.COLUMN_AVATAR);
            int i =0;

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                String currentContent = cursor.getString(contentColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                String currentAvatar = cursor.getString(avatarColumnIndex);
                books.add(i, new Book(currentName,
                        currentAuthor, currentContent, currentGenre, currentAvatar));
                i++;
            }
        } finally {
            cursor.close();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
