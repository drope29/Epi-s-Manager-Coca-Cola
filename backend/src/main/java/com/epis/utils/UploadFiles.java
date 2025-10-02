package com.epis.utils;

import com.epis.entities.Funcionario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadFiles {

    public static List<Funcionario> lerFuncionarios() {
        List<Funcionario> funcionariosses = new ArrayList<>();

        try (InputStream arquivo = UploadFiles.class.getResourceAsStream("/base-funcionarios.xlsx");
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean primeiraLinha = true;

            for (Row row : sheet) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String re = getCellValueAsString(row.getCell(0));
                String nome = getCellValueAsString(row.getCell(1));
                String funcao = getCellValueAsString(row.getCell(2));

                Funcionario f = new Funcionario(re, nome, funcao);
                funcionariosses.add(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return funcionariosses;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    if (val == (long) val) return String.valueOf((long) val);
                    else return String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        List<Funcionario> lista = lerFuncionarios();
        lista.forEach(System.out::println);
    }

}
