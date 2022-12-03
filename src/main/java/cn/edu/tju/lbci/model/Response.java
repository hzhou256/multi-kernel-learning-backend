package cn.edu.tju.lbci.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String code;

    private String msg;

    private PredictionResult result;
}
