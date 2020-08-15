package com.wade.mobile.util.assets;

import java.io.InputStream;

public interface IAssetsFileOperation {
    void fileDo(InputStream inputStream, String str) throws Exception;

    boolean fileFliter(String str) throws Exception;
}