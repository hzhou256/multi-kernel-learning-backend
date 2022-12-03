package cn.edu.tju.lbci.controller;

import cn.edu.tju.lbci.model.PredictionResult;
import cn.edu.tju.lbci.model.Response;
import cn.edu.tju.lbci.service.DrugDiseaseService;
import cn.edu.tju.lbci.service.RNADiseaseService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/predict")
@Slf4j
public class PredictionController {

    private final DrugDiseaseService drugDiseaseService;

    private final RNADiseaseService rnaDiseaseService;

    @Value("${data.path}")
    private String dataPath;

    public PredictionController(DrugDiseaseService drugDiseaseService, RNADiseaseService rnaDiseaseService) {
        this.drugDiseaseService = drugDiseaseService;
        this.rnaDiseaseService = rnaDiseaseService;
    }

    @PostMapping("/drug-disease")
    public Response drugDiseasePredict(@RequestBody JSONObject jsonObject) {
        // 获取请求参数
        String dataName = (String) jsonObject.get("dataName");
        String drugOrDisease = (String) jsonObject.get("drugOrDisease");
        String num = (String) jsonObject.get("num");
        log.info("input parameter: dataPath: {}, dataName: {}, drugOrDisease: {}, num: {}",
                dataPath, dataName, drugOrDisease, num);

        Response res = new Response();
        if (StringUtils.isEmpty(dataName) || StringUtils.isEmpty(drugOrDisease)
                || StringUtils.isEmpty(num)) {
            res.setCode("200");
            res.setMsg("required parameter is empty");
            return res;
        }
        // 预测
        PredictionResult result = drugDiseaseService.predict(dataPath, dataName, drugOrDisease, num);
        if (result == null || !result.getIsSuccess()) {
            res.setCode("500");
            res.setMsg("server error, prediction fail");
            return res;
        }
        // 拼装返回数据
        res.setResult(result);
        res.setMsg("prediction successful");
        res.setCode("200");
        return res;
    }

    @PostMapping("/rna-disease")
    public Response rnaDiseasePredict(@RequestBody JSONObject jsonObject) {
        // 获取请求参数
        String dataName = (String) jsonObject.get("dataName");
        String ncRNAorDisease = (String) jsonObject.get("ncRNAorDisease");
        String num = (String) jsonObject.get("num");
        String numLayer = (String) jsonObject.get("numLayer");
        String numNode = (String) jsonObject.get("numNode");
        // 拼装数据路径
        String newPath = dataPath + dataName;
        log.info("input parameter: dataPath: {}, dataName: {}, ncRNAorDisease: {}, num: {}, numLayer: {}, numNode: {}",
                newPath, dataName, ncRNAorDisease, num, numLayer, numNode);

        Response res = new Response();
        if (StringUtils.isEmpty(dataName) || StringUtils.isEmpty(ncRNAorDisease) || StringUtils.isEmpty(num)
                || StringUtils.isEmpty(numLayer) || StringUtils.isEmpty(numNode)) {
            res.setCode("200");
            res.setMsg("required parameter is empty");
            return res;
        }
        PredictionResult result = rnaDiseaseService.predict(newPath, dataName, ncRNAorDisease, num, numLayer, numNode);
        if (result == null || !result.getIsSuccess()) {
            res.setCode("500");
            res.setMsg("server error, prediction fail");
            return res;
        }
        // 拼装返回数据
        res.setResult(result);
        res.setMsg("prediction successful");
        res.setCode("200");
        return res;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "success";
    }
}
