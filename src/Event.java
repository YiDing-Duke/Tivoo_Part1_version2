import java.util.Date;





public class Event {

	private String title;
	private String link;
	private String startTime;
	private String endTime;
	private String timeStamp;
	
	public String getTimeStamp()
	{
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getLink()
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link =link;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	
	public void publish()
	{
		System.out.println(title);
		System.out.println(link);
		System.out.println(startTime);
		System.out.println(endTime);
		System.out.println(timeStamp);
		System.out.println("---------------------------I am a separator——————————————————————————————");

	}
	
}
