import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hp.gagawa.java.DocumentType;
import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H3;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Title;

public class TivooSystem {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	private String fileName;
//	private String keyWord;

	private void loadFile(String name) {
		fileName = name;
	}

	public ArrayList<Event> filterByKeyword(String keyWord) {
		File inputXml = new File(fileName);

		SAXReader saxReader = new SAXReader();
		ArrayList<Event> filtEvents = new ArrayList<Event>();

		try {
			Document document = saxReader.read(inputXml);
			Element root = document.getRootElement();
			
			ArrayList<Event> events = null;
			if (fileName.equals("resources/dukecal.xml")) {
				events = parseDukeEvent(root);
					
		//		displaySummary(filtEvents, "summary");	
			}
			else if (fileName.equals("resources/googlecal.xml")) {
			    events = parseGoogleEvent(root);
				
			}
			else
				throw new Error("This calendar type is not recognized.");
			
			for (Event i: events)
			{
				if (i.getTitle().contains(keyWord)) {
					filtEvents.add(i);
				}

			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return filtEvents;		

	}
	
	public ArrayList<Event> filterByTime(String timeStamp)
	{
		Date standard =   parseStringYMdToDate(timeStamp);
		File inputXml = new File(fileName);

		SAXReader saxReader = new SAXReader();
		ArrayList<Event> filtEvents = new ArrayList<Event>();

		try {
			Document document = saxReader.read(inputXml);
			Element root = document.getRootElement();
			
			ArrayList<Event> events = null;
			if (fileName.equals("resources/dukecal.xml")) {
				events = parseDukeEvent(root);
					
		//		displaySummary(filtEvents, "summary");	
			}
			else if (fileName.equals("resources/googlecal.xml")) {
			    events = parseGoogleEvent(root);
				
			}
			else
				throw new Error("This calendar type is not recognized.");
			
			for (Event i: events)
			{
				if(i.getTimeStamp().compareTo("")==0)
				{
					filtEvents.add(i);
					continue;
				}
				 
				Date eventDate = parseStringYMdToDate(i.getTimeStamp());
				long bias = eventDate.getTime() / 86400000 - standard.getTime() / 86400000;
				if (bias<20&&bias>0){
					filtEvents.add(i);
				}

			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return filtEvents;		
	}


	private ArrayList<Event> parseDukeEvent(Element root) {
		List<Element> events = root.elements("event");
	//	Stack<Element> filtEvents = new Stack<Element>();
		
		ArrayList<Event> filterEvents  = new ArrayList<Event>();

		for (int i = 0; i < events.size(); i++) {
			
	//		if (events.get(i).element("summary").getText().contains(keyWord)) {
			
				Event event = new Event();
				event.setTitle(events.get(i).element("summary").getText());
		//		String[] date = events.get(i).element("content").getText().split(" ");
				event.setEndTime(events.get(i).element("end").elementText("time")+
						" "+events.get(i).element("end").elementText("shortdate"));
				event.setStartTime(events.get(i).element("start").elementText("time")+
						" "+events.get(i).element("start").elementText("shortdate"));                
				event.setTimeStamp(events.get(i).element("start").element("unformatted").getText().substring(0, 8));
				event.setLink(events.get(i).element("link").getText());
				filterEvents.add(event);
		//	}
		}

		return filterEvents;
	}

	private ArrayList<Event> parseGoogleEvent(Element root) {
		List<Element> events = root.elements("entry");
		
		ArrayList<Event> filterEvents  = new ArrayList<Event>();
		
		for (int i = 0; i < events.size(); i++) {
						
				Event event = new Event();
				String[] date = events.get(i).element("content").getText().split(" ");

				StringBuilder str = new StringBuilder();
				if(date[0].compareTo("When:")==0)
				{
					date[3]=date[3].substring(0,date[3].length()-1);
				    str.append(date[4].substring(0,4)).append(parseMonth(date[2])).append(parseDay(date[3]));
				 
				}  

				event.setTitle(events.get(i).element("title").getText());
				event.setStartTime(events.get(i).elementText("content"));
				event.setEndTime(null);
				event.setLink(events.get(i).elementText("id"));

				event.setTimeStamp(str.toString());
				filterEvents.add(event);
			
		}

		return filterEvents;
	}
	
	public ArrayList<Event> parseDukeBasketBallEvent(Element root){
		List<Element> events = root.elements("Calendar"); 
		ArrayList<Event> filterEvents  = new ArrayList<Event>();
		for(int i = 0; i <events.size(); i++){
			Event event = new Event();
			String title = events.get(i).elementText("Subject");
			event.setTitle(title);
			
            String time[] = events.get(i).elementText("StartDate").split("/");
            for(int j = 0 ; j < 2 ; j++){
				if(time[j].length() == 1){
					time[j] = "0" + time[j];
				}
			}
			
			String timeStamp = time[2]+time[0]+time[1];
			
			event.setTimeStamp(timeStamp);
			
			String starttime = events.get(i).elementText("StartTime") +
					           " " + events.get(i).elementText("StartDate");
			event.setStartTime(starttime);
			
			String endtime = events.get(i).elementText("EndTime") +
			                 " " + events.get(i).elementText("EndDate");
			event.setEndTime(endtime);
			
			String link = events.get(i).elementText("Description");
			link = link.substring(link.indexOf("http"));
			event.setLink(link);
			
			filterEvents.add(event);
		}
		return filterEvents;
	}
    
	public ArrayList<Event> parseNFLEvent(Element root){
		List<Element> events = root.elements("row"); 
		ArrayList<Event> filterEvents  = new ArrayList<Event>();
		for(int i = 0; i <events.size(); i++){
			Event event = new Event();
			String title = events.get(i).elementText("Col1");
			event.setTitle(title);
			
			String time = events.get(i).elementText("Col8").split(" ")[0];
			String timearray[] = time.split("-");
			String timeStamp = timearray[0]+timearray[1]+timearray[2];
			event.setTimeStamp(timeStamp);
			
			String link = events.get(i).elementText("Col2");
			event.setLink(link);
			
			String starttime = events.get(i).elementText("Col8");
			event.setStartTime(starttime);
			
			String endtime = events.get(i).elementText("Col9");
			event.setEndTime(endtime);
			
			filterEvents.add(event);
		}
		return filterEvents;
	}

	
	public void outputSummary(ArrayList<Event> list)
	{
		com.hp.gagawa.java.Document document = new com.hp.gagawa.java.Document(DocumentType.XHTMLTransitional);
		document.head.appendChild(new Title().appendChild(new Text("Calendar Summary")));
        Body body = document.body;
        
	}
	public void displaySummary(Stack<Element> events, String nodeTitle) throws IOException {
		Div document = new Div();
		
		while (!events.isEmpty()) {
			Element event = events.pop();
			Div element = new Div();
			H3 title = new H3();
			
			title.appendText(event.element(nodeTitle).getText());
			element.appendChild(title);
			document.appendChild(element);
		}
				
		PrintWriter out = new PrintWriter(new FileWriter("resources/summary.html")); 
		out.print(document.write());
		out.close();
	}
	
	public String parseMonth(String month)
	{
		String mon=null;
	    String[] MONTHS={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for(int k=0;k<12;k++){
        	if(month.compareTo(MONTHS[k])==0){
        		mon=String.valueOf(k+1);
        		if(k>0&&k<9)
        			mon = "0"+mon;
        	}
        }
        return mon;

	}
	
	public String parseDay(String day)
	{
		String we=day;
		int k = Integer.parseInt(day);
        if(k>0&&k<10)
        we = "0"+we;
        return we;
		

	}
	
	public static Date parseStringYMdToDate(String date) {
		Date output = null;
		try {
			output = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return output;
	}

	public static void main(String[] argv) {
		TivooSystem s = new TivooSystem();
		s.loadFile("resources/googlecal.xml");
		ArrayList<Event> list = s.filterByTime("20110919");
		for(Event i: list)
			i.publish();
		// s.outputSummaryAndDetailsPages("output/summary.html",
		// "output/details_dir");

	}

}
