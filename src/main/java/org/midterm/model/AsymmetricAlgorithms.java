package org.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsymmetricAlgorithms {
    private String publicKey;
    private String privateKey;
    private int keySize;
    private String algorithm;
    private String mode;
    private String padding;
    private String inputText;
    private String fileInputPath;
}
