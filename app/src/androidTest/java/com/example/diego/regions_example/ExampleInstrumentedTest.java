package com.example.diego.regions_example;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.example.diego.regions_example", appContext.getPackageName());
    }

    @Test
    public void testCurrencyCreation() throws Exception {
        CurrencyDetails currencyDetails;
        JSONObject object;

        //////////////////////////////////////////////////////////////
        object = new JSONObject();
        object.put("code", "USD");
        object.put("name", "United States dollar");
        object.put("symbol", "$");
        currencyDetails = CurrencyDetails.create(object);
        assertEquals( "USD"                 , currencyDetails.code);
        assertEquals( "United States dollar", currencyDetails.name );
        assertEquals( "$"                   , currencyDetails.symbol );

        //////////////////////////////////////////////////////////////
        // this is the actual code sent by the server
        object = new JSONObject("{\"code\":\"USD\",\"name\":\"United States dollar\",\"symbol\":\"$\"}");
        currencyDetails = CurrencyDetails.create(object);
        assertEquals( "USD"                 , currencyDetails.code);
        assertEquals( "United States dollar", currencyDetails.name );
        assertEquals( "$"                   , currencyDetails.symbol );
    }

    public void testCountryCreation() throws Exception {
        CountryDefinition countryDefinition;

        JSONObject object;
        object = new JSONObject();
        object.put("name", "United States of America");
        object.put("area", "9629091");
        object.put("alpha3code", "USA");
        object.put("capital", "Washington, D.C.");
        object.put("currencies", "[{\"code\":\"USD\",\"name\":\"United States dollar\",\"symbol\":\"$\"}]");
        object.put("borders", "[\"CAN\",\"MEX\"]");
        countryDefinition = CountryDefinition.create(object);

        assertEquals("United States of America", countryDefinition.name);
        assertEquals(9629091.0f                , countryDefinition.area, 0.01);
        assertEquals("USA"                     , countryDefinition.alpha3Code);
        assertEquals("Washington, D.C."        , countryDefinition.capital);

        assertEquals(1                         , countryDefinition.currencies.size());
        assertEquals("USD"                     , countryDefinition.currencies.get(0).code);
        assertEquals("United States dollar"    , countryDefinition.currencies.get(0).name);
        assertEquals("$"                       , countryDefinition.currencies.get(0).symbol);

        assertEquals(2                         , countryDefinition.borders.size());
        assertEquals("CAN"                     , countryDefinition.borders.get(0));
        assertEquals("MEX"                     , countryDefinition.borders.get(1));
    }
}
