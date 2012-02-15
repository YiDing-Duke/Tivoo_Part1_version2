import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



public class Parser {
	
	private String fileName;
	
	public Parser(String fName){
		
		fileName = fName;
	}
	
	public void parseXml()
	{
		File inputXml = new File(fileName);
		
		SAXReader saxReader = new SAXReader();
		try{
			Document document = saxReader.read(inputXml);
			Element e_events = document.getRootElement();
			List<Element> events = e_events.elements("event");
			for (Iterator i = events.iterator(); i.hasNext();) {
				Element e_event = (Element) i.next();
		//		User user = new User();
			System.out.println("Title: "+ e_event.element("summary").getText()
					+"  start time: "+ e_event.element("start").element("time").getText()
					+","+e_event.element("start").element("shortdate").getText());
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] argv)
	{
		Parser test = new Parser("//Users//mac//dukecal.xml");
		test.parseXml();
	}
	
	
	
}
