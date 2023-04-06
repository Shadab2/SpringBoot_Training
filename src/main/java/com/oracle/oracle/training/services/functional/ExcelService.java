package com.oracle.oracle.training.services.functional;

import com.oracle.oracle.training.model.UserProfileAddress;
import com.oracle.oracle.training.entity.Company;
import com.oracle.oracle.training.model.Geo;
import com.oracle.oracle.training.entity.UserProfile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ExcelService {
    public static List<String> USER_INFO = Arrays.asList("NAME","USERNAME","EMAIL","PHONE","WEBSITE");
    public static List<String> ADDRESS_INFO = Arrays.asList("STREET","SUITE","CITY","ZIPCODE","GEO");
    public static List<String> COMPANY_INFO = Arrays.asList("NAME","CATCH_PHRASE","BS");
    public static ByteArrayInputStream convertUserProfileToExcel(UserProfile userProfile) throws IOException {
        Workbook workbook = null;
        ByteArrayOutputStream out = null;
        try{
            workbook = new XSSFWorkbook();
            out = new ByteArrayOutputStream();

            Sheet userInfoSheet = workbook.createSheet("User_Info");
            Sheet comapanyInfoSheet = workbook.createSheet("Company_Info");
            userInfoSheet.setColumnWidth(0, 6000);
            userInfoSheet.setColumnWidth(1, 6000);
            comapanyInfoSheet.setColumnWidth(0, 6000);
            comapanyInfoSheet.setColumnWidth(1, 10000);

            // style config
            XSSFFont fontItalic = (XSSFFont) workbook.createFont();
            fontItalic.setItalic(true);
            XSSFFont fontBold = (XSSFFont)  workbook.createFont();
            fontBold.setBold(true);

            // fill userInfoSheet
            int rowNum = 0;
            Cell cell = userInfoSheet.createRow(rowNum++).createCell(0);
            setFont(cell,"User Information",fontBold);
            userInfoSheet.createRow(rowNum++).createCell(0).setCellValue("");


            for(int i = 0;i< USER_INFO.size();i++,rowNum++){
                Row row =userInfoSheet.createRow(rowNum);
                Cell cellName = row.createCell(0);
                setFont(cellName,USER_INFO.get(i),fontItalic);
                Cell cellValue = row.createCell(1);
                setCellValueUser(userProfile,cellValue, USER_INFO.get(i));
            }
            userInfoSheet.createRow(rowNum++).createCell(0).setCellValue("");
            cell = userInfoSheet.createRow(rowNum++).createCell(0);
            setFont(cell,"ADDRESS",fontItalic);
            for(int i = 0;i<ADDRESS_INFO.size();i++,rowNum++){
                Row row =userInfoSheet.createRow(rowNum);
                Cell cellName = row.createCell(0);
                setFont(cellName, ADDRESS_INFO.get(i),fontItalic );
                if(ADDRESS_INFO.get(i).equals("GEO")){
                    Geo geo = userProfile.getAddress().getGeo();
                    Cell latitude =  row.createCell(1);
                    Cell latitudeValue = row.createCell(2);
                    setFont(latitude,"Lat:",fontItalic);
                    latitudeValue.setCellValue(geo.getLat());
                    rowNum++;
                    row = userInfoSheet.createRow(rowNum);
                    Cell longtitue =  row.createCell(1);
                    Cell longitudeValue = row.createCell(2);
                    setFont(longtitue,"Long:",fontItalic);
                    longitudeValue.setCellValue(geo.getLng());

                }else {
                    Cell cellValue = row.createCell(1);
                    setCellValueAddress(userProfile.getAddress(),cellValue, ADDRESS_INFO.get(i));
                }
            }

            // fill company sheet
            rowNum = 0;
            Cell companyCell = comapanyInfoSheet.createRow(rowNum++).createCell(0);
            setFont(companyCell,"User Company Details",fontBold);
            comapanyInfoSheet.createRow(rowNum++).createCell(0).setCellValue("");
            List<Company> set = userProfile.getCompanies();
            for(int j = 0; j < userProfile.getCompanies().size();j++){
                Company company = userProfile.getCompanies().get(j);
                Row headingRow = comapanyInfoSheet.createRow(rowNum++);
                headingRow.createCell(0).setCellValue((j+1)+".");
                for(int i = 0;i< COMPANY_INFO.size();i++,rowNum++){
                    Row row =comapanyInfoSheet.createRow(rowNum);
                    Cell cellName = row.createCell(0);
                    setFont(cellName, COMPANY_INFO.get(i),fontItalic);
                    Cell cellValue = row.createCell(1);
                    setCellValueCompany(company, cellValue,COMPANY_INFO.get(i));
                }
            }

            workbook.write(out);
            return  new ByteArrayInputStream(out.toByteArray());

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            workbook.close();
            out.close();
        }
        return null;
    }

    private  static void setFont(Cell cell,String value,XSSFFont font){
        XSSFRichTextString richTextString = new XSSFRichTextString();
        richTextString.append(value,font);
        cell.setCellValue(richTextString);
    }

    private static void setCellValueAddress(UserProfileAddress address, Cell cell, String cellName) {
        if(cellName.equals("STREET")) cell.setCellValue(address.getStreet());
        else if(cellName.equals("SUITE")) cell.setCellValue(address.getSuite());
        else if(cellName.equals("CITY")) cell.setCellValue(address.getCity());
        else if(cellName.equals("ZIPCODE")) cell.setCellValue(address.getZipcode());
    }

    private static void setCellValueUser(UserProfile userProfile,Cell cell,String cellName){
        if(cellName.equals("NAME")) cell.setCellValue(userProfile.getName());
        else if(cellName.equals("USERNAME")) cell.setCellValue(userProfile.getUsername());
        else if(cellName.equals("EMAIL")) cell.setCellValue(userProfile.getEmail());
        else if(cellName.equals("PHONE")) cell.setCellValue(userProfile.getPhone());
        else if(cellName.equals("WEBSITE")) cell.setCellValue(userProfile.getWebsite());
    }
    private static void setCellValueCompany(Company company,Cell cell,String cellName){
        if(cellName.equals("NAME")) cell.setCellValue(company.getName());
        else if(cellName.equals("BS")) cell.setCellValue(company.getBs());
        else if(cellName.equals("CATCH_PHRASE")) cell.setCellValue(company.getCatchPhrase());
    }
}
