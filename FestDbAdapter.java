package ie.ncirl.festfriend;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("NewApi")
public class FestDbAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_BAND = "band";
	public static final String KEY_DAY = "day";
	public static final String KEY_STAGE = "stage";
	public static final String KEY_STIME = "stime";
	public static final String KEY_FTIME = "ftime";
	public static final String KEY_PLANNER = "planner";								//added

	private static final String TAG = "CountriesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "World";
	private static final String SQLITE_TABLE = "Country";
	private static final String PLANNER_TABLE = "Planner";
	private static final int DATABASE_VERSION = 4;

	private final Context mCtx;

	private static final String DATABASE_CREATE =
			"CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
					KEY_ROWID + " integer PRIMARY KEY autoincrement," +
					KEY_BAND + "," +
					KEY_DAY + "," +
					KEY_STAGE + "," +
					KEY_STIME + "," +
					KEY_FTIME + "," +
					KEY_PLANNER + "," +																//added
					" UNIQUE (" + KEY_BAND +"));";
	private static final String DATABASE_CREATE_PLAN = 
			"CREATE TABLE if not exists " + PLANNER_TABLE + " (" +
					KEY_ROWID + " integer PRIMARY KEY autoincrement," +
					KEY_BAND + "," +
					KEY_DAY + "," +
					KEY_STAGE + "," +
					KEY_STIME + "," +
					KEY_FTIME + "," +
					KEY_PLANNER + "," +																//added
					" UNIQUE (" + KEY_BAND +"));";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}


		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE);
			Log.w(TAG, DATABASE_CREATE_PLAN);
			db.execSQL(DATABASE_CREATE_PLAN);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PLANNER_TABLE);
			onCreate(db);
		}
	}
	

	public FestDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public FestDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (mDbHelper != null) {
			mDbHelper.close();
		}
	}

	public long addAct(String band, String day, 
			String stage, String stime, String ftime, String planner) {					//added

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BAND, band);
		initialValues.put(KEY_DAY, day);
		initialValues.put(KEY_STAGE, stage);
		initialValues.put(KEY_STIME, stime);
		initialValues.put(KEY_FTIME, ftime);
		initialValues.put(KEY_PLANNER, planner);										//added

		return mDb.insert(SQLITE_TABLE, null, initialValues);
	}
	
	public long addToPlanner(String band, String day, String stage,
			String stime, String ftime, String planner){
		
		ContentValues plan = new ContentValues();
		plan.put(KEY_BAND, band);
		plan.put(KEY_DAY, day);
		plan.put(KEY_STAGE, stage);
		plan.put(KEY_STIME, stime);
		plan.put(KEY_FTIME, ftime);
		plan.put(KEY_PLANNER, planner);
		
		return mDb.insert(PLANNER_TABLE, null, plan);
	}
	
	public boolean deleteAllActs() {

		int doneDelete = 0;
		doneDelete = mDb.delete(SQLITE_TABLE, null , null);
		Log.w(TAG, Integer.toString(doneDelete));
		return doneDelete > 0;

	}
	
	public Cursor getActsForPlanner(String inputText) throws SQLException {

		Cursor mCursor = mDb.query(PLANNER_TABLE, new String[] {KEY_ROWID,
				KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},		//added 
				null, null, null, null, KEY_DAY + "," + KEY_STIME, null);									//added

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public Cursor getPlannerActsByName(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null  ||  inputText.length () == 0)  {
			mCursor = mDb.query(PLANNER_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					null, null, null, null, null, null);								//added

		}
		else {
			mCursor = mDb.query(true, PLANNER_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					KEY_BAND + " like '%" + inputText + "%'", null,
					null, null, null, null, null);										//added
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public void deleteActFromPlanner(String position){
		
		mDb.delete(PLANNER_TABLE, KEY_BAND + "= '" + position + "' ", null);
		
	}

 	public Cursor fetchActsByDay(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null  ||  inputText.length () == 0)  {
			mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					null, null, null, null, null, null);								//added

		}
		else {
			mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					KEY_DAY + " like '%" + inputText + "%'", null,
					null, null, null, null, null);										//added
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public Cursor fetchActsByName(String inputText) throws SQLException {
		Log.w(TAG, inputText);
		Cursor mCursor = null;
		if (inputText == null  ||  inputText.length () == 0)  {
			mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					null, null, null, null, null, null);								//added

		}
		else {
			mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
					KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},	//added 
					KEY_BAND + " like '%" + inputText + "%'", null,
					null, null, null, null, null);										//added
		}
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public Cursor fetchAllActs() {

		Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
				KEY_BAND, KEY_DAY, KEY_STAGE, KEY_STIME, KEY_FTIME, KEY_PLANNER},		//added 
				null, null, null, null, null, null);									//added

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public void insertSomeActs() {

		addAct("Fatboy Slim", "Friday", "Main", "22.30", "23.55","");        
		addAct("My Bloody Valentine", "Friday", "Main", "21.00", "22.00",""); 
		addAct("Wu-Tang Clan", "Friday", "Main", "19.15", "20.15",""); 
		addAct("Miles Kane", "Friday", "Main", "18.00", "18.45",""); 
		addAct("Hudson Taylor", "Friday", "Main", "17.00", "17.30",""); 
		addAct("Disclosure", "Saturday", "Main", "00.00", "01.45",""); 
		addAct("Two Door Cinema Club", "Saturday", "Main", "22.30", "23.55",""); 
		addAct("Bjork", "Saturday", "Main", "21.00", "22.10","Add"); 
		addAct("Robert Plant presents Sensational Space Shifters", "Saturday", "Main", "19.30", "20.15",""); 
		addAct("Ellie Goulding", "Saturday", "Main", "17.30", "18.15","Add"); 
		addAct("Duckworth Lewis MethodFatboy Slim", "Saturday", "Main", "16.00", "16.50",""); 
		addAct("Ocean Colour Sscene", "Saturday", "Main", "14.30", "15.30",""); 
		addAct("The Beat", "Saturday", "Main", "13.00", "13.50",""); 
		addAct("Arctic Monkeys", "Sunday", "Main", "10.15", "23.40",""); 
		addAct("Franz Ferdinand", "Sunday", "Main", "20.45", "21.45",""); 
		addAct("EELS", "Sunday", "Main", "19.15", "20.15","");        
		addAct("Kodaline", "Sunday", "Main", "18.00", "18.45",""); 
		addAct("Noah & The Whale", "Sunday", "Main", "16.30", "17.15",""); 
		addAct("Black Uhuru", "Sunday", "Main", "14.45", "15.30",""); 
		addAct("Dublin Gospel Choir", "Sunday", "Main", "13.00", "14.00",""); 
		addAct("Ghostboy", "Friday", "Electric Arena", "21.00", "22.15",""); 
		addAct("Giorgio Moroder", "Friday", "Electric Arena", "22.30", "11.55",""); 
		addAct("Raglans", "Saturday", "Electric Arena", "13.30", "14.15",""); 
		addAct("Sam Smith", "Saturday", "Electric Arena", "14.45", "15.30",""); 
		addAct("Hurts", "Saturday", "Electric Arena", "16.00", "17.00",""); 
		addAct("Cyril Hahn", "Saturday", "Electric Arena", "17.30", "18.30",""); 
		addAct("Little Green Cars", "Saturday", "Electric Arena", "19.00", "19.45",""); 
		addAct("The Walkmen", "Saturday", "Electric Arena", "20.15", "21.00",""); 
		addAct("Billy Bragg", "Saturday", "Electric Arena", "21.30", "22.30",""); 
		addAct("Ryan Sheridan", "Saturday", "Electric Arena", "23.00", "00.15",""); 
		addAct("Tiga", "Saturday", "Electric Arena", "00.30", "02.00","");        
		addAct("Nina Nesbitt", "Sunday", "Electric Arena", "12.30", "13.30",""); 
		addAct("MS MR", "Sunday", "Electric Arena", "13.45", "14.30",""); 
		addAct("Hermitage Green", "Sunday", "Electric Arena", "15.00", "15.45",""); 
		addAct("The Strypes", "Sunday", "Electric Arena", "16.15", "17.00",""); 
		addAct("Johnny Marr", "Sunday", "Electric Arena", "17.30", "18.30",""); 
		addAct("Warpaint", "Sunday", "Electric Arena", "19.00", "20.00",""); 
		addAct("David Byrne & St. Vincent", "Sunday", "Electric Arena", "20.30", "22.00",""); 
		addAct("The Knife", "Sunday", "Electric Arena", "22.30", "23.45",""); 

	}

}
