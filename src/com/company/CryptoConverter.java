package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class CryptoConverter {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for the currency names
        System.out.println ("Convert which currency?");
        String curr1 = scanner.nextLine ();
        System.out.println("To which currency?");
        String curr2 = scanner.nextLine();

        // Fetch data objects for each currency
        JSONObject convertFrom = getCurrencyData(curr1);
        JSONObject convertTo = getCurrencyData(curr2);

        // Store relevant data and output it
        String name1 = (String) convertFrom.get("name");
        String symbol1 = (String) convertFrom.get("symbol");
        String price1 = (String) convertFrom.get("price_usd");
        String rank1 = (String) convertFrom.get("rank");

        String name2 = (String) convertTo.get("name");
        String symbol2 = (String) convertTo.get("symbol");
        String price2 = (String) convertTo.get("price_usd");
        String rank2 = (String) convertTo.get("rank");

        System.out.println("1 " + name1 + " (" + symbol1 + ") is currently worth " + (Float.parseFloat(price1) / Float.parseFloat(price2)) + " " + symbol2 + " (" + price1 + " USD).");
        System.out.println(name1 + " is currently ranked #" + rank1 + ".");
        System.out.println(name2 + " is currently ranked #" + rank2 + ".");
    }

    // Replace spaces with hyphens to match the API's format
    public static String getFormattedCurrencyName(String name) {
        return name.replace(" ", "-");
    }

    // Return the JSONObject or print the error that occurred
    public static JSONObject getCurrencyData(String currencyName) {
        JSONObject currencyData = null;

        try {
            // Access the JSON file for this currency and store the first (and only) object in the array
            URL url = new URL("https://api.coinmarketcap.com/v1/ticker/" + getFormattedCurrencyName(currencyName));
            BufferedReader bReader = new BufferedReader(new InputStreamReader(url.openStream()));
            JSONArray tickerResult = (JSONArray) new JSONParser().parse(bReader);
            currencyData = (JSONObject) tickerResult.get(0);
        } catch (MalformedURLException me) {
            System.err.println ("URL was malformed for some reason...?");
        } catch (FileNotFoundException fe) {
            System.err.println ("Currency \'" + currencyName + "\" not found. Accepted currencies are listed at https://coinmarketcap.com/");
        } catch (IOException ie) {
            System.err.println ("An IOException has occurred. Please try again.");
        } catch (ParseException pe) {
            System.err.println ("A parsing error occured.");
        }

        if (currencyData != null)
            return currencyData;
        else {
            System.exit(0);
            return null;
        }
    }
}