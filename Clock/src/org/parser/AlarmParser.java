/*
Copyright (c) 2013, Damian Kudas & Micha³ Mrowczyk
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the FreeBSD Project.
*/
package org.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.model.Alarm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parser for alarms - important in persistence mechanism
 * @author Micha³
 *
 */
public class AlarmParser {
	
	/**
	 * From given path parse I xml file and return I collection of alarms - ya know ?!
	 * @param path path for xml file
	 * @return collection of alarms
	 */
	public Collection<Alarm> parseAlarms(String path) {
		Collection<Alarm> result = new Vector<Alarm>();
		try {	
			System.out.println("Parsing alarms...");
			File f = new File(path);
			if (!f.exists()) {
				initializeXMLFile(f);
			}
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			
			System.out.println("Root element:" + doc.getDocumentElement().getNodeName());
			NodeList alarms = doc.getElementsByTagName("alarm");
			
			for (int i = 0; i < alarms.getLength(); i++) {
				Node alarm = alarms.item(i);
				System.out.println("Current element: " + alarm.getNodeName());
				
				if (alarm.getNodeType() == Node.ELEMENT_NODE) {
					Element alarmElem = (Element) alarm;
					String strName = alarmElem.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
					String strURI = alarmElem.getElementsByTagName("uri").item(0).getFirstChild().getNodeValue();
					String strGame = alarmElem.getElementsByTagName("game").item(0).getFirstChild().getNodeValue();
					String strCycle = alarmElem.getElementsByTagName("cycle").item(0).getFirstChild().getNodeValue();
					String strVolume = alarmElem.getElementsByTagName("volume").item(0).getFirstChild().getNodeValue();
					
					Node date = alarmElem.getElementsByTagName("date").item(0);
					Element dateElem = (Element) date;
					String strYear = dateElem.getElementsByTagName("year").item(0).getFirstChild().getNodeValue();
					String strMonth = dateElem.getElementsByTagName("month").item(0).getFirstChild().getNodeValue();
					String strDay = dateElem.getElementsByTagName("day").item(0).getFirstChild().getNodeValue();
					String strHour = dateElem.getElementsByTagName("hour").item(0).getFirstChild().getNodeValue();
					String strMinute = dateElem.getElementsByTagName("minute").item(0).getFirstChild().getNodeValue();
					
					System.out.println("Name: " + strName);
					System.out.println("URI: " + strURI);
					System.out.println("Game: " + strGame);
					System.out.println("Cycle: " + strCycle);
					System.out.println("Volume: " + strVolume);
					System.out.println("Year: " + strYear);
					System.out.println("Month: " + strMonth);
					System.out.println("Day: " + strDay);
					System.out.println("Hour: " + strHour);
					System.out.println("Minute: " + strMinute);

					String name = strName;
					String uri = strURI;
					String game = strGame;
					long cycle = Long.parseLong(strCycle);
					int volume = Integer.parseInt(strVolume);
					int year = Integer.parseInt(strYear);
					int month = Integer.parseInt(strMonth);
					int day = Integer.parseInt(strDay);
					int hour = Integer.parseInt(strHour);
					int minute = Integer.parseInt(strMinute);
					
					Calendar cal = new GregorianCalendar(year, month, day, hour, minute);
					result.add(new Alarm(name, uri, game, cycle, cal, volume));			
				}			
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * For given path and collection of alarms save I alarms
	 * @param alarms alarms to be saved in xml
	 * @param path path with xml file in it
	 */
	public void saveAlarms(Collection<Alarm> alarms, String path) {
		try {
			System.out.println("Saving alarms...");	
			File f = new File(path);
			if (!f.exists()) {
				initializeXMLFile(f);
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			
			Node root = doc.getFirstChild();
			
			// Removing all child nodes...
			while (root.hasChildNodes()) {
				root.removeChild(root.getLastChild());
			}
			
			for (Alarm alarm: alarms) {
				Element alarmElem = doc.createElement("alarm");
				root.appendChild(alarmElem);
				
				Element nameElem = doc.createElement("name");
				nameElem.appendChild(doc.createTextNode(alarm.getName()));
				alarmElem.appendChild(nameElem);
				
				Element uriElem = doc.createElement("uri");
				uriElem.appendChild(doc.createTextNode(alarm.getUri()));
				alarmElem.appendChild(uriElem);
				
				Element gameElem = doc.createElement("game");
				gameElem.appendChild(doc.createTextNode(alarm.getGame()));
				alarmElem.appendChild(gameElem);
				
				Element cycleElem = doc.createElement("cycle");
				cycleElem.appendChild(doc.createTextNode(String.valueOf(alarm.getCycle())));
				alarmElem.appendChild(cycleElem);
				
				Element volumeElem = doc.createElement("volume");
				volumeElem.appendChild(doc.createTextNode(String.valueOf(alarm.getVolume())));
				alarmElem.appendChild(volumeElem);
				
				Element dateElem = doc.createElement("date");
				alarmElem.appendChild(dateElem);
				
				Element yearElem = doc.createElement("year");
				yearElem.appendChild(doc.createTextNode(String.valueOf(alarm.getDate().get(Calendar.YEAR))));
				dateElem.appendChild(yearElem);
				
				Element monthElem = doc.createElement("month");
				monthElem.appendChild(doc.createTextNode(String.valueOf(alarm.getDate().get(Calendar.MONTH))));
				dateElem.appendChild(monthElem);
				
				Element dayElem = doc.createElement("day");
				dayElem.appendChild(doc.createTextNode(String.valueOf(alarm.getDate().get(Calendar.DAY_OF_MONTH))));
				dateElem.appendChild(dayElem);
				
				Element hourElem = doc.createElement("hour");
				hourElem.appendChild(doc.createTextNode(String.valueOf(alarm.getDate().get(Calendar.HOUR_OF_DAY))));
				dateElem.appendChild(hourElem);
				
				Element minuteElem = doc.createElement("minute");
				minuteElem.appendChild(doc.createTextNode(String.valueOf(alarm.getDate().get(Calendar.MINUTE))));
				dateElem.appendChild(minuteElem);
			}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(f);
			transformer.transform(source, result);
			System.out.println("SUCCESS!");
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializing xml file if not already initialized
	 * @param f xml file 
	 * @throws IOException
	 */
	private void initializeXMLFile(File f) throws IOException {
		System.out.println("Creating XML Configuration File");
		if (!f.createNewFile()) {
			System.out.println("Cannot create file on Android");
		} else {		
				FileWriter fw = new FileWriter(f.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n");
				bw.write("<alarms>" + "\n");
				bw.write("</alarms>" + "\n");
				bw.close();
				fw.close();				
		}
	}
}
