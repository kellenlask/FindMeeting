/**
 * Created by Kellen on 3/4/2015.
 */
public enum Day {
	SUNDAY (0),
	MONDAY (1),
	TUESDAY (2),
	WEDNESDAY (3),
	THURSDAY (4),
	FRIDAY (5),
	SATURDAY (6);

	private final int index;

	Day(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	} //End public int getIndex()

	public String toString() {
		switch(index) {
			case 0:
				return "Sunday";
			case 1:
				return "Monday";
			case 2:
				return "Tuesday";
			case 3:
				return "Wednesday";
			case 4:
				return "Thursday";
			case 5:
				return "Friday";
			default:
				return "Saturday";
		}
	} //End public String toString()
} //End public enum Day
