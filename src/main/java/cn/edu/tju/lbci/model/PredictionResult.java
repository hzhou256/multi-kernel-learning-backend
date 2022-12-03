package cn.edu.tju.lbci.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResult {

    private List<String> list;

    private Boolean isSuccess;

    private String message;

}
