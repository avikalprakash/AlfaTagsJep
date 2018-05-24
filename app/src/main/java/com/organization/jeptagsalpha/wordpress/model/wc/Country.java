package com.organization.jeptagsalpha.wordpress.model.wc;

import java.util.ArrayList;
import java.util.List;


public class Country {
    private String id;
    private String name;

    public Country(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static List<Country> getCountryList()
    {
        List<Country> countryList = new ArrayList<Country>();
        countryList.add(new Country("AF","Afghanistan"));
        countryList.add(new Country("AX","Åland Islands"));
        countryList.add(new Country("AL","Albania"));
        countryList.add(new Country("DZ","Algeria"));
        countryList.add(new Country("AD","Andorra"));
        countryList.add(new Country("AO","Angola"));
        countryList.add(new Country("AI","Anguilla"));
        countryList.add(new Country("AQ","Antarctica"));
        countryList.add(new Country("AG","Antigua and Barbuda"));
        countryList.add(new Country("AR","Argentina"));
        countryList.add(new Country("AM","Armenia"));
        countryList.add(new Country("AW","Aruba"));
        countryList.add(new Country("AU","Australia"));
        countryList.add(new Country("AT","Austria"));
        countryList.add(new Country("AZ","Azerbaijan"));
        countryList.add(new Country("BS","Bahamas"));
        countryList.add(new Country("BH","Bahrain"));
        countryList.add(new Country("BD","Bangladesh"));
        countryList.add(new Country("BB","Barbados"));
        countryList.add(new Country("BY","Belarus"));
        countryList.add(new Country("BE","Belgium"));
        countryList.add(new Country("PW","Belau"));
        countryList.add(new Country("BZ","Belize"));
        countryList.add(new Country("BJ","Benin"));
        countryList.add(new Country("BM","Bermuda"));
        countryList.add(new Country("BT","Bhutan"));
        countryList.add(new Country("BO","Bolivia"));
        countryList.add(new Country("BQ","Bonaire"));
        countryList.add(new Country("BA","Bosnia and Herzegovina"));
        countryList.add(new Country("BW","Botswana"));
        countryList.add(new Country("BV","Bouvet Island"));
        countryList.add(new Country("BR","Brazil"));
        countryList.add(new Country("IO","British Indian Ocean Territory"));
        countryList.add(new Country("VG","British Virgin Islands"));
        countryList.add(new Country("BN","Brunei"));
        countryList.add(new Country("BG","Bulgaria"));
        countryList.add(new Country("BF","Burkina Faso"));
        countryList.add(new Country("BI","Burundi"));
        countryList.add(new Country("KH","Cambodia"));
        countryList.add(new Country("CM","Cameroon"));
        countryList.add(new Country("CA","Canada"));
        countryList.add(new Country("CV","Cape Verde"));
        countryList.add(new Country("KY","Cayman Islands"));
        countryList.add(new Country("CF","Central African Republic"));
        countryList.add(new Country("TD","Chad"));
        countryList.add(new Country("CL","Chile"));
        countryList.add(new Country("CN","China"));
        countryList.add(new Country("CX","Christmas Island"));
        countryList.add(new Country("CC","Cocos (Keeling) Islands"));
        countryList.add(new Country("CO","Colombia"));
        countryList.add(new Country("KM","Comoros"));
        countryList.add(new Country("CG","Congo (Brazzaville)"));
        countryList.add(new Country("CD","Congo (Kinshasa)"));
        countryList.add(new Country("CK","Cook Islands"));
        countryList.add(new Country("CR","Costa Rica"));
        countryList.add(new Country("HR","Croatia"));
        countryList.add(new Country("CU","Cuba"));
        countryList.add(new Country("CW","CuraÇao"));
        countryList.add(new Country("CY","Cyprus"));
        countryList.add(new Country("CZ","Czech Republic"));
        countryList.add(new Country("DK","Denmark"));
        countryList.add(new Country("DJ","Djibouti"));
        countryList.add(new Country("DM","Dominica"));
        countryList.add(new Country("DO","Dominican Republic"));
        countryList.add(new Country("EC","Ecuador"));
        countryList.add(new Country("EG","Egypt"));
        countryList.add(new Country("SV","El Salvador"));
        countryList.add(new Country("GQ","Equatorial Guinea"));
        countryList.add(new Country("ER","Eritrea"));
        countryList.add(new Country("EE","Estonia"));
        countryList.add(new Country("ET","Ethiopia"));
        countryList.add(new Country("FK","Falkland Islands"));
        countryList.add(new Country("FO","Faroe Islands"));
        countryList.add(new Country("FJ","Fiji"));
        countryList.add(new Country("FI","Finland"));
        countryList.add(new Country("FR","France"));
        countryList.add(new Country("GF","French Guiana"));
        countryList.add(new Country("PF","French Polynesia"));
        countryList.add(new Country("TF","French Southern Territories"));
        countryList.add(new Country("GA","Gabon"));
        countryList.add(new Country("GM","Gambia"));
        countryList.add(new Country("GE","Georgia"));
        countryList.add(new Country("DE","Germany"));
        countryList.add(new Country("GH","Ghana"));
        countryList.add(new Country("GI","Gibraltar"));
        countryList.add(new Country("GR","Greece"));
        countryList.add(new Country("GL","Greenland"));
        countryList.add(new Country("GD","Grenada"));
        countryList.add(new Country("GP","Guadeloupe"));
        countryList.add(new Country("GT","Guatemala"));
        countryList.add(new Country("GG","Guernsey"));
        countryList.add(new Country("GN","Guinea"));
        countryList.add(new Country("GW","Guinea-Bissau"));
        countryList.add(new Country("GY","Guyana"));
        countryList.add(new Country("HT","Haiti"));
        countryList.add(new Country("HM","Heard Island and McDonald Islands"));
        countryList.add(new Country("HN","Honduras"));
        countryList.add(new Country("HK","Hong Kong"));
        countryList.add(new Country("HU","Hungary"));
        countryList.add(new Country("IS","Iceland"));
        countryList.add(new Country("IN","India"));
        countryList.add(new Country("ID","Indonesia"));
        countryList.add(new Country("IR","Iran"));
        countryList.add(new Country("IQ","Iraq"));
        countryList.add(new Country("IE","Republic of Ireland"));
        countryList.add(new Country("IM","Isle of Man"));
        countryList.add(new Country("IL","Israel"));
        countryList.add(new Country("IT","Italy"));
        countryList.add(new Country("CI","Ivory Coast"));
        countryList.add(new Country("JM","Jamaica"));
        countryList.add(new Country("JP","Japan"));
        countryList.add(new Country("JE","Jersey"));
        countryList.add(new Country("JO","Jordan"));
        countryList.add(new Country("KZ","Kazakhstan"));
        countryList.add(new Country("KE","Kenya"));
        countryList.add(new Country("KI","Kiribati"));
        countryList.add(new Country("KW","Kuwait"));
        countryList.add(new Country("KG","Kyrgyzstan"));
        countryList.add(new Country("LA","Laos"));
        countryList.add(new Country("LV","Latvia"));
        countryList.add(new Country("LB","Lebanon"));
        countryList.add(new Country("LS","Lesotho"));
        countryList.add(new Country("LR","Liberia"));
        countryList.add(new Country("LY","Libya"));
        countryList.add(new Country("LI","Liechtenstein"));
        countryList.add(new Country("LT","Lithuania"));
        countryList.add(new Country("LU","Luxembourg"));
        countryList.add(new Country("MO","Macao S.A.R."));
        countryList.add(new Country("MK","Macedonia"));
        countryList.add(new Country("MG","Madagascar"));
        countryList.add(new Country("MW","Malawi"));
        countryList.add(new Country("MY","Malaysia"));
        countryList.add(new Country("MV","Maldives"));
        countryList.add(new Country("ML","Mali"));
        countryList.add(new Country("MT","Malta"));
        countryList.add(new Country("MH","Marshall Islands"));
        countryList.add(new Country("MQ","Martinique"));
        countryList.add(new Country("MR","Mauritania"));
        countryList.add(new Country("MU","Mauritius"));
        countryList.add(new Country("YT","Mayotte"));
        countryList.add(new Country("MX","Mexico"));
        countryList.add(new Country("FM","Micronesia"));
        countryList.add(new Country("MD","Moldova"));
        countryList.add(new Country("MC","Monaco"));
        countryList.add(new Country("MN","Mongolia"));
        countryList.add(new Country("ME","Montenegro"));
        countryList.add(new Country("MS","Montserrat"));
        countryList.add(new Country("MA","Morocco"));
        countryList.add(new Country("MZ","Mozambique"));
        countryList.add(new Country("MM","Myanmar"));
        countryList.add(new Country("NA","Namibia"));
        countryList.add(new Country("NR","Nauru"));
        countryList.add(new Country("NP","Nepal"));
        countryList.add(new Country("NL","Netherlands"));
        countryList.add(new Country("AN","Netherlands Antilles"));
        countryList.add(new Country("NC","New Caledonia"));
        countryList.add(new Country("NZ","New Zealand"));
        countryList.add(new Country("NI","Nicaragua"));
        countryList.add(new Country("NE","Niger"));
        countryList.add(new Country("NG","Nigeria"));
        countryList.add(new Country("NU","Niue"));
        countryList.add(new Country("NF","Norfolk Island"));
        countryList.add(new Country("KP","North Korea"));
        countryList.add(new Country("NO","Norway"));
        countryList.add(new Country("OM","Oman"));
        countryList.add(new Country("PK","Pakistan"));
        countryList.add(new Country("PS","Palestinian Territory"));
        countryList.add(new Country("PA","Panama"));
        countryList.add(new Country("PG","Papua New Guinea"));
        countryList.add(new Country("PY","Paraguay"));
        countryList.add(new Country("PE","Peru"));
        countryList.add(new Country("PH","Philippines"));
        countryList.add(new Country("PN","Pitcairn"));
        countryList.add(new Country("PL","Poland"));
        countryList.add(new Country("PT","Portugal"));
        countryList.add(new Country("QA","Qatar"));
        countryList.add(new Country("RE","Reunion"));
        countryList.add(new Country("RO","Romania"));
        countryList.add(new Country("RU","Russia"));
        countryList.add(new Country("RW","Rwanda"));
        countryList.add(new Country("BL","Saint Barthélemy"));
        countryList.add(new Country("SH","Saint Helena"));
        countryList.add(new Country("KN","Saint Kitts and Nevis"));
        countryList.add(new Country("LC","Saint Lucia"));
        countryList.add(new Country("MF","Saint Martin (French part)"));
        countryList.add(new Country("SX","Saint Martin (Dutch part)"));
        countryList.add(new Country("PM","Saint Pierre and Miquelon"));
        countryList.add(new Country("VC","Saint Vincent and the Grenadines"));
        countryList.add(new Country("SM","San Marino"));
        countryList.add(new Country("ST","São Tomé and Príncipe"));
        countryList.add(new Country("SA","Saudi Arabia"));
        countryList.add(new Country("SN","Senegal"));
        countryList.add(new Country("RS","Serbia"));
        countryList.add(new Country("SC","Seychelles"));
        countryList.add(new Country("SL","Sierra Leone"));
        countryList.add(new Country("SG","Singapore"));
        countryList.add(new Country("SK","Slovakia"));
        countryList.add(new Country("SI","Slovenia"));
        countryList.add(new Country("SB","Solomon Islands"));
        countryList.add(new Country("SO","Somalia"));
        countryList.add(new Country("ZA","South Africa"));
        countryList.add(new Country("GS","South Georgia/Sandwich Islands"));
        countryList.add(new Country("KR","South Korea"));
        countryList.add(new Country("SS","South Sudan"));
        countryList.add(new Country("ES","Spain"));
        countryList.add(new Country("LK","Sri Lanka"));
        countryList.add(new Country("SD","Sudan"));
        countryList.add(new Country("SR","Suriname"));
        countryList.add(new Country("SJ","Svalbard and Jan Mayen"));
        countryList.add(new Country("SZ","Swaziland"));
        countryList.add(new Country("SE","Sweden"));
        countryList.add(new Country("CH","Switzerland"));
        countryList.add(new Country("SY","Syria"));
        countryList.add(new Country("TW","Taiwan"));
        countryList.add(new Country("TJ","Tajikistan"));
        countryList.add(new Country("TZ","Tanzania"));
        countryList.add(new Country("TH","Thailand"));
        countryList.add(new Country("TL","Timor-Leste"));
        countryList.add(new Country("TG","Togo"));
        countryList.add(new Country("TK","Tokelau"));
        countryList.add(new Country("TO","Tonga"));
        countryList.add(new Country("TT","Trinidad and Tobago"));
        countryList.add(new Country("TN","Tunisia"));
        countryList.add(new Country("TR","Turkey"));
        countryList.add(new Country("TM","Turkmenistan"));
        countryList.add(new Country("TC","Turks and Caicos Islands"));
        countryList.add(new Country("TV","Tuvalu"));
        countryList.add(new Country("UG","Uganda"));
        countryList.add(new Country("UA","Ukraine"));
        countryList.add(new Country("AE","United Arab Emirates"));
        countryList.add(new Country("GB","United Kingdom (UK)"));
        countryList.add(new Country("US","United States (US)"));
        countryList.add(new Country("UY","Uruguay"));
        countryList.add(new Country("UZ","Uzbekistan"));
        countryList.add(new Country("VU","Vanuatu"));
        countryList.add(new Country("VA","Vatican"));
        countryList.add(new Country("VE","Venezuela"));
        countryList.add(new Country("VN","Vietnam"));
        countryList.add(new Country("WF","Wallis and Futuna"));
        countryList.add(new Country("EH","Western Sahara"));
        countryList.add(new Country("WS","Western Samoa"));
        countryList.add(new Country("YE","Yemen"));
        countryList.add(new Country("ZM","Zambia"));
        countryList.add(new Country("ZW","Zimbabwe"));
        return countryList;
    }
    public String toString()
    {
        return this.getName();
    }

    public static Country getCountry(String id)
    {

        for (Country country :Country.getCountryList())
        {
            if(country.getId().equals(id))
            {
                return  country;
            }
        }
        return null;
    }
}
