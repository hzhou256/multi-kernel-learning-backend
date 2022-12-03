package cn.edu.tju.lbci.service;

import cn.edu.tju.lbci.model.PredictionResult;


public interface DrugDiseaseService {

    public PredictionResult predict(String dataPath, String dataName, String drugOrDisease, String num);

}
