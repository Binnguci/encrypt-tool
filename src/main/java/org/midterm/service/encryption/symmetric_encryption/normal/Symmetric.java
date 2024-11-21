package org.midterm.service.encryption.symmetric_encryption.normal;

import java.util.List;

public abstract class Symmetric {

    protected List<String> transformations;

    protected Symmetric() {
        initTransformations();
    }

    public abstract void initTransformations();
}
