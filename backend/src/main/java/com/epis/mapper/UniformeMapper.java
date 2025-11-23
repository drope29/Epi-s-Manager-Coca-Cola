package com.epis.mapper;

import com.epis.dtos.UniformeCreateDto;
import com.epis.dtos.UniformeUpdateDto;
import com.epis.entities.Epi;
import com.epis.entities.Uniforme;
import com.epis.entities.UniformeEpi;
import com.epis.services.EpiService;
import com.epis.services.FuncaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UniformeMapper {

    @Autowired
    private FuncaoService funcaoService;

    @Autowired
    private EpiService epiService;

    public Uniforme toUniforme(UniformeCreateDto dto) {

        Uniforme uniforme = new Uniforme();

        uniforme.setUniformeId(UUID.randomUUID());

        uniforme.setFuncao(funcaoService.getById(dto.getFuncao()));

        List<UniformeEpi> itens = dto.getUniformeEpis().stream().map(req -> {
            Epi epi = epiService.getById(req.getEpi());

            UniformeEpi ue = new UniformeEpi();
            ue.setEpi(epi);
            ue.setQuantidade(req.getQuantidade());
            return ue;
        }).toList();

        uniforme.setUniformeEpis(itens);

        return uniforme;

    }

    public void toUniforme(UniformeUpdateDto dto, Uniforme uniforme) {

        if (dto.getFuncao() != null) {

            uniforme.setFuncao(funcaoService.getById(dto.getFuncao()));

        }

        if (dto.getUniformeEpis() != null) {

            List<UniformeEpi> itens = dto.getUniformeEpis().stream().map(req -> {
                Epi epi = epiService.getById(req.getEpi());

                UniformeEpi ue = new UniformeEpi();
                ue.setEpi(epi);
                ue.setQuantidade(req.getQuantidade());
                return ue;
            }).toList();

            uniforme.setUniformeEpis(itens);

        }

    }

}
