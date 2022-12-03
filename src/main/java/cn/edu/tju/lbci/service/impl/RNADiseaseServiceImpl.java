package cn.edu.tju.lbci.service.impl;

import cn.edu.tju.lbci.model.PredictionResult;
import cn.edu.tju.lbci.service.RNADiseaseService;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCellArray;
import com.mathworks.toolbox.javabuilder.MWException;
import lombok.extern.slf4j.Slf4j;
import ncRNA_disease.ncRNA_disease;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RNADiseaseServiceImpl implements RNADiseaseService {
    /**
     * @param dataPath       表示数据的存储位置
     * @param dataName       dataName一共分为四个，代表不同的数据集，有circRNA、miRNA、lncRNA1、lncRNA2
     * @param ncRNAorDisease 表示是对非编码RNA进行关联预测还是对疾病进行关联预测，分为两个输入有ncRNA、disease
     * @param num            表示具体的非编码RNA或疾病的类型。
     *                       具体circRNA-disease的可从data/circRNA/interactions/cd_adjmat.txt中获得
     *                       miRNA-disease的可从data/miRNA/HMDD2.0-You-disease.txt或data/miRNA/HMDD2.0-You-disease.txt中获得
     *                       lncRNA1-disease的可从data/lncRNA1/disease_178.txt或data/lncRNA1/lncRNA_115.txt中获得
     *                       lncRNA2-disease的可从data/lncRNA1/diseaseNames_2017.txt或data/lncRNA1/lncRNANames_2017.txt中获得
     * @param numLayer       表示模型的层数，一共分为两层，输入1或者2
     * @param numNode        表示模型每层对应的节点数，最多4个，输入1、2、3、4
     * @return 返回模型预测的前20的对应的结果
     * predict_results = train_ncRNA_dis(data_path,data_name,ncRNA_or_disease,num,num_layer,num_node)
     */
    @Override
    public PredictionResult predict(String dataPath, String dataName, String ncRNAorDisease,
                                    String num, String numLayer, String numNode) {
        PredictionResult result = new PredictionResult();
        ncRNA_disease ncRNADisease = null;
        Object[] res = null;
        MWCellArray tmp = null;
        List<MWArray> arrays = null;
        List<String> list = new ArrayList<>();
        try {
            ncRNADisease = new ncRNA_disease();
            // 第一个参数是返回值数量
            log.info("starting predict drug-disease relationship, invoke ncRNADisease.train_ncRNA_dis()");
            res = ncRNADisease.train_ncRNA_dis(1, dataPath, dataName, ncRNAorDisease, num,
                    Integer.valueOf(numLayer), Integer.valueOf(numNode));
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
            if (ncRNADisease != null) {
                log.info("dispose ncRNADisease...");
                ncRNADisease.dispose();
            }
            if (res != null) {
                log.info("dispose res...");
                for (Object re : res) {
                    MWArray.disposeArray(re);
                }
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
        }
        return result;
    }
}
