package org.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.midterm.constant.StringConstant;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassicAlgorithm {
    private String name;
    private Integer shift;
    private Integer aMultiplier;
    private Integer bShift;
    private Integer constant;
    private String key;
    private String substitutionKey;
    private String language = StringConstant.LANGUAGE_ENGLISH;
    private List<List<Integer>> hillMatrix;

    public static ClassicAlgorithm create(){
        return new ClassicAlgorithm();
    }
}
