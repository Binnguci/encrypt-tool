package org.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SymmetricAlgorithms {
    private String name;
    private String key;
    private String iv;
    private String mode;
    private String padding;
    private String filePath;
    private String inputFilePath;
    private String inputText;
    public static SymmetricAlgorithms create(){
        return new SymmetricAlgorithms();
    }
}
