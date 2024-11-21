package org.midterm.service.encryption.symmetric_encryption;

import org.midterm.model.InformationData;

public interface SymmetricAlgorithm {
    String encrypt(String input, InformationData info) throws Exception;
    String decrypt(String input, InformationData info) throws Exception;
    String generateKey(InformationData info) throws Exception;
    String generateIV(InformationData info);
}
