package org.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformationData {
    String algorithm;
    String mode;
    String padding;
    Integer shift;
    String key;
    String iv;
    Integer aMultiplier;
    String filePath;
    String resultFilePath;
    Integer bShift;
    String language;
    String inputFilePath;
    String outputFilePath;
    String inputText;
    String outputText;
}
