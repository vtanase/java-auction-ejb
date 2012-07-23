package com.vlad.dao;
import java.util.List;

import javax.ejb.Local;

import com.vlad.dto.UtilizatorDTO;

@Local
public interface UtilizatorDAO {

    public UtilizatorDTO save(UtilizatorDTO utilizator);
    public List<UtilizatorDTO> getVanzatori();
}
