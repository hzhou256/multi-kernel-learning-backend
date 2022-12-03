package cn.edu.tju.lbci.service.impl;

import cn.edu.tju.lbci.model.PredictionResult;
import cn.edu.tju.lbci.service.DrugDiseaseService;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCellArray;
import com.mathworks.toolbox.javabuilder.MWException;
import drug_disease.drug_disease;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DrugDiseaseServiceImpl implements DrugDiseaseService {

    /**
     * @param dataPath      'data/';% 数据所在路径
     * @param dataName      'LRSSL_dataset';%所用的数据集，分为三个分别是Cdataset、Fdataset、LRSSL_dataset
     * @param drugOrDisease 'disease';%表示是对药物进行关联预测还是对疾病进行关联预测，分为两个输入有drug、disease
     * @param num           'Abortion, Spontaneous' %对应的是具体的药物或者疾病，具体在在data/Cdataset.mat或者data/Fdataset.mat或者
     *                      data/Fdataset.mat中，有两个cell文件分别是dis_name和drug_name
     * @return 返回模型预测的前20的对应的结果
     * [predict_results] = train(data_path,data_name,drug_or_disease,num)
     */
    @Override
    public PredictionResult predict(String dataPath, String dataName, String drugOrDisease, String num) {
        PredictionResult result = new PredictionResult();
        drug_disease drugDisease = null;
        MWCellArray tmp = null;
        List<MWArray> arrays = null;
        Object[] res = null;
        List<String> list = new ArrayList<>();
        try {
            drugDisease = new drug_disease();
            // 第一个参数是返回值数量
            log.info("starting predict drug-disease relationship, invoke drugDisease.train()");
            res = drugDisease.train(1, dataPath, dataName, drugOrDisease, num);
            tmp = (MWCellArray) res[0];
            arrays = tmp.asList();
            for (MWArray array : arrays) {
                String s = array.toString();
                list.add(s);
            }
            result.setIsSuccess(true);
            result.setList(list);
            result.setMessage("prediction successful");
            log.info("predict successful, result: {}", list);
        } catch (MWException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            result.setIsSuccess(false);
            result.setList(new ArrayList<>(0));
            result.setMessage("prediction fail");
        } finally {
            // 释放资源
            log.info("release resources...");
            if (drugDisease != null) {
                log.info("dispose drugDisease...");
                drugDisease.dispose();
            }
            if (tmp != null) {
                log.info("dispose tmp...");
                MWArray.disposeArray(tmp);
            }
//            if (arrays != null) {
//                log.info("dispose arrays...");
//                for (MWArray array : arrays) {
//                    MWArray.disposeArray(array);
//                }
//            }
            if (res != null) {
                log.info("dispose res...");
                for (Object re : res) {
                    MWArray.disposeArray(re);
                }
            }
        }
        return result;
    }
}
