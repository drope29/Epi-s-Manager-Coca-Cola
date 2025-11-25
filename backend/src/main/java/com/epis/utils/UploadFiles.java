package com.epis.utils;

import com.epis.entities.Epi;
import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import com.epis.services.FuncaoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class UploadFiles {

    @Autowired
    private FuncaoService funcaoService;

    private static final Logger log = LoggerFactory.getLogger(UploadFiles.class);

    public List<Funcionario> lerFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();

        Map<String, Funcao> funcoesMap = new HashMap<>();

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
                String funcaoNome = getCellValueAsString(row.getCell(2)).trim();

                if (re.isEmpty() || nome.isEmpty() || funcaoNome.isEmpty()) {
                    continue;
                }

                Funcao funcao = funcoesMap.computeIfAbsent(funcaoNome, nomeFuncao ->
                        new Funcao(UUID.randomUUID(), nomeFuncao)
                );

                Funcionario funcionario = new Funcionario(re, nome, funcao.getId());
                funcionarios.add(funcionario);
            }

            this.lerFuncoes(new ArrayList<>(funcoesMap.values()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }


    public List<Epi> lerEpis() {
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

    public void lerFuncoes(List<Funcao> funcoes) {
        funcaoService.uploadFuncoes(funcoes);
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
}