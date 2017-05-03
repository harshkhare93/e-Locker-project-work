package harshkhare.e_locker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;

import io.blackbox_vision.materialcalendarview.view.CalendarView;

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DAY = "harshkhare.e_locker.day";
    public static final String MONTH = "harshkhare.e_locker.month";
    public static final String YEAR = "harshkhare.e_locker.year";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        final Button btnContinue = (Button) findViewById(R.id.btnContinue);
        final CalendarView docdate = (CalendarView) findViewById(R.id.docdate);
        btnContinue.setOnClickListener(this);
        docdate.setOnDateClickListener(new CalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(@NonNull Date date) {
                Date currentDate = new Date(System.currentTimeMillis());
                if (date.before(currentDate)) {
                    moveToNext(date);
                } else {
                    Toast.makeText(CalenderActivity.this, "Select a next date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void moveToNext(Date selectedDate) {
        Intent intent = new Intent(this, Notificationsetting.class);
        intent.putExtra("nextdate","Date_var_here");
        intent.putExtra(DAY, selectedDate.getDate());
        intent.putExtra(MONTH, selectedDate.getMonth()+1);
        intent.putExtra(YEAR, selectedDate.getYear()+1900);
        CalenderActivity.this.startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        Intent noti=new Intent(CalenderActivity.this,Notificationsetting.class);
        startActivity(noti);

    }
}
