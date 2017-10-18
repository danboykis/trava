package com.danboykis.trava;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.danboykis.trava.MapNavigator.*;

public class MapNavigatorTest {

    public static<T> List<T> listOf(T... args) {
        return Collections.unmodifiableList( Arrays.asList(args) );
    }

    public static Map<Object,Object> mapOf(Object... kvs) {
        HashMap hm = new HashMap();

        for(int i=0; i<kvs.length; i+=2) {
            hm.put(kvs[i],kvs[i+1]);
        }

        return hm;
    }

    @Test
    public void testGetIn() throws Exception {
        Map<String,Object> m1 = new HashMap<>();
        Map<String,Object> m2 = new HashMap<>();
        Map<String,Object> m3 = new HashMap<>();
        m3.put("3","FOUND_ME");
        m2.put("2",m3);
        m1.put("1",m2);
        Assert.assertEquals("FOUND_ME", getIn(m1, listOf("1", "2", "3")));
        Assert.assertEquals(null, getIn(m1, listOf("X")));
        Assert.assertEquals(null, getIn(m1, listOf("X","Y")));
    }

    @Test
    public void testAssoc() throws Exception {
        Object data = null;

        Map m = (Map) assoc(data, "1", "VALUE1");
        Assert.assertTrue(m.containsKey("1"));
        Assert.assertEquals("VALUE1",m.get("1"));

        data = new HashMap();

        m = (Map) MapNavigator.assoc(data, "1", "VALUE1");
        Assert.assertTrue(m.containsKey("1"));
        Assert.assertEquals("VALUE1",m.get("1"));

    }

    @Test
    public void testAssocIn() throws Exception {
        Object data = null;

        Assert.assertEquals(
                mapOf("A", mapOf("B", mapOf("C", "VALUE"))),
                MapNavigator.assocIn(data, listOf("A","B","C"),"VALUE"));

        Map<String,Object> m1 = new HashMap<>();
        Map<String,Object> m2 = new HashMap<>();
        m2.put("B",1);
        m1.put("A",m2);

        Assert.assertEquals(
                mapOf("A", mapOf("B", mapOf("C", "VALUE"))),
                assocIn(m1, listOf("A","B","C"),"VALUE"));

    }

    @Test
    public void testAssocMany() throws Exception {
        Map<String, Object> m = new HashMap<>();
        assoc(m,"K1","V1","K2","V2","K3","V3");

        Assert.assertEquals("V1", m.get("K1"));
        Assert.assertEquals("V2", m.get("K2"));
        Assert.assertEquals("V3", m.get("K3"));
    }

    @Test
    public void testAssocInMany() throws Exception {
        Object data = assocIn(null,listOf("A1","A2"),"AVAL",
                listOf("B1","B2"), "BVAL");

        Object testData = mapOf("A1",mapOf("A2","AVAL"), "B1", mapOf("B2", "BVAL"));

        Assert.assertTrue(testData.equals(data));
    }
}