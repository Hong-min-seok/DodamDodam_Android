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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.BsonDocument;
import org.bson.Document;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtID, edtPwd;
    MongoClientURI mongoUri;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtID = findViewById(R.id.edtUserID);
        edtPwd = findViewById(R.id.edtPassword);

//        mongoUri = new MongoClientURI();
        mongoClient = new MongoClient(new ServerAddress("cluster0-shard-00-01.u4wd4.mongodb.net",27017));

        MongoDatabase db = mongoClient.getDatabase("dodamdodam");
        mongoCollection = db.getCollection("member");
        BasicDBObject query = new BasicDBObject();
        query.put("userid", "test");
        query.put("password","test");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FindIterable<Document> r = mongoCollection.find(query);
                Log.d("ddd",r.toString());

//                Intent intent = new Intent(LoginActivity.this, WifiActivity.class);
//                startActivity(intent);

            }
        });


    }


}
