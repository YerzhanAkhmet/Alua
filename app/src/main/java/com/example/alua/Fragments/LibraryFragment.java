package com.example.alua.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alua.Activities.LibraryItemActivity;
import com.example.alua.Classes.Book;
import com.example.alua.Data.LibraryContract.BookCreate;
import com.example.alua.Data.LibraryDbHelper;
import com.example.alua.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LibraryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "LibraryFragment";
    private String mParam1;
    private String mParam2;
    GridView gridView;
    GridViewAdapter gridViewAdapter;
    LibraryDbHelper mDbHelper;
    List<Book> books = new ArrayList<>();
    List<Book> booksToGridView = new ArrayList<>();
    Button loadDataButton;
    Spinner changeGenre;
    String genre;
    TextView libraryTV;
    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {}

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final FloatingActionButton fab = getView().findViewById(R.id.fab);
        gridView = getView().findViewById(R.id.library_list);
        loadDataButton = getView().findViewById(R.id.load_data_button);
        changeGenre = getView().findViewById(R.id.spinner_genre);
        libraryTV = getView().findViewById(R.id.libraryTV);
        mDbHelper = new LibraryDbHelper(getContext());
        Paper.init(getContext());
        setGenre();
        if (Paper.book().contains("books")){
            if (books.isEmpty()){
                getView().setVisibility(View.VISIBLE);
                loadDataButton.setVisibility(View.GONE);
                fab.show();
                displayDatabaseInfo();
            }
        } else {
            gridView.setVisibility(View.GONE);
            loadDataButton.setVisibility(View.VISIBLE);
            fab.hide();
        }
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBook();
                displayDatabaseInfo();
                gridView.setVisibility(View.VISIBLE);
                fab.show();
                loadDataButton.setVisibility(View.GONE);
                Paper.book().write("books", 1);
                setGenre();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
                books.clear();
                gridView.setVisibility(View.GONE);
                loadDataButton.setVisibility(View.VISIBLE);
                fab.hide();
                if (Paper.book().contains("books")){
                    Paper.book().delete("books");
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        boolean onNavigationItemSelected(MenuItem item);

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GridViewAdapter extends ArrayAdapter<Book>{

        public GridViewAdapter(@NonNull Context context, int resourse, List<Book> books) {
            super(context, resourse, books);
        }

        public GridViewAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View v = convertView;
            Book book = getItem(position);
            if(null == v){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.books_item, null);
            }

            TextView name = v.findViewById(R.id.library_name);
            TextView author = v.findViewById(R.id.library_author);
            ImageView logo = v.findViewById(R.id.library_logo);

            name.setText(book.getName());
            author.setText(book.getAuthor());
            Glide.with(getContext()).load(book.getAvatar()).into(logo);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LibraryItemActivity.class);
                    if (booksToGridView.size()>1 && booksToGridView.size()<4){
                        intent.putExtra("id",  position+1);
                    } else {
                        intent.putExtra("id",  position);
                    }
                    startActivity(intent);
                }
            });
            registerForContextMenu(v);
            return v;
        }
    }

    private void createBook() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        ContentValues values4 = new ContentValues();
        values1.put(BookCreate.COLUMN_NAME, "Снежная королева");
        values1.put(BookCreate.COLUMN_AUTHOR, "Ганс Христиан Андерсен");
        values1.put(BookCreate.COLUMN_GENRE, BookCreate.GENRE_FAIRYTALES);
        values1.put(BookCreate.COLUMN_CONTENT, getString(R.string.snow_queen_chapter1) + getString(R.string.snow_queen_chapter2));
        values1.put(BookCreate.COLUMN_AVATAR, "https://images.radario.ru/images/webeventposter/c4eb5489652e40dc97c0562e5ee0358e.jpg");
        values2.put(BookCreate.COLUMN_NAME, "Последний лист");
        values2.put(BookCreate.COLUMN_AUTHOR, "О'Генри");
        values2.put(BookCreate.COLUMN_GENRE, BookCreate.GENRE_STORIES);
        values2.put(BookCreate.COLUMN_CONTENT, getString(R.string.the_last_leaf));
        values2.put(BookCreate.COLUMN_AVATAR, "https://files.adme.ru/files/news/part_87/878410/14422760-1-650-d85af6b851-1484580106.jpg");
        values3.put(BookCreate.COLUMN_NAME, "Белые слоны");
        values3.put(BookCreate.COLUMN_AUTHOR, "Эрнест Хемингуэй");
        values3.put(BookCreate.COLUMN_GENRE, BookCreate.GENRE_STORIES);
        values3.put(BookCreate.COLUMN_CONTENT, getString(R.string.white_elephants));
        values3.put(BookCreate.COLUMN_AVATAR, "https://writology.com/userdata/writers/267/blog/55fa4826a6bc86cc544c36eb582d2e208e7362a05dfceeb79e_pimgpsh_fullsize_distr.jpg");
        values4.put(BookCreate.COLUMN_NAME, "Который из трех");
        values4.put(BookCreate.COLUMN_AUTHOR, "Антон Павлович Чехов");
        values4.put(BookCreate.COLUMN_GENRE, BookCreate.GENRE_STORIES);
        values4.put(BookCreate.COLUMN_CONTENT, getString(R.string.which_of_the_three));
        values4.put(BookCreate.COLUMN_AVATAR, "https://i.livelib.ru/workpic/1000976590/200/29c2/A._P._Chehov__Kotoryj_iz_treh.jpg");


        List<ContentValues> values = new ArrayList<>();
        values.add(0, values1);
        values.add(1, values2);
        values.add(2, values3);
        values.add(3, values4);
        db.insert(BookCreate.TABLE_NAME, null, values.get(0));
        db.insert(BookCreate.TABLE_NAME, null, values.get(1));
        db.insert(BookCreate.TABLE_NAME, null, values.get(2));
        db.insert(BookCreate.TABLE_NAME, null, values.get(3));
    }

    public void deleteBook(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(BookCreate.TABLE_NAME,null, null);
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BookCreate._ID,
                BookCreate.COLUMN_NAME,
                BookCreate.COLUMN_AUTHOR,
                BookCreate.COLUMN_GENRE,
                BookCreate.COLUMN_CONTENT,
                BookCreate.COLUMN_AVATAR};

        // Делаем запрос
        Cursor cursor = db.query(
                BookCreate.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        try {
            int idColumnIndex = cursor.getColumnIndex(BookCreate._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookCreate.COLUMN_NAME);
            int authorColumnIndex = cursor.getColumnIndex(BookCreate.COLUMN_AUTHOR);
            int contentColumnIndex = cursor.getColumnIndex(BookCreate.COLUMN_CONTENT);
            int genreColumnIndex = cursor.getColumnIndex(BookCreate.COLUMN_GENRE);
            int avatarColumnIndex = cursor.getColumnIndex(BookCreate.COLUMN_AVATAR);
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
    public void setGenre(){
        final ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array_genre_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        changeGenre.setAdapter(genderSpinnerAdapter);
        changeGenre.setSelection(3);
        changeGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.genre_ft))) {
                        genre = "ft";
                    } else if (selection.equals(getString(R.string.genre_s))) {
                        genre = "s";
                    } else if (selection.equals(getString(R.string.genre_mi))){
                        genre = "mi";
                    } else {
                        genre = "all";
                    }
                }
                booksToGridView.clear();
                gridViewAdapter = new GridViewAdapter(getContext(), R.layout.books_item);
                gridView.setAdapter(gridViewAdapter);
                libraryTV.setVisibility(View.GONE);
                if(!books.isEmpty()) {
                    for (int i = 0; i < books.size(); i++) {
                        if (genre != null) {
                            if (genre.equals("ft")) {
                                if (books.get(i).getGenre().equals("1")) {
                                    booksToGridView.add(books.get(i));
                                }
                            } else if (genre.equals("s")) {
                                if (books.get(i).getGenre().equals("2")) {
                                    booksToGridView.add(books.get(i));
                                }
                            } else if (genre.equals("mi")){
                                if (books.get(i).getGenre().equals("3")) {
                                    booksToGridView.add(books.get(i));
                                }
                            } else {
                                booksToGridView.add(books.get(i));
                            }
                        }
                    }
                }
                if (!booksToGridView.isEmpty()) {
                    if (booksToGridView.size()<2){
                        gridView.setNumColumns(1);
                    } else {
                        gridView.setNumColumns(2);
                    }
                    gridViewAdapter = new GridViewAdapter(getActivity(), R.layout.books_item, booksToGridView);
                    gridView.setAdapter(gridViewAdapter);
                } else {
                    libraryTV.setText("No such files :(");
                    libraryTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
