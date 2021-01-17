package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DTO.WaterDTO;
import com.example.myapplication.Decorator.EventDecorator;
import com.example.myapplication.Decorator.OneDayDecorator;
import com.example.myapplication.Decorator.SaturdayDecorator;
import com.example.myapplication.Decorator.SundayDecorator;
import com.example.myapplication.Tasks.CalTask;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CalActivity extends AppCompatActivity {

    String time,kcal,menu;
    String device_id;
    TextView waterdaytext;
    TextView f_nick;

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);

        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");

        materialCalendarView = findViewById(R.id.calendarView);
        waterdaytext = findViewById(R.id.ddayText);
        f_nick = findViewById(R.id.f_nick);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );


        try {

            CalTask networkCalTask = new CalTask(handler);

            Map<String, String> params = new HashMap<String, String>();
            params.put("device_id", device_id);

            networkCalTask.execute(params);

        }catch (Exception e){

            Toast.makeText(CalActivity.this, "에러 발생!.", Toast.LENGTH_SHORT).show();

        }

        //String[] result = {"2020-11-7", "2020-11-08", "2020-11-9", "2020-11-10","2020-12-10","2020-12-11","2020-12-11"};
        //new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "-" + Month + "-" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                Toast.makeText(getApplicationContext(), shot_Day, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

            switch (msg.what){

                case -1:
                    Toast.makeText(CalActivity.this, "핸들러에서 에러 발생.", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    WaterDTO water = (WaterDTO) msg.obj;
                    String[] result = water.getWater();

                    f_nick.setText((String)water.getF_nick());

                    if(result.length == 0 || result == null) {
                        waterdaytext.setText("물주기 정보가 없어요!");
                    }
                    else {
                        Arrays.sort(result);
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date begin = formatter.parse(result[result.length-1]);
                            Date end = new Date();
                            long diff = end.getTime() - begin.getTime();
                            long diffDays = diff / (24*60*60*1000);
                            waterdaytext.setText(diffDays + "일 전에 물을 줬어요");
                        } catch (Exception e) {
                            Toast.makeText(CalActivity.this, "물주기 정보 에러", Toast.LENGTH_SHORT).show();
                        }
                    }


                    new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
                    break;

            }

        }
    };


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
            Log.d("ddd", String.valueOf(Time_Result.length));
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (int i = 0; i < Time_Result.length; i++) {
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.GREEN, calendarDays, CalActivity.this));
        }
    }
}