package com.epis.mapper;

import com.epis.dtos.EpiCreateDto;
import com.epis.dtos.EpiUpdateDto;
import com.epis.entities.Epi;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EpiMapper {

    public Epi toEpi(EpiCreateDto dto) {
        Epi epi = new Epi();

        epi.setEpiId(UUID.randomUUID());
        epi.setCodigoAutenticacao(dto.getCodigoAutenticacao());
        epi.setCodigoCompra(dto.getCodigoCompra());
        epi.setDescricao(dto.getDescricao());
        epi.setDataValidade(dto.getDataValidade());

        return epi;
    }

    public void toEpi(EpiUpdateDto dto, Epi epi) {

        if (dto.getCodigoAutenticacao() != null) {
            epi.setCodigoAutenticacao(dto.getCodigoAutenticacao());
        }

        if (dto.getCodigoCompra() != null) {
            epi.setCodigoCompra(dto.getCodigoCompra());
        }

        if (dto.getDescricao() != null) {
            epi.setDescricao(dto.getDescricao());
        }

        if (dto.getDataValidade() != null) {
            epi.setDataValidade(dto.getDataValidade());
        }

    }
}
