package ie.ncirl.festfriend;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
 
public class AndroidDashboardDesignActivity extends Activity {
	

	
	
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
         
        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_fri = (Button) findViewById(R.id.btn_fri);
         
        // Dashboard Friends button
        Button btn_planner = (Button) findViewById(R.id.btn_planner);
         
        // Dashboard Messages button
        Button btn_sat = (Button) findViewById(R.id.btn_sat);
         
        // Dashboard Places button
        Button btn_location = (Button) findViewById(R.id.btn_location);
         
        // Dashboard Events button
        Button btn_sun = (Button) findViewById(R.id.btn_sun);
         
        // Dashboard Photos button
        Button btn_weather = (Button) findViewById(R.id.btn_weather);
         
        /**
         * Handling all button click events
         * */
         
        // Listening to News Feed button click
        btn_fri.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), FriActivity.class);
                startActivity(i);
            }
        });
         
       // Listening Friends button click
        btn_planner.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), PlannerActivity.class);
                startActivity(i);
            }
        });
         
        // Listening Messages button click
        btn_sat.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), SatActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Places button click
        btn_location.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
            }
        });
         
        // Listening to Events button click
        btn_sun.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), SunActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Photos button click
        btn_weather.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching News Feed Screen
                Intent i = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(i);
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_layout, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_search:
            // search action
            return true;
     
        
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
 
}
    
    
