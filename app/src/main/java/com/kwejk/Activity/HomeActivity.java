package com.kwejk.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.MenuItem;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.kwejk.Adapter.CommentAdapter;
import com.kwejk.BuildConfig;
import com.kwejk.Fragments.Top12Fragment;
import com.kwejk.Fragments.Top24Fragment;
import com.kwejk.Fragments.Top48Fragment;
import com.kwejk.Model.CommentModel;
import com.kwejk.Model.MemeModel;
import com.kwejk.Model.VoteModel;

import android.widget.LinearLayout;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import com.kwejk.Parser.Comments;
import com.kwejk.Parser.CommentsResponseInterface;
import com.kwejk.Parser.Vote;
import com.kwejk.Adapter.Adapter;
import com.kwejk.R;
import com.kwejk.Fragments.HomeFragment;
import com.kwejk.Fragments.WaitingFragment;

import android.os.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.util.List;

import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CommentsResponseInterface {

    public static Toolbar toolbar;
    public static ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Adapter.Holder latestMem;
    private Dialog dialogGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeViews();
        toggleDrawer();
        initializeDefaultFragment(savedInstanceState,0);
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Strona Główna");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeDefaultFragment(Bundle savedInstanceState, int itemIndex){
        if (savedInstanceState == null){
            MenuItem menuItem = navigationView.getMenu().getItem(itemIndex).setChecked(true);
            onNavigationItemSelected(menuItem);
        }
    }

    private void toggleDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //Checks if the navigation drawer is open -- If so, close it
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // If drawer is already close -- Do not override original functionality
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        findViewById(R.id.loading).setVisibility(View.VISIBLE);
        switch (menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeFragment()).commit();
                closeDrawer();
                break;
            case R.id.waiting:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new WaitingFragment()).commit();
                closeDrawer();
                break;
            case R.id.top12:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Top12Fragment()).commit();
                closeDrawer();
                break;
            case R.id.top24:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Top24Fragment()).commit();
                closeDrawer();
                break;
            case R.id.top48:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Top48Fragment()).commit();
                closeDrawer();
                break;
        }
        return true;
    }

    private void closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void voteUp(View v) {
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        Adapter.Holder holder = (Adapter.Holder) parent.getTag();
        new Vote().execute(new VoteModel(holder, "up", v.getTag().toString()));
    }

    public void voteDown(View v) {
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        Adapter.Holder holder = (Adapter.Holder) parent.getTag();
        new Vote().execute(new VoteModel(holder, "down", v.getTag().toString()));
    }

    public void More(View v){
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        Adapter.Holder holder = (Adapter.Holder) parent.getTag();
        latestMem = holder;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.menu_layout);
        dialog.getWindow().getAttributes().dimAmount = 0.9f;
        dialog.show();
        dialogGlobal = dialog;
    }

    public void Save(View v){
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
         }else{
             SaveImageGranted();
         }
        dialogGlobal.dismiss();
    }

    public void Share(View v){
        Bitmap meme = ((BitmapDrawable)latestMem.getImage().getDrawable()).getBitmap();
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg");
            meme.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File imagePath = new File(getCacheDir(), "images");
        File newFile = new File(imagePath, "image.jpg");
        Uri contentUri = FileProvider.getUriForFile(this, "com.kwejk.fileprovider", newFile);
        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Udostępnij obrazek"));
        }
        dialogGlobal.dismiss();
    }

    public void Visit(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://kwejk.pl/obrazek/" + latestMem.ID));
        startActivity(browserIntent);
        dialogGlobal.dismiss();
    }

    public void Back(View v){
        dialogGlobal.dismiss();
    }

    public void ShowComments(View v){
        dialogGlobal.dismiss();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.background);
        dialog.setContentView(R.layout.comments_layout);
        dialog.getWindow().getAttributes().dimAmount = 0.9f;
        dialog.setTitle("Komentarze");
        dialog.show();
        dialogGlobal = dialog;

        new Comments(this).execute(latestMem.ID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SaveImageGranted();
                } else {
                    Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_SHORT).show();
                }
    }

    public void SaveImageGranted() {
        Bitmap bm = ((BitmapDrawable)latestMem.getImage().getDrawable()).getBitmap();
        OutputStream fOut = null;
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + File.separator + File.separator);
            root.mkdirs();
            File sdImageMainDirectory = new File(root, latestMem.ID + ".png");
            fOut = new FileOutputStream(sdImageMainDirectory);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "Pomyślnie zapisano obrazek w\n" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    @Override
    public void onParsingDone(List<CommentModel> cList) {
        ListView listView = (ListView) dialogGlobal.findViewById(R.id.listViewComments);
        CommentAdapter adapter = new CommentAdapter(this, R.layout.comments_layout, cList);
        listView.setAdapter(adapter);
    }

}
