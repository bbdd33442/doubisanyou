
package com.doubisanyou.baseproject.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * <p>
 * 每个子类对应于sqlite数据库中的一个表,所有子类成员变量均会自动保存在表中.
 * 
 * 可根据成员变量生成sqlite简单表结构.
 * </p>
 * <p>
 * 表：<br/>
 * 在保存数据时，对应每个不同的类，会创建一张数据库表.<br/>
 * 使用{@link Table}标签可以定义表名和表的版本.如果无 {@link Table}标签，表名默认为类名.<br/>
 * </p>
 * <p>
 * 列：<br/>
 * 如上方所述，在保数据时，会创建一张表。而表的每一列，对应于类的每一个成员变量.<br/>
 * 使用{@link Column}标签可定义类的成员变量所对应的列名，及是否为主键。如果不使用{@link Column}
 * 标签，列名默认为变量名，对应列非主键.<br/>
 * <em>成员变量类型只能是 String , int,long , double , 分别对应sqlite中的 TEXT , INTEGER ,INTEGER , REAL</em>
 * </p>
 * 
 * <p>
 * 保存数据：<br/>
 * 使用{@link #writeToDB(SQLiteDatabase)}、
 * {@link #writeToDB(SQLiteDatabase, boolean)}函数将对象中的值保存至数据库.
 * </p>
 * <p>
 * 读助数据：<br/>
 * 设置主键的值后，可使用 {@link #query(SQLiteDatabase)}函数以所有主键为查询条件进行查询，并填充值。<br/>
 * 使用 {@link #query(SQLiteDatabase, String, String[])}传入sql查询条件进行简单的单表查询.<br/>
 * 使用 {@link #queryList(Class, SQLiteDatabase, String, String[])}
 * 传入sql查询条件进行简单的单表查询，返回多条数据.<br/>
 * 复杂的查询，请使用android自带的查询函数得到cursor，并使用 {@link #readData(Cursor)}函数将数据读助至对象.
 * </P>
 * 
 * <p>
 * 表版本:<br/>
 * 使用{@link DBBean}创建的表都会有一个版本.表版本默认为0。如果表结构有更改，需要用{@link Table#version()}
 * 来指定新的版本。当检测当版本号更改时，会调用{@link #onUpgrade(SQLiteDatabase, int, int)}
 * 函数。如果想保存旧表中的数据，你需要在子类中重写此函数。
 * </p>
 * 
 * @author LiuJiane
 * 
 */
public abstract class DBBean {

	public static final String TAG = "DBItem";

	// 类型对应表
	private static Map<Class<?>, String> typeMap = new HashMap<Class<?>, String>();

	// sqlite 关键字列表
	private static final String[] SQLITE_KEYWORDS = { "ABORT", "ACTION",
			"ADD", "AFTER", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
			"ATTACH", "AUTOINCREMENT", "BEFORE", "BEGIN", "BETWEEN", "BY",
			"CASCADE", "CASE", "CAST", "CHECK", "COLLATE", "COLUMN", "COMMIT",
			"CONFLICT", "CONSTRAINT", "CREATE", "CROSS", "CURRENT_DATE",
			"CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE", "DEFAULT",
			"DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DETACH", "DISTINCT",
			"DROP", "EACH", "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE",
			"EXISTS", "EXPLAIN", "FAIL", "FOR", "FOREIGN", "FROM", "FULL",
			"GLOB", "GROUP", "HAVING", "IF", "IGNORE", "IMMEDIATE", "IN",
			"INDEX", "INDEXED", "INITIALLY", "INNER", "INSERT", "INSTEAD",
			"INTERSECT", "INTO", "IS", "ISNULL", "JOIN", "KEY", "LEFT", "LIKE",
			"LIMIT", "MATCH", "NATURAL", "NO", "NOT", "NOTNULL", "NULL", "OF",
			"OFFSET", "ON", "OR", "ORDER", "OUTER", "PLAN", "PRAGMA",
			"PRIMARY", "QUERY", "RAISE", "REFERENCES", "REGEXP", "REINDEX",
			"RELEASE", "RENAME", "REPLACE", "RESTRICT", "RIGHT", "ROLLBACK",
			"ROW", "SAVEPOINT", "SELECT", "SET", "TABLE", "TEMP", "TEMPORARY",
			"THEN", "TO", "TRANSACTION", "TRIGGER", "UNION", "UNIQUE",
			"UPDATE", "USING", "VACUUM", "VALUES", "VIEW", "VIRTUAL", "WHEN",
			"WHERE" };

	static {
		// java类型与sqlite类型对应MAP
		typeMap.put(int.class, "INTEGER");
		typeMap.put(long.class, "INTEGER");
		typeMap.put(double.class, "REAL");
		typeMap.put(String.class, "TEXT");

		// sqlite关键字排序
		Arrays.sort(SQLITE_KEYWORDS);
	}

	// 已检查类型
	private static Set<Class<? extends DBBean>> CHECKED_CLASS = new HashSet<Class<? extends DBBean>>();

	/**
	 * 检查子类是否符合条件,条件见{@link DBBean}说明.
	 */
	public DBBean() {
		// 检查类型
		if (!CHECKED_CLASS.contains(getClass())) {

			Field[] fields = getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				// 跳过静态变量
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers)) {
					continue;
				}

				// 检查类成员是否为支持类型
				if (typeMap.get(field.getType()) == null
						&& field.getType() != List.class) {
					throw new IllegalStateException(field.getName()
							+ "is the not surpot data type: " + field.getType());
				}

				// 检查成员变量名是否为sqlite关键字
				if (Arrays.binarySearch(SQLITE_KEYWORDS, getColumnName(field)
						.toUpperCase()) >= 0) {
					throw new IllegalStateException(
							"column name can't be a sqlite keyword: "
									+ getColumnName(field));
				}
			}

			CHECKED_CLASS.add(getClass());
		}// end 检查类型

	}

	/**
	 * 删除所有数据 *
	 * 
	 * @param db
	 *            要从中删除数据的{@link SQLiteDatabase}对象
	 * @param beanType
	 *            要删除数据之{@link DBBean}子类
	 */
	public static void deleteAll(SQLiteDatabase db,
			Class<? extends DBBean> beanType) {
		try {
			db.delete(beanType.newInstance().getTableName(), null, null);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据主键值删除数据库中的记录
	 * 
	 * @param db
	 */
	public void delete(SQLiteDatabase db) {
		Object[] selectionAndArgs = keyWordsSelectionAndArgs();
		if (selectionAndArgs != null) {
			String selection = (String) selectionAndArgs[0];
			String[] args = (String[]) selectionAndArgs[1];
			db.delete(getTableName(), selection, args);
		}
	}

	/**
	 * 查询多条数据，从单个表（也就是单个{@link DBBean}子类)，要据条件取回多条数据
	 * 
	 * @param beanType
	 *            {@link DBBean}子类，对应一张数据库表
	 * @param db
	 *            数据库
	 * @param selection
	 *            查询条件 ,对应sql中的WHERE ，null表查询全部. e.g. "id=?"
	 * @param args
	 *            查询参数,每个对应selections参数中的一个'?'.
	 * @return 查询结果
	 */
	@SuppressWarnings("rawtypes")
	public static List queryList(Class<? extends DBBean> beanType,
			SQLiteDatabase db, String selection, String[] args) {
		List<DBBean> ret = new ArrayList<DBBean>();
		Cursor cursor = null;
		try {
			beanType.newInstance().createTableIfNeed(db);
			cursor = db.query(beanType.newInstance().getTableName(), null,
					selection, args, null, null, null);
			while (cursor.moveToNext()) {
				DBBean info = beanType.newInstance();
				info.readData(cursor);
				ret.add(info);
			}
		} catch (InstantiationException e) {
			// will not happen
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// will not happen
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ret;
	}

	// 查询主键对应记录时的selection 和 args
	// index 0: selection index 1:args
	// 如果没有主键，返回null
	private Object[] keyWordsSelectionAndArgs() {
		StringBuilder selection = new StringBuilder();
		List<String> argList = new ArrayList<String>();
		String[] args = null;
		List<Field> primaryKeys = getPrimaryKeys();
		if (primaryKeys.size() > 0) {
			for (int i = 0; i < primaryKeys.size(); i++) {
				if (i != 0) {
					selection.append(" AND ");
				}
				selection.append(' ');
				selection.append(getColumnName(primaryKeys.get(i)));
				selection.append("=? ");
				try {
					Object value = primaryKeys.get(i).get(this);
					String arg = (value == null) ? null : value.toString();
					argList.add(arg);
				} catch (Exception e) {
				}
			}
			args = new String[argList.size()];
			argList.toArray(args);
		} else {
			return null;
		}
		return new Object[] { selection.toString(), args };
	}

	/**
	 * 根据设置的主键值进行查询,查询到的值填充到对象中
	 * 
	 * @param db
	 *            数据库
	 * @return 查询是否成功
	 */
	public boolean query(SQLiteDatabase db) {
		createTableIfNeed(db);
		boolean suc = false;
		Cursor queryCursor = null;
		Object[] selectionAndArgs = keyWordsSelectionAndArgs();
		if (selectionAndArgs == null) {
			return false;
		}
		String selection = (String) selectionAndArgs[0];
		String[] args = (String[]) selectionAndArgs[1];
		try {
			queryCursor = db.query(getTableName(), null, selection.toString(),
					args, null, null, null);
			if (queryCursor.moveToFirst()) {
				readData(queryCursor);
				suc = true;
			}
		} finally {
			if (queryCursor != null) {
				queryCursor.close();
			}
		}
		return suc;
	}

	/**
	 * 查询单条数据，从类对应的表中查询，查询到的值填充到对象中
	 * 
	 * @param db
	 *            数据库
	 * @param selection
	 *            查询条件 ,对应sql中的WHERE ，为null时查询全部. e.g. "id=?"
	 * @param args
	 *            查询参数,每个对应selections参数中的一个'?'.
	 * @return 如果没查询到返回false,查询到返回true
	 */
	public boolean query(SQLiteDatabase db, String selection, String[] args) {
		createTableIfNeed(db);
		boolean ret = false;
		// 取对应id的所有列
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), null, selection, args, null,
					null, null);
			if (cursor.moveToFirst()) {
				readData(cursor);
				ret = true;
			} else {
				ret = false;
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return ret;
	}

	/**
	 * 保存到Sqlite数据库中,如果已有主键值相同记录，则更新记录，不然插入新记录. 如果没建表的话会自动建表
	 * 
	 * @param db
	 *            要保存表在的{@link SQLiteDatabase} 对象
	 * @return 是否已有数据
	 */
	public boolean writeToDB(SQLiteDatabase db) {
		return writeToDB(db, true);
	}

	/**
	 * 保存到Sqlite数据库中
	 * 
	 * @param db
	 *            要保存表在的{@link SQLiteDatabase} 对象
	 * @param update
	 *            如果已有主键值相同之记录，是否更新已有数据
	 * @return 是否已有数据
	 */
	public boolean writeToDB(SQLiteDatabase db, boolean update) {
		createTableIfNeed(db);
		boolean ret;
		// 事务开始
		// db.beginTransaction();
		ContentValues cv = new ContentValues();
		Field[] fields = getClass().getDeclaredFields();
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				String fieldName = getColumnName(field);

				// 跳过静态变量
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers)) {
					continue;
				}

				Type type = field.getType();

				if (type == long.class) {

					// long型：INTEGER

					cv.put(fieldName, field.getLong(this));

				} else if (type == int.class) {

					// int : INTEGER
					cv.put(fieldName, field.getInt(this));
				} else if (type == double.class) {

					// double : REAL
					cv.put(fieldName, field.getDouble(this));
				} else if (type == String.class) {

					// String : TEXT
					cv.put(fieldName, ((String) field.get(this)));
				} else if (type == List.class) {

				} else {
					throw new IllegalStateException("not surpot data type: "
							+ field.getType());
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		ret = false;
		Cursor cursor = null;
		Object[] selectionAndArgs = keyWordsSelectionAndArgs();
		String selection = null;
		String[] args = null;
		if (selectionAndArgs != null) {
			selection = (String) selectionAndArgs[0];
			args = (String[]) selectionAndArgs[1];
			try {
				cursor = db.query(getTableName(), null, selection.toString(),
						args, null, null, null);
				if (cursor.getCount() > 0) {
					ret = true;
				}
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		boolean saved = false;
		if (ret) {
			if (update) {
				db.update(getTableName(), cv, selection.toString(), args);
				Log.i(TAG, "update :" + this.toString());
				saved = true;
			}
		} else {
			db.insertOrThrow(getTableName(), null, cv);
			saved = true;
			Log.i(TAG, "insert :" + this.toString());
		}
		return saved;
	}

	/**
	 * 从cursor 中取数据填充到对象中
	 * 
	 * @param cursor
	 *            要从中取数据的{@link Cursor}对象
	 * @param tableName
	 *            表名/代表名,为null时表不使用表名
	 */
	public void readData(Cursor cursor, String tableName) {

		Field[] fields = getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			// 跳过静态变量
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers)) {
				continue;
			}
			String colName;
			if (tableName != null && tableName.length() > 0) {
				colName = tableName + "." + getColumnName(field);
			} else {
				colName = getColumnName(field);
			}
			int columnIndex = cursor.getColumnIndex(colName);
			// 判断在cursor中是否有对应名字的列
			if (columnIndex >= 0) {
				Type type = field.getType();
				try {
					if (type == long.class) {

						// long型：INTEGER
						field.setLong(this, cursor.getLong(columnIndex));

					} else if (type == int.class) {

						// int : INTEGER
						field.setInt(this, cursor.getInt(columnIndex));
					} else if (type == double.class) {

						// double : REAL
						field.setDouble(this, cursor.getDouble(columnIndex));
					} else if (type == String.class) {

						// String : TEXT
						field.set(this, cursor.getString(columnIndex));
					} else if (type == List.class) {

					} else {
						throw new IllegalStateException(
								"not surpot data type: " + field.getType());
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从cursor 中取数据填充到对象中
	 * 
	 * @param cursor
	 *            要从中取数据的{@link Cursor}对象
	 */
	public void readData(Cursor cursor) {
		readData(cursor, null);
	}

	public void dropTable(SQLiteDatabase db) {
		String sql = "DROP TABLE " + getTableName();
		try {
			db.execSQL(sql);
		} catch (Exception e) {
		}
	}

	/**
	 * 如果数据库中的版本号与类定义的不一致，会回调此函数 默认实现为调用{@link DBBean#dropTable(SQLiteDatabase)}
	 * 及{@link DBBean#createTableIfNeed(SQLiteDatabase)}
	 * 
	 * @param db
	 *            数据库
	 * @param version_in_db
	 *            数据库中的版本号
	 * @param version_in_class
	 *            {@link DBBean}子类的{@link Table#version()}
	 * */
	protected void onUpgrade(SQLiteDatabase db, int version_in_db,
			int version_in_class) {
		Log.i(TAG, "onUpgrade");
		dropTable(db);
		createTableIfNeed(db);
		// TODO 如果旧表中的列名与新表中有对应，copy之
	}

	/** 建表，表名为类名 */
	protected void createTableIfNeed(SQLiteDatabase db) {
		if (isTableExists(db)) {
			return;
		}
		// 建表sql语句
		StringBuilder createTableSql = new StringBuilder();
		createTableSql.append("CREATE TABLE IF NOT EXISTS ");
		createTableSql.append(getTableName());
		// 设置_ID为主键
		Field[] fields = getClass().getDeclaredFields();
		createTableSql.append('(');
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			// 跳过静态变量
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) || field.getType() == List.class) {
				continue;
			}
			if (i != 0) {
				createTableSql.append(',');
			}
			// 成员变量名对应表的列
			createTableSql.append(getColumnName(field));
			createTableSql.append(' ');
			String sqliteType = typeMap.get(field.getType());

			if (sqliteType == null) {
				throw new IllegalStateException("not surpot data type: "
						+ field.getType());
			}
			createTableSql.append(sqliteType);

			if (isPrimaryKeys(field)) {
				createTableSql.append(" PRIMARY KEY");
			}
		}

		createTableSql.append(")");
		// 执行
		Log.i(TAG, createTableSql.toString());
		db.execSQL(createTableSql.toString());

		// ----- 在DBBeanMaster表中记录 ----
		if (getClass() != DBBeanMaster.class) {
			DBBeanMaster master = new DBBeanMaster();
			master.name = getTableName();
			Table table = getClass().getAnnotation(Table.class);
			master.version = table != null ? table.version() : 0;
			master.writeToDB(db);
		}
	}

	private boolean isPrimaryKeys(Field field) {
		Column column = field.getAnnotation(Column.class);
		return (column != null && column.primaryKey());
	}

	// 子类与主键列表的对应
	private static Map<Class<? extends DBBean>, List<Field>> PRIMAY_KEY_MAP = new HashMap<Class<? extends DBBean>, List<Field>>();

	/** 主键 列表 不会为空 */
	private List<Field> getPrimaryKeys() {
		List<Field> primaryKeys = PRIMAY_KEY_MAP.get(getClass());
		if (null == primaryKeys) {
			primaryKeys = new ArrayList<Field>();
			for (Field field : getClass().getFields()) {
				if (isPrimaryKeys(field)) {
					primaryKeys.add(field);
				}
			}
			PRIMAY_KEY_MAP.put(getClass(), primaryKeys);
		}
		return primaryKeys;
	}

	/** 返回与成员变量对应的列名 */
	protected String getColumnName(Field field) {
		Column column = field.getAnnotation(Column.class);
		String columnName;
		if (column != null && column.name() != null
				&& column.name().length() > 0) {
			columnName = column.name();
		} else {
			columnName = field.getName();
		}
		return columnName;
	}

	/** 返回表名 */
	protected String getTableName() {
		Table table = this.getClass().getAnnotation(Table.class);
		String tableName;
		if (table != null && table.name() != null && table.name().length() > 0) {
			tableName = table.name();
		} else {
			tableName = getClass().getSimpleName();
		}
		return tableName;
	}

	// 已确认OK的表
	private static Set<String> EXISTS_TABLE_SET = new HashSet<String>();

	/**
	 * 指定表名的表，于数据库中是否存在. 此处会检查数据库的版本，如果已存在数据库中表的版本号，与本类{@link Table#version()}
	 * 中的值不一致 ，会调用{@link #onUpgrade(int, int)}
	 * 
	 * @param db
	 *            数据库
	 * @return 是否存在对应表
	 */
	protected boolean isTableExists(SQLiteDatabase db) {
		String tableName = getTableName();
		if (EXISTS_TABLE_SET.contains(tableName)) {
			return true;
		}
		Cursor cursor = null;
		Cursor master_cursor = null;
		boolean ret = false;
		try {
			String sql = "PRAGMA table_info(" + tableName + ")";
			cursor = db.rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				if (getClass() != DBBeanMaster.class) {
					String[] args = { tableName };
					DBBeanMaster master = new DBBeanMaster();
					if (master.query(db, "name=?", args)) {
						int version_in_db = master.version;
						Table table = getClass().getAnnotation(Table.class);
						int version_in_class = table != null ? table.version()
								: 0;
						if (version_in_class != version_in_db) {
							onUpgrade(db, version_in_db, version_in_class);
						}
					}
				}
				EXISTS_TABLE_SET.add(tableName);
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (master_cursor != null) {
				master_cursor.close();
			}
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (Field field : getClass().getDeclaredFields()) {
			sb.append('{');
			field.setAccessible(true);
			sb.append(field.getName());
			sb.append(':');
			try {
				sb.append(field.get(this));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			sb.append('}');
		}
		sb.append('}');
		return sb.toString();
	}
}
