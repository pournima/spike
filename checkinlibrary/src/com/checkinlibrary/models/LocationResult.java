package com.checkinlibrary.models;

//39.76687666666667 lon -86.04375999999999: [{"location":{"bearing":"271.0","contact_name":"Morgan","distance":9.60340820021933,"id":54,"name":"Hairtastic","prefered_currency":null,"supported":16}},
//{"location":{"bearing":"271.0","contact_name":"Morgan","distance":9.68909377675898,"id":55,"name":"Bloomin' Gardens","prefered_currency":null,"supported":16}}]

public class LocationResult {
    String contact_name;
    String name;
    Integer supported;
    Double distance;
    Double bearing;
}
