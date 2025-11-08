package com.epis.utils;

import com.epis.entities.Epi;
import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

                Funcao funcaoObj = new Funcao(funcao);
                Funcionario f = new Funcionario(re, nome, funcaoObj);
                funcionariosses.add(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return funcionariosses;
    }

    public static List<Epi> lerEpis() {
        Set<String> codigosEpis = new LinkedHashSet<>();
        List<Epi> epis = new ArrayList<>();

        try (InputStream arquivo = UploadFiles.class.getResourceAsStream("/base-epis.xlsx");
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean primeiraLinha = true;

            for (Row row : sheet) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String codigo = getCellValueAsString(row.getCell(0));
                String descricaoCompleta = getCellValueAsString(row.getCell(1));

                if (codigo == null || codigo.trim().isEmpty()) {
                    continue;
                }

                if (codigosEpis.add(codigo.trim())) {
                    epis.add(new Epi(codigo.trim(), descricaoCompleta != null ? descricaoCompleta.trim() : ""));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return epis;
    }

    public static List<Funcao> lerFuncoes() {
        Set<String> nomesFuncoes = new HashSet<>();
        List<Funcao> funcoes = new ArrayList<>();

        try (InputStream arquivo = UploadFiles.class.getResourceAsStream("/base-funcionarios.xlsx");
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean primeiraLinha = true;

            for (Row row : sheet) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String funcaoImportada = getCellValueAsString(row.getCell(2));

                if (funcaoImportada == null || funcaoImportada.trim().isEmpty()) {
                    continue;
                }

                if (nomesFuncoes.add(funcaoImportada.trim())) {
                    funcoes.add(new Funcao(funcaoImportada.trim()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return funcoes;
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
        //List<Funcionario> listaFunc = lerFuncionarios();
        //listaFunc.forEach(System.out::println);

        System.out.println("\n--- EPIS ---\n");

        List<Epi> listaEpi = lerEpis();
        listaEpi.forEach(System.out::println);
    }
}