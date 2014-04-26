package ie.ncirl.festfriend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SunActivity extends Activity {

	private FestDbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sun_layout);


		dbHelper = new FestDbAdapter(this);
		dbHelper.open();

		//Clean all data
		dbHelper.deleteAllActs();
		//Add some data
		dbHelper.insertSomeActs();

		//Generate ListView from SQLite Database
		displayListView();
		displayListView();

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_layout, menu);

		return super.onCreateOptionsMenu(menu);
	}


	private void displayListView() {

		Cursor cursor = dbHelper.fetchActsByDay("Sunday");

		// The desired columns to be bound
		String[] columns = new String[] {
				FestDbAdapter.KEY_BAND,
				FestDbAdapter.KEY_STAGE,
				FestDbAdapter.KEY_STIME,
				FestDbAdapter.KEY_FTIME
		};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { 
				R.id.code,
				R.id.name,
				R.id.continent,
				R.id.region,
		};

		// create the adapter using the cursor pointing to the desired data 
		//as well as the layout information
		dataAdapter = new SimpleCursorAdapter(
				this, R.layout.list_layout, 
				cursor, 
				columns, 
				to,
				1);

		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view, 
					int position, long id) {

				// Get the cursor, positioned to the corresponding row in the result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);

				final String band = cursor.getString(cursor.getColumnIndexOrThrow("band"));
				final String day = cursor.getString(cursor.getColumnIndexOrThrow("day"));
				final String stage = cursor.getString(cursor.getColumnIndexOrThrow("stage"));
				final String stime = cursor.getString(cursor.getColumnIndexOrThrow("stime"));
				final String ftime = cursor.getString(cursor.getColumnIndexOrThrow("ftime"));
				final String planner = cursor.getString(cursor.getColumnIndexOrThrow("planner"));

				builder.setTitle("Add To Planner")
				.setMessage("Add To Planner")
				.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dbHelper.addToPlanner(band, day, stage, stime, ftime, planner);
					}
				}).setNegativeButton("No", null).show();


				String showBand = 
						cursor.getString(cursor.getColumnIndexOrThrow("band"));

				Toast.makeText(getApplicationContext(),
						showBand, Toast.LENGTH_SHORT).show();

			}
		});

		EditText myFilter = (EditText) findViewById(R.id.myFilter);
		myFilter.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, 
					int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, 
					int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		});

		dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return dbHelper.fetchActsByName(constraint.toString());
			}
		});

	}
}









