package com.vlad.dao;
import javax.ejb.Local;

import com.vlad.dto.ProdusDTO;

@Local
public interface ProdusDAO {

    public ProdusDTO save(ProdusDTO produs);
}
