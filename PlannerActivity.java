package ie.ncirl.festfriend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlannerActivity extends Activity {
	/** Called when the activity is first created. */

	private FestDbAdapter dbHelper;
	private SimpleCursorAdapter dataAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planner_layout);

		dbHelper = new FestDbAdapter(this);
		dbHelper.open();

		displayListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actionbar_layout, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void displayListView() {

		Cursor cursor = dbHelper.getActsForPlanner("isPlanned");
		// The desired columns to be bound
		String[] columns = new String[] {
				FestDbAdapter.KEY_DAY,
				FestDbAdapter.KEY_STIME,
				FestDbAdapter.KEY_BAND,
				FestDbAdapter.KEY_STAGE,
				FestDbAdapter.KEY_FTIME,											//added
		};

		// the XML defined views which the data will be bound to
		int[] to = new int[] { 
				R.id.day,
				R.id.stime,
				R.id.band,
				R.id.stage,
				R.id.ftime,
		};

		// create the adapter using the cursor pointing to the desired data 
		//as well as the layout information
		dataAdapter = new SimpleCursorAdapter(
				this, R.layout.planner_list_layout, 
				cursor, 
				columns, 
				to,
				1);

		
		ListView listView = (ListView) findViewById(R.id.listView2);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView = (ListView) findViewById(R.id.listView2);

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> listView, View view, 
					int position, long id) {

				// Get the cursor, positioned to the corresponding row in the result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				final String band = cursor.getString(cursor.getColumnIndexOrThrow("band"));

				builder.setTitle("Delete From Planner")
				.setMessage("Delete From Planner")
				.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dbHelper.deleteActFromPlanner(band);
						displayListView();
						
					}
				}).setNegativeButton("No", null).show();
				
				
				String showMe = 
						cursor.getString(cursor.getColumnIndexOrThrow("band"));
				Toast.makeText(getApplicationContext(),
						showMe, Toast.LENGTH_SHORT).show();

			}
		});

		EditText myFilter = (EditText) findViewById(R.id.myFilter2);
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
				return dbHelper.getPlannerActsByName(constraint.toString());
			}
		});

	}
}
