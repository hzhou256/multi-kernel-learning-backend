package cn.edu.tju.lbci.service;

import cn.edu.tju.lbci.model.PredictionResult;

public interface RNADiseaseService {

    public PredictionResult predict(String dataPath, String dataName, String ncRNAorDisease,
                                    String num, String numLayer, String numNode);
}
