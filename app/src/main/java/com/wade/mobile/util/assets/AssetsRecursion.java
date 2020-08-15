package com.wade.mobile.util.assets;


import android.content.Context;
import com.ai.cmcchina.multiple.util.FileUtils;
import java.io.File;
import java.io.InputStream;

public class AssetsRecursion {
    private String baseDir;
    private Context context;
    private IAssetsFileOperation fileOper;
    private String separator = File.separator;

    public AssetsRecursion(Context context2, IAssetsFileOperation fileOper2) {
        this.context = context2;
        this.fileOper = fileOper2;
    }

    public void recursion(String baseDir2) throws Exception {
        this.baseDir = baseDir2;
        _recursion(baseDir2);
    }

    private void _recursion(String assetDir) throws Exception {
        for (String fileName : this.context.getAssets().list(assetDir)) {
            String filePath = assetDir + this.separator + fileName;
            if (!fileName.contains(FileUtils.HIDDEN_PREFIX)) {
                _recursion(filePath);
            } else if (this.fileOper.fileFliter(fileName)) {
                InputStream inputStream = this.context.getAssets().open(filePath);
                try {
                    this.fileOper.fileDo(inputStream, filePath.replace(this.baseDir + this.separator, ""));
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        }
    }
}