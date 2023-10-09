import com.github.javafaker.Faker;
import utilities.ExcelUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunnerClass {

    private static final String columnName = "nationalInsuranceNumber";
    private static final Faker faker = new Faker();
    private static ExcelUtil dataSheet;
    private static int countWales = 0;
    private static int countEngland = 0;
    private static int countScotland = 0;
    private static int countNorthernIreland = 0;
    private static int countNonUk = 0;

    public static void main(String[] args) {

        dataSheet = new ExcelUtil("src\\test\\resources\\dataSheets\\TestDataSheet.xlsx", "People Data Sheet");

        List<Map<String, String>> dataList = dataSheet.getDataList();

        generateNinos(dataList);
    }

    private static void generateNinos(List<Map<String, String>> dataList) {
        List<String> ninoList = new ArrayList<>();
        int rowNumber = 1;
        String nationalInsuranceNumber;

        for (Map<String, String> person : dataList) {


            nationalInsuranceNumber = buildNino(person);

            while (ninoList.contains(nationalInsuranceNumber)) {
                nationalInsuranceNumber = buildNino(person);
            }

            dataSheet.setCellData(nationalInsuranceNumber, columnName, rowNumber);
            ninoList.add(nationalInsuranceNumber);
            rowNumber++;
        }

        printNumbersPerCountry();
    }

    private static String buildNino(Map<String, String> person) {

        String firstNameDigit = String.valueOf(person.get("firstName").charAt(0));
        String lastNameDigit = String.valueOf(person.get("lastName").charAt(0));
        String birthYearDigits = person.get("dateOfBirth").substring(9);
        String randomCode = getRandomCode();
        String birthCountryDigit = setBirthCountryDigit(person.get("birthCountry"));

        return firstNameDigit + lastNameDigit + " " + birthYearDigits + " " + randomCode + " " + birthCountryDigit;

    }

    private static String setBirthCountryDigit(String birthCountry) {
        String birthCountryDigit = "";

        switch (birthCountry) {
            case "Wales":
                birthCountryDigit = "W";
                countWales++;
                break;
            case "England":
                birthCountryDigit = "E";
                countEngland++;
                break;
            case "Scotland":
                birthCountryDigit = "S";
                countScotland++;
                break;
            case "Northern Ireland":
                birthCountryDigit = "N";
                countNorthernIreland++;
                break;
            default:
                birthCountryDigit = "O";
                countNonUk++;
                break;
        }

        return birthCountryDigit;
    }

    private static String getRandomCode() {
        return Integer.toString(faker.number().numberBetween(1000, 9999));
    }

    private static void printNumbersPerCountry() {
        System.out.println("National Insurance Number count per country:");
        System.out.println("Wales = " + countWales);
        System.out.println("England = " + countEngland);
        System.out.println("Scotland = " + countScotland);
        System.out.println("Northern Ireland = " + countNorthernIreland);
        System.out.println("Non-UK = " + countNonUk);
    }

}
