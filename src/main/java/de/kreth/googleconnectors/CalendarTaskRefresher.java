package de.kreth.googleconnectors;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.googleconnectors.calendar.CalendarAdapter;

public class CalendarTaskRefresher {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String UPDATE_SQL = "UPDATE clubevent SET `location`=?, `iCalUID`=?, `organizerDisplayName`=?, `caption`=?, `description`=?, `start`=?, `end`=?, `allDay`=? WHERE (`id`=?)";

	private static final String INSERT_SQL = "INSERT INTO `clubevent` (`id`, `location`, `iCalUID`, `organizerDisplayName`, `caption`, `description`, `start`, `end`, `allDay`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SELECT_SQL = "SELECT id, deleted FROM clubevent where id=?";

	private static final String SELECT_DELETED_SQL = "SELECT id, organizerDisplayName, caption FROM clubevent where deleted=true";

	private final CalendarAdapter calendarAdapter;

	private final DataSource dataSource;

	public CalendarTaskRefresher(CalendarAdapter calendarAdapter, DataSource dataSource) {
		this.calendarAdapter = calendarAdapter;
		this.dataSource = dataSource;
	}

	public void synchronizeCalendarTasks(String hostname) throws SQLException {

		try (Connection conn = dataSource.getConnection()) {

			try (final PreparedStatement insert = conn.prepareStatement(INSERT_SQL);
					final PreparedStatement update = conn.prepareStatement(UPDATE_SQL);
					final PreparedStatement select = conn.prepareStatement(SELECT_SQL);
					final PreparedStatement selectDeleted = conn.prepareStatement(SELECT_DELETED_SQL);) {

				List<ClubEvent> list = loadEventsFromGoogle(hostname);
				log.debug("Found these events: {}", list);

				List<String> deleted = new ArrayList<>();

				for (ClubEvent e : list) {
					select.setString(1, e.getId());
					try (ResultSet rs = select.executeQuery()) {
						if (rs.next()) {
							update(update, e);
							log.debug("successfully updated {}", e);
							if (rs.getBoolean("deleted")) {
								try {
									delete(hostname, deleted, e);
								}
								catch (IOException e1) {
									log.error("Goolge delete failed for {}", e);
								}
							}
						}
						else {
							try {
								insert(insert, e);
								log.debug("successfully inserted {}", e);
							}
							catch (SQLException ex) {
								log.warn("Insert failed, updating {}", e, ex);
								update(update, e);
							}
						}
					}
				}

				try (ResultSet rs = selectDeleted.executeQuery()) {
					while (rs.next()) {
						String id = rs.getString("id");
						if (!deleted.contains(id)) {
							String organizerDisplayName = rs.getString("organizerDisplayName");
							String caption = rs.getString("caption");
							try {
								calendarAdapter.deleteEvent(hostname, organizerDisplayName, id);
								log.info("Successfully deleted {}, {}, {} online on google", organizerDisplayName,
										caption, id);
							}
							catch (IOException e1) {
								log.error("Konnte nicht löschen: {}, {}, {}", organizerDisplayName, caption, id, e1);
							}
						}
					}
				}
			}
		}

	}

	private void delete(String hostname, List<String> deleted, ClubEvent e) throws IOException {
		String organizerDisplayName = e.getOrganizerDisplayName();
		String id = e.getId();
		calendarAdapter.deleteEvent(hostname, organizerDisplayName, id);
		deleted.add(id);
		log.info("Successfully deleted {} online on google", e);
	}

	public void insert(final PreparedStatement insert, ClubEvent e) throws SQLException {
		log.trace("try inserting {}", e);
		insert.setString(1, e.getId());
		insert.setString(2, e.getLocation());
		insert.setString(3, e.getiCalUID());
		insert.setString(4, e.getOrganizerDisplayName());
		insert.setString(5, e.getCaption());
		insert.setString(6, e.getDescription());
		insert.setDate(7, e.getStart());
		insert.setDate(8, e.getEnd());
		insert.setBoolean(9, e.isAllDay());
		insert.execute();
	}

	public void update(final PreparedStatement update, ClubEvent e) throws SQLException {
		log.trace("try updating {}", e);
		update.setString(1, e.getLocation());
		update.setString(2, e.getiCalUID());
		update.setString(3, e.getOrganizerDisplayName());
		update.setString(4, e.getCaption());
		update.setString(5, e.getDescription());
		update.setDate(6, e.getStart());
		update.setDate(7, e.getEnd());
		update.setBoolean(8, e.isAllDay());
		update.setString(9, e.getId());
		update.execute();
	}

	public List<ClubEvent> loadEventsFromGoogle(String remoteHost) {

		log.info("Loading events from Google Calendar");

		List<ClubEvent> list = new ArrayList<>();

		try {

			List<com.google.api.services.calendar.model.Event> events = calendarAdapter.getAllEvents(remoteHost);

			for (com.google.api.services.calendar.model.Event ev : events) {

				if ("cancelled".equals(ev.getStatus())) {
					log.debug("Cancelled: {}", ev.getSummary());
				}
				else {
					list.add(ClubEvent.parse(ev));
				}
			}

		}
		catch (IOException e) {
			log.error("Error loading events from google.", e);
		}
		return list;
	}

}
