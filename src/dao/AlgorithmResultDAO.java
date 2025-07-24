package dao;

import java.util.List;
import models.AlgorithmResult;

public interface AlgorithmResultDAO {
    void save(AlgorithmResult algorithmResult);
    List<AlgorithmResult> findAll();
    void clear();
}
