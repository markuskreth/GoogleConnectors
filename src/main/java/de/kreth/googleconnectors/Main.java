package de.kreth.googleconnectors;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.kreth.googleconnectors.calendar.CalendarAdapter;

public class Main {

	public static void main(String[] args) throws ParseException, IOException, SQLException, GeneralSecurityException {
		Options opts = new Options();

		opts.addOption(Option.builder("f").argName("filePath")
				.desc("Path to Settingsfile with database connection settings.").hasArg().required(false).build());
		opts.addOption(Option.builder("host").argName("hostname").desc("host name ").hasArg().required(true).build());

		DefaultParser parser = new DefaultParser();
		CommandLine parsed = parser.parse(opts, args);
		Properties dbProps = new Properties();
		dbProps.load(new FileReader(new File(parsed.getOptionValue("f"))));

		DataSource ds = createDataSource(dbProps);

		CalendarAdapter calendarAdapter = new CalendarAdapter();
		CalendarTaskRefresher refresher = new CalendarTaskRefresher(calendarAdapter, ds);
		refresher.synchronizeCalendarTasks(parsed.getOptionValue("host"));
	}

	public static DataSource createDataSource(Properties dbProps) throws SQLException {
		MysqlDataSource ds = new MysqlDataSource();
		ds.setUrl(dbProps.getProperty("url"));
		ds.setUser(dbProps.getProperty("username"));
		ds.setPassword(dbProps.getProperty("password"));
		ds.setPrepStmtCacheSize(5);
		ds.setCharacterEncoding("utf8");
		ds.setServerTimezone("Europe/Berlin");
		return ds;
	}

}
