package com.example.myquizzire;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class question extends AppCompatActivity {
    static
    {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    public  MediaPlayer oursound1;

    public static final String file_name="my quizzire";
    public static final String key_name="questions";

    private TextView question,noIndicator;
    private FloatingActionButton time;
    private LinearLayout optionconatiner;
    private Button share,next;
    private int count=0;
    private int position=0;
    private int score=0;
    private String temp1;

    private List<questionmodel>list;
    private Dialog loadingdialog;


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CountDownTimer(45000,1000) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(final long l) {
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getApplicationContext(),""+l/1000 +" SECS ARE LEFT",Toast.LENGTH_SHORT).show();
                    }
                });

                    if (l < 35000) {
                        time.setImageDrawable(getDrawable(R.drawable.time1_border));

                        oursound1=MediaPlayer.create(com.example.myquizzire.question.this,R.raw.tiktok);
                        oursound1.start();
                    }
                }

            @Override
            public void onFinish() {
                if (position!=list.size()) {
                    Intent score_int = new Intent(question.this, end.class);
                    score_int.putExtra("score", score);
                    score_int.putExtra("total", list.size());


                    startActivity(score_int);
                    finish();
                    oursound1=null;
                    oursound1.release();
                    Toast.makeText(question.this, "Your Time Is Over !!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }.start();
        setContentView(R.layout.activity_question);
       question=findViewById(R.id.question);
       noIndicator=findViewById(R.id.number_indicator);
       time=findViewById(R.id.time);
       optionconatiner=findViewById(R.id.option_container);
       share=findViewById(R.id.share);
       next=findViewById(R.id.submit);

       preferences=getSharedPreferences(file_name, Context.MODE_PRIVATE);
       editor=preferences.edit();
       gson=new Gson();

        temp1=String.valueOf(getIntent().getStringExtra("TITLE"));


        loadingdialog=new Dialog(this);
        loadingdialog.setContentView(R.layout.loading);
        loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingdialog.setCancelable(false);

        list=new ArrayList<>();
        loadingdialog.show();
        myRef.child("set").child(temp1).child("question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(questionmodel.class));

                }
                if (list.size()>0){

                    for (int i=0;i<4;i++){
                        optionconatiner.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkanswer((Button) view);

                            }
                        });
                    }
                    playanime(question,0,list.get(position).getQuestion());
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            next.setEnabled(false);
                            next.setAlpha(0.7f);
                            enableOption(true);
                            position++;
                            if (position== list.size()){
                                 Intent score_int=new Intent(question.this,end.class);
                                 score_int.putExtra("score",score);
                                 score_int.putExtra("total",list.size());

                                 startActivity(score_int);
                                oursound1.stop();
                                oursound1.reset();
                                oursound1=null;
                                oursound1.release();
                                 finish();

                                return ;

                            }
                            count=0;
                            playanime(question,0,list.get(position).getQuestion());
                        }
                    });


                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(question.this,"YOU CAN USE THE MAIL ONLY !", Toast.LENGTH_SHORT).show();
                            String body=list.get(position).getQuestion()+" \n "+list.get(position).getA()+"\n"+list.get(position).getB()+"\n"+list.get(position).getC()+"\n"+list.get(position).getD();
                            Intent share_intent=new Intent(Intent.ACTION_SEND);
                            share_intent.setType("plain/text");
                            share_intent.putExtra(Intent.EXTRA_SUBJECT,"challenge");
                            share_intent.putExtra(Intent.EXTRA_TEXT,body);
                            startActivity(Intent.createChooser(share_intent,"share via"));
                        }
                    });
                    loadingdialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    private void playanime(final View view, final int value,final String data){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (value == 0 && count<4){
                    String option="";
                    if(count==0){option=list.get(position).getA();}
                    else if(count==1){option=list.get(position).getB();}
                    else if(count==2){option=list.get(position).getC();}
                    else if(count==3){option=list.get(position).getD();}
                    playanime(optionconatiner.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {


               if (value==0){
                   try {
                       ((TextView)view).setText(data);
                       noIndicator.setText(position+1+"/"+list.size());
                   }
                   catch (ClassCastException ex){
                       ((Button)view).setText(data);
                   }
                   view.setTag(data);
                   playanime(view,1,data);
               }


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }
    private void checkanswer(Button selectedoption){
        enableOption(false);
        next.setEnabled(true);
        next.setAlpha(1);
        if (selectedoption.getText().toString().equals(list.get(position).getCorrect())){

            score++;

        }

    }
    private void enableOption(boolean enable){
        for (int i=0;i<4;i++){
            optionconatiner.getChildAt(i).setEnabled(enable);
          if (enable){
              optionconatiner.getChildAt(i);
            }
        }
    }



}
