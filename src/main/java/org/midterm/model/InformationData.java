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
    String key;
    String iv;
    String filePath;
    String inputFilePath;
    String outputFilePath;
    String inputText;
}
