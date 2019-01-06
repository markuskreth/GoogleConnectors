package de.kreth.googleconnectors;

import java.sql.Date;

import com.google.api.services.calendar.model.Event;

public class ClubEvent {

	private String id;
	private String location;
	private String iCalUID;
	private String organizerDisplayName;
	private String caption;
	private String description;
	private Date start;
	private Date end;
	private boolean allDay;

	public String getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getiCalUID() {
		return iCalUID;
	}

	public String getOrganizerDisplayName() {
		return organizerDisplayName;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public boolean isAllDay() {
		return allDay;
	}

	@Override
	public String toString() {
		return "ClubEvent [id=" + id + ", location=" + location + ", iCalUID=" + iCalUID + ", organizerDisplayName="
				+ organizerDisplayName + ", caption=" + caption + ", description=" + description + ", start=" + start
				+ ", end=" + end + ", allDay=" + allDay + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allDay ? 1231 : 1237);
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((iCalUID == null) ? 0 : iCalUID.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((organizerDisplayName == null) ? 0 : organizerDisplayName.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClubEvent other = (ClubEvent) obj;
		if (allDay != other.allDay) {
			return false;
		}
		if (caption == null) {
			if (other.caption != null) {
				return false;
			}
		} else if (!caption.equals(other.caption)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (end == null) {
			if (other.end != null) {
				return false;
			}
		} else if (!end.equals(other.end)) {
			return false;
		}
		if (iCalUID == null) {
			if (other.iCalUID != null) {
				return false;
			}
		} else if (!iCalUID.equals(other.iCalUID)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (organizerDisplayName == null) {
			if (other.organizerDisplayName != null) {
				return false;
			}
		} else if (!organizerDisplayName.equals(other.organizerDisplayName)) {
			return false;
		}
		if (start == null) {
			if (other.start != null) {
				return false;
			}
		} else if (!start.equals(other.start)) {
			return false;
		}
		return true;
	}

	public static ClubEvent parse(Event ev) {
		ClubEvent event = new ClubEvent();
		event.id = ev.getId();
		event.location = ev.getLocation();
		event.iCalUID = ev.getICalUID();
		event.organizerDisplayName = ev.getOrganizer().getDisplayName();
		event.caption = ev.getSummary();
		event.description = ev.getDescription();
		event.start = new Date(ev.getStart().getDateTime().getValue());
		event.end = new Date(ev.getEnd().getDateTime().getValue());
//		event.allDay = ev.
		return event;
	}
}
