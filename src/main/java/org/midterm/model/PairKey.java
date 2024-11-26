package org.midterm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PairKey {
    private String publicKey;
    private String privateKey;

    public static PairKey create() {
        return new PairKey();
    }
}
