package com.example.kettu.myapplication;

import android.os.NetworkOnMainThreadException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TheaterJanitor {

    private ArrayList theaters = new ArrayList<Theater>();
    private static String areasXmlUrl = "https://www.finnkino.fi/xml/TheatreAreas/";
    private static String datesXmlUrl = "https://www.finnkino.fi/xml/ScheduleDates/";

    private ArrayList stringOfTheaters = new ArrayList<String>();
    private ArrayList stringOfMovies= new ArrayList<String>();
    private ArrayList stringOfDates = new ArrayList<String>();
    private ArrayList dateList = new ArrayList<Date>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    // "https://www.finnkino.fi/xml/Schedule/?area=<teatterinID>&dt=<päivämäärä pp.kk.vvvv>"

    public TheaterJanitor(){
        System.out.println("Helou Janitor");
        createTheaters();
        setDisplayDates();

    }

    public String getTheaterName(int index){
        Theater temp;
        String retVal = "";

        if (theaters.isEmpty() == false){
            temp = (Theater)theaters.get(index);
            retVal = temp.getName();
        }
        else {
            System.out.println("Emptyh!");
        }
        return retVal;
    }

    public String getTheaterId(int index){
        Theater temp;
        String retVal = "";

        if (theaters.isEmpty() == false){
            temp = (Theater)theaters.get(index);
            retVal = temp.getId();
        }
        else {
            System.out.println("Emptyh!");
        }
        return retVal;
    }

    private int createTheaters(){
        System.out.println("Create theaters.");

        setDisplayDates(); // TODO where to call from

        String theaterId = "";
        String theaterName = "";
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(areasXmlUrl);
            doc.getDocumentElement().normalize();
            System.out.println(doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("TheatreArea");
            int theaterListLen = nList.getLength();
            System.out.println("¤¤¤ ID\t Name ¤¤¤");
            for(int i = 0; i < theaterListLen; i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element elemental = (Element) node;
                    theaterId = elemental.getElementsByTagName("ID").item(0).getTextContent(); // no need for strings, remove todo
                    theaterName = elemental.getElementsByTagName("Name").item(0).getTextContent();
                    theaters.add(new Theater(theaterId, theaterName));
                    System.out.println(theaterId + "\t" + theaterName);
                    stringOfTheaters.add(theaterName);
                }
            }

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("######DONE#######");
        }

        return 1;
    }

    public String getDateString(int index){
        return stringOfDates.get(index).toString();
    }

    private void setDisplayDates(){
        System.out.println("Set display times from: " + datesXmlUrl);
        String dateSts = "";

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(datesXmlUrl);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("dateTime");
            int dateListLen = nList.getLength();
            System.out.println(dateListLen);
            for(int i = 0; i < dateListLen; i++) {
                Node node = nList.item(i);
                String dateTemp = node.getTextContent();
                dateList.add(format.parse(dateTemp));
                System.out.println(dateTemp);
            }

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            System.out.println("######DONE#######");
        }
    }


    public ArrayList<String> getTheaterStrings(){
        this.createTheaters(); // todo when to call? maybe after program has been launched and ui is visible
        return stringOfTheaters;
    }

    public ArrayList<String> getDateStrings(){
        return dateList;
    }

    // Get movies based starting and stopping date and return list of strings of movie
    public ArrayList<String> getMovies(Date startingDate, Date stopDate, int index){
        String showTitle = "";

        // Debugs
        System.out.println("getMovies from : " + startingDate.toString() + " to: "+ stopDate.toString());
        System.out.println("with index = " + index);

        // Date parody and id for the xml finnkino
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat parody = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf.format(startingDate);
        System.out.println("Date = " + date1);

        String parody1 = parody.format(startingDate);
        String parody2 = parody.format(stopDate);

        LocalDate localDate1 = LocalDate.parse(parody1);
        LocalDate localDate2 = LocalDate.parse(parody2);
        long noOfDaysDifference = ChronoUnit.DAYS.between(localDate1, localDate2);

        Theater t = (Theater)theaters.get(index);
        String movieId = t.getId();

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String s = "https://www.finnkino.fi/xml/Schedule/?area=" + movieId +"&dt="+date1+"&nrOfDays=" + noOfDaysDifference;
            System.out.println(s);
            Document doc = builder.parse(s);
            doc.getDocumentElement().normalize();
            System.out.println(doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Show");
            int movieListLen = nList.getLength();
            System.out.println("movie show len = " + movieListLen);
            for(int i = 0; i < movieListLen; i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element elemental = (Element) node;
                    showTitle = elemental.getElementsByTagName("Title").item(0).getTextContent();
                    stringOfMovies.add(showTitle);

                    System.out.println(showTitle);
                }
            }

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NetworkOnMainThreadException e){
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        }
        finally {
            System.out.println("######DONE#######");
        }

        return stringOfMovies;
    }
}
