package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.BsonDocument;
import org.bson.Document;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtID, edtPwd;

//    MongoClientURI uri = new MongoClientURI(
//            "mongodb+srv://dodamdodam:<dodamdodam>@cluster0.u4wd4.mongodb.net/<dodamdodam>?retryWrites=true&w=majority");

//    String MongoDB_IP = "mongodb+srv://dodamdodam:<dodamdodam>@cluster0.u4wd4.mongodb.net/<dodamdodam>?retryWrites=true&w=majority";
//    int MongoDB_PORT = 27017;
//    String DB_NAME = "testDB";
//
//    //Connect to MongoDB
//    MongoClient  mongoClient = new MongoClient(new ServerAddress(MongoDB_IP, MongoDB_PORT));
//    DB db = mongoClient.getDB(DB_NAME);
//    DBCollection collection = db.getCollection("testCollection02");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtID = findViewById(R.id.edtUserID);
        edtPwd = findViewById(R.id.edtPassword);

//        DBCollection mongoCollection = db.getCollection("member");



//        Log.d("ddd", mongoCollection.find());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, WifiActivity.class);
                startActivity(intent);

            }
        });


    }


}
