package cn.woodwhales.common.file;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static cn.woodwhales.common.file.FileEncryptTool.encodeFile;

/**
 * FileEncryptTool 使用示例
 * @author woodwhales created on 2021-10-01 17:56
 */
class FileEncryptToolTest {

    private static String originFileName;
    private static String originFileParentAbsolutePath;

    @BeforeAll
    public static void init() {
        URL resource = FileEncryptToolTest.class
                           .getClassLoader()
                           .getResource("test.png");

        originFileName = resource.getFile();
        File parentFile = new File(originFileName).getParentFile();
        originFileParentAbsolutePath = parentFile.getAbsolutePath();
    }

    /**
     * 加密测试
     */
    @Test
    public void testEncoder() {
        String encodeFileName = encodeFile(FileEncryptToolTest.originFileName,
                                       FileTypeHexStrEnum.PNG);
        Assertions.assertEquals("test_89504E47_504b0304_zip.PNG", StringUtils.substringAfterLast(encodeFileName, File.separator));
    }

    /**
     * 解密测试
     */
    @Test
    public void testDecode() {
        String encodeFileName = encodeFile(FileEncryptToolTest.originFileName,
                                           FileTypeHexStrEnum.PNG);
        String decodeFileName = FileEncryptTool.decodeFile(encodeFileName);
        Assertions.assertEquals(originFileParentAbsolutePath + File.separator + "test", decodeFileName);
    }

}