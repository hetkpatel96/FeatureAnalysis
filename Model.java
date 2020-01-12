
public class Model {

	
	private String path;
	
	private String data;
	
	private String executionTime;
	
	private String Duration;
	
	private String StartTimeStamp;
	
	private String endTimeStamp;
	
	

	public String getStartTimeStamp() {
		return StartTimeStamp;
	}

	public void setStartTimeStamp(String startTimeStamp) {
		StartTimeStamp = startTimeStamp;
	}

	public String getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(String endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	@Override
	public String toString() {
		return path + ", " + data + ", " + executionTime + ", " + Duration
				+ ", " + StartTimeStamp + ", " + endTimeStamp;
	}
	
	
}
