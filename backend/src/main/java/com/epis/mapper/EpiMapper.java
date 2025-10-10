package com.epis.mapper;

import com.epis.dtos.EpiCreateDto;
import com.epis.dtos.EpiUpdateDto;
import com.epis.entities.Epi;

public class EpiMapper {

    public static Epi toEpi(EpiCreateDto dto) {
        Epi epi = new Epi();

        epi.setCodigoAutenticacao(dto.getCodigoAutenticacao());
        epi.setCodigoCompra(dto.getCodigoCompra());
        epi.setDescricao(dto.getDescricao());
        epi.setDataValidade(dto.getDataValidade());

        return epi;
    }

    public static Epi toEpi(EpiUpdateDto dto) {
        Epi epi = new Epi();

        epi.setCodigoAutenticacao(dto.getCodigoAutenticacao());
        epi.setCodigoCompra(dto.getCodigoCompra());
        epi.setDescricao(dto.getDescricao());
        epi.setDataValidade(dto.getDataValidade());

        return epi;
    }
}
